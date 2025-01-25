package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/22/00:21
 * @Description:
 */
@SpringBootTest
@Slf4j
public class ZKTest {

    @Autowired
    private CuratorFramework curator;

    @Test
    public void test_create() throws Exception {
        String s = curator.create().forPath("/app0", "rem".getBytes());
        log.info(s);
    }

    @Test
    public void test_create_with_mode() throws Exception {
        String s = curator.create().withMode(CreateMode.EPHEMERAL).forPath("/app0", "rem".getBytes());
        log.info(s);
    }

    @Test
    public void test_create01() throws Exception {
        String s = curator.create().creatingParentsIfNeeded().forPath("/app0", "rem".getBytes());
        log.info(s);
    }

    @Test
    public void test_get() throws Exception {
        byte[] s = curator.getData().forPath("/app0");
        log.info(new String(s));
    }

    @Test
    public void test_get_children() throws Exception {
        List<String> strings = curator.getChildren().forPath("/");
        log.info(strings.toString());
    }

    @Test
    public void test_get_status() throws Exception {
        Stat stat = new Stat();
        byte[] strings = curator.getData().storingStatIn(stat).forPath("/app0");
        log.info(stat.toString());
    }

    @Test
    public void test_node_cache() throws Exception {

        CuratorCache curatorCache = CuratorCache.build(curator, "app1");
        curatorCache.start();
        curatorCache.listenable().addListener((type, childData, data) -> {
            switch (type) {

                case NODE_CREATED:
                    log.info("app1被创建了");
                    break;

                case NODE_CHANGED:
                    log.info("app1被修改了");
                    break;
                case NODE_DELETED:
                    log.info("app1被删除了");
                    break;
                default:
                    break;

            }
        });
        // 保持客户端运行，以便监听事件
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            curator.close();
        }

    }

}
