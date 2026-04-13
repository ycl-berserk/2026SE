package com.ruc.platform.knowledgeness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.knowledgeness.entity.KnowledgeTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 知识模板Mapper接口
 */
@Mapper
public interface KnowledgeTemplateMapper extends BaseMapper<KnowledgeTemplate> {

    /**
     * 查询启用的模板列表
     * @return 模板列表
     */
    List<KnowledgeTemplate> selectEnabledTemplates();
}
