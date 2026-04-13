package com.ruc.platform.knowledgeness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.knowledgeness.entity.KnowledgeCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识分类Mapper接口
 */
@Mapper
public interface KnowledgeCategoryMapper extends BaseMapper<KnowledgeCategory> {
}
