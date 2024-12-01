package top.javarem.infrastructure.dao.iService.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.iService.IRuleService;
import top.javarem.infrastructure.dao.entity.Rule;
import top.javarem.infrastructure.dao.mapper.RuleMapper;

@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements IRuleService {

}
