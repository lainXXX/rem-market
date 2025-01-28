package top.javarem.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javarem.types.annotations.DCCValue;
import top.javarem.types.annotations.RateLimiterAccessInterceptor;
import top.javarem.types.common.constants.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Author: rem
 * @Date: 2025/01/26/15:48
 * @Description:速率限制器
 */
@Component
@Aspect
@Slf4j
public class RateLimiterAOP {

    @DCCValue("rateLimiter:close")
    private String rateLimiterSwitch;

    private final RedissonClient redissonClient;

    public RateLimiterAOP(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Pointcut("@annotation(top.javarem.types.annotations.RateLimiterAccessInterceptor)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(rateLimiterAccessInterceptor)")
    public Object doRouter(ProceedingJoinPoint joinPoint, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {
        // 0. 限流开关【open 开启、close 关闭】关闭后，不会走限流策略
        if (org.apache.commons.lang3.StringUtils.isBlank(rateLimiterSwitch) || "close".equals(rateLimiterSwitch)) {
            return joinPoint.proceed();
        }
        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation key is null");
        }
//        1.黑名单拦截 【单位时间内的请求数是否大于黑名单设置的请求数】
//        获取拦截字段
        String keyAttr = getAttrValue(key, joinPoint.getArgs());
        log.info("aop attr {}", keyAttr);
//        黑名单计数器
        String countKey = Constants.RedisKey.RATE_LIMITER_COUNT_KEY + keyAttr;
        RAtomicLong blacklistCounter = redissonClient.getAtomicLong(countKey);
//        黑名单拦截 如果key不为all 且黑名单拦截的规则中请求次数不为0 且缓存中的请求次数大于黑名单拦截规则次数 则执行拦截
        if (!"all".equals(key) && rateLimiterAccessInterceptor.blacklistCount() != 0 && blacklistCounter.get() > rateLimiterAccessInterceptor.blacklistCount()) {
            log.info("限流-黑名单拦截 keyAttr {}", keyAttr);
        }
//        2.限流处理
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(Constants.RedisKey.RATE_LIMITER_KEY.concat(keyAttr));
//        初始化限流器 尝试设置速率【每秒可通过的许可数】 如果该限流器已存在则不设置
        rateLimiter.trySetRate(RateType.OVERALL, rateLimiterAccessInterceptor.permitsPerSecond(), 1, RateIntervalUnit.SECONDS);
//        尝试获取许可
        if (!rateLimiter.tryAcquire()) {
//            如果这次请求获取许可失败则增加黑名单计数器次数
            blacklistCounter.incrementAndGet();
//            设置24小时后过期
            blacklistCounter.expire(24, TimeUnit.HOURS);
            log.info("限流-超频次拦截：{}", keyAttr);
            return fallBackMethod(joinPoint, rateLimiterAccessInterceptor.fallbackMethod());

        }
        return joinPoint.proceed();
    }

    private Object fallBackMethod(ProceedingJoinPoint joinPoint, String methodName) throws Throwable {

//        1.获取方法所属的对象实例
        Object target = joinPoint.getTarget();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        2.通过对象实例和方法名获取method对象
        Method method = target.getClass().getMethod(methodName, signature.getParameterTypes());
        return method.invoke(target, joinPoint.getArgs());
    }


    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                // filedValue = BeanUtils.getProperty(arg, attr);
                // fix: 使用lombok时，uId这种字段的get方法与idea生成的get方法不同，会导致获取不到属性值，改成反射获取解决
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }

    /**
     * 获取对象的特定属性值
     *
     * @param item 对象
     * @param name 属性名
     * @return 属性值
     * @author tang
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取方法，该方法同时兼顾继承类获取父类的属性
     *
     * @param item 对象
     * @param name 属性名
     * @return 该属性对应方法
     * @author tang
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
