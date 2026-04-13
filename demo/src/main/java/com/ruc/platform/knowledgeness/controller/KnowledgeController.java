package com.ruc.platform.knowledgeness.controller;

import com.ruc.platform.common.api.PageResult;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.knowledgeness.dto.KnowledgeArticleQueryDTO;
import com.ruc.platform.knowledgeness.service.KnowledgeService;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleDetailVO;
import com.ruc.platform.knowledgeness.vo.KnowledgeArticleListItemVO;
import com.ruc.platform.knowledgeness.vo.KnowledgeTemplateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识控制器
 * 处理知识条目查询、模板下载等请求
 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    /**
     * 获取知识条目列表
     * 支持关键词搜索、分类筛选
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/articles")
    public Result<PageResult<KnowledgeArticleListItemVO>> listArticles(KnowledgeArticleQueryDTO queryDTO) {
        log.info("查询知识条目列表，条件: {}", queryDTO);
        // 默认查询已发布的条目
        if (queryDTO.getStatus() == null) {
            queryDTO.setStatus(1);
        }
        PageResult<KnowledgeArticleListItemVO> result = knowledgeService.listArticles(queryDTO);
        return Result.ok(result);
    }

    /**
     * 获取知识条目详情
     * 
     * @param id 条目ID
     * @return 条目详情
     */
    @GetMapping("/articles/{id}")
    public Result<KnowledgeArticleDetailVO> getArticleDetail(@PathVariable Long id) {
        log.info("获取知识条目详情，id: {}", id);
        KnowledgeArticleDetailVO detail = knowledgeService.getArticleDetail(id);
        return Result.ok(detail);
    }

    /**
     * 获取知识模板列表
     * 
     * @return 模板列表
     */
    @GetMapping("/templates")
    public Result<List<KnowledgeTemplateVO>> listTemplates() {
        log.info("获取知识模板列表");
        List<KnowledgeTemplateVO> templates = knowledgeService.listTemplates();
        return Result.ok(templates);
    }
}
