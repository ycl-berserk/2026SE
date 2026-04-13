package com.ruc.platform.knowledgeness.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruc.platform.common.api.PageResult;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.knowledgeness.dto.KnowledgeArticleQueryDTO;
import com.ruc.platform.knowledgeness.entity.KnowledgeArticle;
import com.ruc.platform.knowledgeness.entity.KnowledgeTemplate;
import com.ruc.platform.knowledgeness.mapper.KnowledgeArticleMapper;
import com.ruc.platform.knowledgeness.mapper.KnowledgeTemplateMapper;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleDetailVO;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleListItemVO;
import com.ruc.platform.knowledgeness.vo.KnowledgeTemplateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeArticleMapper articleMapper;
    private final KnowledgeTemplateMapper templateMapper;

    @Override
    public PageResult<KnowledgeArticleListItemVO> listArticles(KnowledgeArticleQueryDTO queryDTO) {
        Page<KnowledgeArticle> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<KnowledgeArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getStatus() != null, KnowledgeArticle::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getCategoryId() != null, KnowledgeArticle::getCategoryId, queryDTO.getCategoryId())
                .and(queryDTO.getKeyword() != null && !queryDTO.getKeyword().isBlank(), w -> w
                        .like(KnowledgeArticle::getTitle, queryDTO.getKeyword())
                        .or()
                        .like(KnowledgeArticle::getSummary, queryDTO.getKeyword()))
                .orderByDesc(KnowledgeArticle::getPublishTime);

        Page<KnowledgeArticle> articlePage = articleMapper.selectPage(page, wrapper);
        List<KnowledgeArticleListItemVO> list = articlePage.getRecords().stream()
                .map(this::convertToListItemVO)
                .collect(Collectors.toList());

        return PageResult.of(articlePage.getTotal(), articlePage.getCurrent(), articlePage.getSize(), list);
    }

    @Override
    public KnowledgeArticleDetailVO getArticleDetail(Long id) {
        KnowledgeArticleDetailVO detail = articleMapper.selectArticleDetail(id);
        if (detail == null) {
            throw new BizException(ResultCode.NOT_FOUND, "知识条目不存在");
        }

        KnowledgeArticle article = articleMapper.selectById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            articleMapper.updateById(article);
            detail.setViewCount(article.getViewCount());
        }
        return detail;
    }

    @Override
    public List<KnowledgeTemplateVO> listTemplates() {
        return templateMapper.selectEnabledTemplates().stream()
                .map(this::convertToTemplateVO)
                .collect(Collectors.toList());
    }

    private KnowledgeArticleListItemVO convertToListItemVO(KnowledgeArticle article) {
        KnowledgeArticleListItemVO vo = new KnowledgeArticleListItemVO();
        vo.setId(article.getId());
        vo.setTitle(article.getTitle());
        vo.setSummary(article.getSummary());
        vo.setPublishTime(article.getPublishTime());
        vo.setViewCount(article.getViewCount());
        vo.setCategoryName("");
        return vo;
    }

    private KnowledgeTemplateVO convertToTemplateVO(KnowledgeTemplate template) {
        KnowledgeTemplateVO vo = new KnowledgeTemplateVO();
        BeanUtils.copyProperties(template, vo);
        return vo;
    }
}
