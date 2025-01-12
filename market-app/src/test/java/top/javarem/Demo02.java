package top.javarem;

import org.junit.jupiter.api.Test;
import top.javarem.infrastructure.dao.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rem
 * @Date: 2024/12/14/15:44
 * @Description:
 */
public class Demo02 {

    @Test
    public void test() {

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        Integer fd = map.get("fd");
        System.out.println(fd);

    }

}
