package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.iService.IAwardService;
import top.javarem.infrastructure.dao.entity.Award;
import top.javarem.infrastructure.dao.mapper.AwardMapper;

@Service
public class AwardServiceImpl extends ServiceImpl<AwardMapper, Award> implements IAwardService {

}
