package top.javarem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.infrastructure.dao.Iservice.IAwardService;
import top.javarem.infrastructure.dao.entity.Award;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/11/24/22:01
 * @Description:
 */
@SpringBootTest
public class Demo {

    @Autowired
    private IAwardService awardService;

    @Test
    public void test() {
        List<Award> list = awardService.lambdaQuery()
                .select(Award::getAwardDesc)
                .list();
        System.out.println(list);
    }

}
