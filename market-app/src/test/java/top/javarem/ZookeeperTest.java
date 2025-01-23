package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: rem
 * @Date: 2025/01/22/00:21
 * @Description:
 */
@SpringBootTest
@Slf4j
public class ZookeeperTest {

    @Autowired
    private CuratorFramework curator;

}
