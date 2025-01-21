package top.javarem.domain.activity.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.javarem.domain.activity.model.entity.SkuProductEntity;
import top.javarem.domain.activity.repository.IActivityRepository;
import top.javarem.domain.activity.service.IRaffleActivitySkuProductService;

import java.util.List;

/**
 * @Author: rem
 * @Date: 2025/01/21/15:52
 * @Description:
 */
@Service
@Slf4j
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {

    private final IActivityRepository repository;

    public RaffleActivitySkuProductService(IActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SkuProductEntity> getSkuProductListByActivityId(Long activityId) {

        return repository.getSkuProductListByActivityId(activityId);

    }

}
