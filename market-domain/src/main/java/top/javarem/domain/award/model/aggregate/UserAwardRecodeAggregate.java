package top.javarem.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.javarem.domain.award.model.entity.TaskEntity;
import top.javarem.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author: rem
 * @Date: 2024/12/08/19:57
 * @Description:用户抽奖奖品记录聚合
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecodeAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;

    private TaskEntity taskEntity;

}
