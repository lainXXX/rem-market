package top.javarem.infrastructure.dao.Iservice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.javarem.infrastructure.dao.Iservice.IRuleTreeNodeService;
import top.javarem.infrastructure.dao.entity.RuleTreeNode;
import top.javarem.infrastructure.dao.mapper.RuleTreeNodeMapper;

/**
 * @Author: rem
 * @Date: 2024/11/27/09:19
 * @Description:
 */
@Service
public class RuleTreeNodeServiceImpl extends ServiceImpl<RuleTreeNodeMapper, RuleTreeNode> implements IRuleTreeNodeService {
}
