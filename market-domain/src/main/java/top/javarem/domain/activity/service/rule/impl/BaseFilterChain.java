package top.javarem.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javarem.domain.activity.model.vo.ActivityStatusVO;
import top.javarem.domain.activity.service.rule.AbstractActivityChain;
import top.javarem.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import top.javarem.types.enums.ResponseCode;
import top.javarem.types.exception.AppException;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: rem
 * @Date: 2024/12/02/21:35
 * @Description:
 */
@Component("activity_base_filter")
@Slf4j
public class BaseFilterChain extends AbstractActivityChain {

    @Override
    public Boolean execute(DefaultActivityChainFactory.ActivityChainFilterVO chainFilterVO) {
        log.info("执行基础规则责任链");
//        1.判断活动是否开启
        if (!chainFilterVO.getStatus().equals(ActivityStatusVO.OPEN.getCode())) {
            throw new AppException(ResponseCode.ERROR_ACTIVITY_STATUS.getInfo(), ResponseCode.ERROR_ACTIVITY_STATUS.getInfo());
        }
//        2.判断当前时间是否在活动时间内
        Date now = new Date();
        if (!now.after(chainFilterVO.getBeginTime()) && now.before(chainFilterVO.getEndTime())) {
            throw new AppException(ResponseCode.ERROR_ACTIVITY_TIMEOUT.getInfo(), ResponseCode.ERROR_ACTIVITY_TIMEOUT.getInfo());
        }
//        3.判断活动库存是否充足
        if (chainFilterVO.getSkuSurplusStock() <= 0) {
            throw new AppException(ResponseCode.ERROR_ACTIVITY_STOCK_EMPTY.getInfo(), ResponseCode.ERROR_ACTIVITY_STOCK_EMPTY.getInfo());
        }
        return next().execute(chainFilterVO);
    }


}
