package top.javarem.domain.award.service;

import top.javarem.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author: rem
 * @Date: 2024/12/08/18:16
 * @Description:奖品发放接口
 */
public interface IAwardService {

    /**
     * 写入抽奖之后的中奖记录
     * @param userAwardRecordEntity
     */
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

}
