package top.javarem.infrastructure.dao.Iservice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.Iservice.IRuleTreeService;
import top.javarem.infrastructure.dao.entity.RuleTree;
import top.javarem.infrastructure.dao.mapper.RuleTreeMapper;

/**
 * @Author: rem
 * @Date: 2024/11/27/09:14
 * @Description:
 */
@Service
public class RuleTreeServiceImpl extends ServiceImpl<RuleTreeMapper, RuleTree> implements IRuleTreeService {
}
