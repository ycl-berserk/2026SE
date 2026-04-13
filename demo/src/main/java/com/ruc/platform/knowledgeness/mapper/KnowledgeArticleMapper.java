package com.ruc.platform.knowledgeness.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.knowledgeness.entity.KnowledgeArticle;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleDetailVO;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleListItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识条目Mapper接口
 */
@Mapper
public interface KnowledgeArticleMapper extends BaseMapper<KnowledgeArticle> {

    /**
     * 分页查询知识条目（带分类名称）
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param status 状态
     * @return 知识条目列表
     */
    List<KnowledgeArticleListItemVO> selectArticlePage(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("status") Integer status
    );

    /**
     * 查询知识条目详情（带分类名称和关键词）
     * @param id 条目ID
     * @return 知识条目详情
     */
    KnowledgeArticleDetailVO selectArticleDetail(@Param("id") Long id);
}
