package top.javarem.Api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.api.IRaffleStrategyService;
import top.javarem.api.dto.DisplayAwardDTO;
import top.javarem.api.dto.DisplayAwardRequestDTO;
import top.javarem.api.response.Response;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2024/12/14/16:40
 * @Description:
 */
@SpringBootTest
@Slf4j
public class RaffleStrategyTest {

    @Autowired
    private IRaffleStrategyService raffleStrategyService;

    @Test
    public void test_displayAward() {
        DisplayAwardRequestDTO request = new DisplayAwardRequestDTO();
        request.setUserId("rem");
        request.setActivityId(100301L);
        Response<List<DisplayAwardDTO>> listResponse = raffleStrategyService.displayAwardList(request);
        log.info(listResponse.toString());
    }

}
