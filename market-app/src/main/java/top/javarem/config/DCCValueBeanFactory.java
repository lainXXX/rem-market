package top.javarem.config;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import top.javarem.types.annotations.DCCValue;
import top.javarem.types.common.constants.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2025/01/24/18:14
 * @Description:
 */
@Configuration
@Slf4j
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "/rem-market-dcc";

    private static final String BASE_CONFIG_PATH_CONFIG = BASE_CONFIG_PATH + "/config";

    private final Map<String, Object> dccGroup = new HashMap<>();

    @Autowired(required = false)
    private CuratorFramework client;
    public DCCValueBeanFactory() throws Exception {
        if (client == null) return;
        /**
         * 检查该路径的子节点是否存在 不存在则创建
         */
//        1.检查并创建基础节点
        if (null == client.checkExists().forPath(BASE_CONFIG_PATH_CONFIG)) {
            client.create().creatingParentsIfNeeded().forPath(BASE_CONFIG_PATH_CONFIG);
            log.info("DCC 节点监听 base node {} not absent create new done!", BASE_CONFIG_PATH_CONFIG);
        }

        /**
         * CuratorCache 是一个用于监听 ZooKeeper 节点数据变化的工具。
         * 当你创建并启动一个 CuratorCache 实例时，Curator 会自动为指定的路径注册一个监听器，当该路径下的节点数据发生变化时，你可以收到通知。
         */
//        2. 创建监听器
        CuratorCache curatorCache = CuratorCache.build(client, BASE_CONFIG_PATH_CONFIG);
//        3.添加监听器
        curatorCache.listenable().addListener((type, childData, data) -> {
            switch (type) {
                case NODE_CHANGED:
                    String dccValuePath = data.getPath();
                    Object objBean = dccGroup.get(dccValuePath);
                    if (null == objBean) return;
                    try {
                        Class<?> objBeanClass = objBean.getClass();
                        // 检查 objBean 是否是代理对象
                        if (AopUtils.isAopProxy(objBean)) {
                            // 获取代理对象的目标对象
                            objBeanClass = AopUtils.getTargetClass(objBean);
//                            objBeanClass = AopProxyUtils.ultimateTargetClass(objBean);
                        }
                        String filedName = dccValuePath.substring(dccValuePath.lastIndexOf("/") + 1);
                        String newValue = new String(data.getData());
                        
                        Field field = objBeanClass.getDeclaredField(filedName);
                        field.setAccessible(true);
                        field.set(objBean, newValue);
                        field.setAccessible(false);
                        log.info("zookeeper节点配置更新成功 path:{} value:{}", dccValuePath, newValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                default:
                    break;
            }
        });
//        4.开启监听器
        curatorCache.start();


        log.info("创建zookeeper监听器 节点:{}", BASE_CONFIG_PATH_CONFIG);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (client == null) return bean;

        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        if (AopUtils.isAopProxy(bean)) {
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }
        Field[] declaredFields = targetBeanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!field.isAnnotationPresent(DCCValue.class)) continue;
            String value = field.getAnnotation(DCCValue.class).value();
            if (StringUtils.isBlank(value)) {
                throw new RuntimeException("dccValue is not configured");
            }
            String[] splits = value.split(Constants.COLON);
            String key = splits[0];
            String defaultValue = splits.length == 2 ? splits[1] : null;
            try {
                String keyPath = BASE_CONFIG_PATH_CONFIG.concat("/").concat(key);
                if (null == client.checkExists().forPath(keyPath)){
                    client.create().creatingParentsIfNeeded().forPath(keyPath, defaultValue.getBytes());
                    if (StringUtils.isNotBlank(defaultValue)) {
                        field.setAccessible(true);
                        field.set(targetBeanObject, defaultValue);
                        field.setAccessible(false);
                    }
                    log.info("DCC 节点监听 创建节点 {}", keyPath);
                }
                 else {
                    String configValue = new String(client.getData().forPath(keyPath));
                    if (StringUtils.isNotBlank(configValue)) {
                        field.setAccessible(true);
                        field.set(targetBeanObject, configValue);
                        field.setAccessible(false);
                    }
                    log.info("DCC 节点监听 设置配置 {} {} {}", keyPath, field.getName(), configValue);
                }
                dccGroup.put(keyPath, targetBeanObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return bean;

    }
}
