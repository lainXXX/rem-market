package top.javarem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.types.common.constants.Constants;

/**
 * @Author: rem
 * @Date: 2025/01/27/18:52
 * @Description:
 */
@SpringBootTest
@Slf4j
public class RedisTest {

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void test() {

    }

}
