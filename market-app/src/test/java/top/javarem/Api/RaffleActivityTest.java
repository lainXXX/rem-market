package top.javarem.Api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.javarem.api.IRaffleActivityService;
import top.javarem.api.dto.ActivityDrawRequestDTO;
import top.javarem.api.dto.ActivityDrawResponseDTO;
import top.javarem.api.response.Response;
import top.javarem.infrastructure.dao.entity.RaffleActivity;

import javax.annotation.Resource;

/**
 * @Author: rem
 * @Date: 2024/12/14/17:19
 * @Description:
 */
@SpringBootTest
@Slf4j
public class RaffleActivityTest {

    @Resource
    private IRaffleActivityService raffleActivityService;

    @Test
    public void test_draw() {

        ActivityDrawRequestDTO activityDrawRequestDTO = new ActivityDrawRequestDTO();
        activityDrawRequestDTO.setUserId("user001");
        activityDrawRequestDTO.setActivityId(Long.valueOf(100301));

        Response<ActivityDrawResponseDTO> draw = raffleActivityService.draw(activityDrawRequestDTO);
        log.info(draw.toString());

    }

}
