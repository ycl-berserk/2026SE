package com.ruc.platform.knowledgeness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.knowledgeness.entity.KnowledgeTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgeTemplateMapper extends BaseMapper<KnowledgeTemplate> {

    @Select("""
            SELECT
                id,
                name,
                description,
                category,
                file_id,
                format,
                download_count,
                status,
                created_at,
                updated_at
            FROM knowledge_template
            WHERE status = 1
            ORDER BY created_at DESC
            """)
    List<KnowledgeTemplate> selectEnabledTemplates();
}
