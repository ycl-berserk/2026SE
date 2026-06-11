package com.ruc.platform.admin.banner.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruc.platform.admin.banner.dto.BannerSaveDTO;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.home.entity.HomeBanner;
import com.ruc.platform.home.mapper.HomeBannerMapper;
import com.ruc.platform.notice.entity.Notice;
import com.ruc.platform.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final HomeBannerMapper homeBannerMapper;
    private final NoticeMapper noticeMapper;

    @GetMapping
    @Transactional
    public Result<List<HomeBanner>> list() {
        normalizeBannerSortOrders();
        return Result.ok(orderedBanners());
    }

    @PostMapping
    @Transactional
    public Result<Long> create(@RequestBody BannerSaveDTO dto) {
        normalizeBannerSortOrders();
        int total = Math.toIntExact(homeBannerMapper.selectCount(null));
        int sortOrder = normalizedSortOrder(dto.getSortOrder(), total + 1);
        shiftBannersFrom(sortOrder);
        HomeBanner banner = new HomeBanner();
        banner.setTitle(dto.getTitle());
        banner.setSubtitle(dto.getSubtitle());
        banner.setTargetType(dto.getTargetType());
        banner.setTargetId(dto.getTargetId());
        banner.setTargetPath(dto.getTargetPath());
        banner.setSortOrder(sortOrder);
        banner.setCreatedBy(currentUserId());
        banner.setUpdatedBy(currentUserId());
        banner.setCreatedAt(LocalDateTime.now());
        banner.setUpdatedAt(LocalDateTime.now());
        homeBannerMapper.insert(banner);
        return Result.ok(banner.getId());
    }

    @PutMapping("/{id}")
    @Transactional
    public Result<Void> update(@PathVariable Long id, @RequestBody BannerSaveDTO dto) {
        HomeBanner existing = homeBannerMapper.selectById(id);
        if (existing == null) {
            throw new BizException(ResultCode.NOT_FOUND, "轮播图不存在");
        }
        normalizeBannerSortOrders();
        existing = homeBannerMapper.selectById(id);
        int total = Math.toIntExact(homeBannerMapper.selectCount(null));
        int oldSortOrder = normalizedSortOrder(existing.getSortOrder(), total);
        int newSortOrder = normalizedSortOrder(dto.getSortOrder(), total);
        moveBannerSortOrder(id, oldSortOrder, newSortOrder);
        HomeBanner banner = new HomeBanner();
        banner.setId(id);
        banner.setTitle(dto.getTitle());
        banner.setSubtitle(dto.getSubtitle());
        banner.setTargetType(dto.getTargetType());
        banner.setTargetId(dto.getTargetId());
        banner.setTargetPath(dto.getTargetPath());
        banner.setSortOrder(newSortOrder);
        banner.setUpdatedBy(currentUserId());
        banner.setUpdatedAt(LocalDateTime.now());
        homeBannerMapper.updateById(banner);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public Result<Void> delete(@PathVariable Long id) {
        HomeBanner banner = homeBannerMapper.selectById(id);
        if (banner != null && "notice".equals(banner.getSourceType()) && banner.getSourceNoticeId() != null) {
            Notice notice = new Notice();
            notice.setId(banner.getSourceNoticeId());
            notice.setIsBanner(false);
            notice.setUpdatedAt(LocalDateTime.now());
            noticeMapper.updateById(notice);
        }
        homeBannerMapper.deleteById(id);
        if (banner != null) {
            closeBannerSortGap(normalizedSortOrder(banner.getSortOrder(), Integer.MAX_VALUE));
            normalizeBannerSortOrders();
        }
        return Result.ok();
    }

    private int normalizedSortOrder(Integer sortOrder, int maxSortOrder) {
        int value = sortOrder == null || sortOrder < 1 ? 1 : sortOrder;
        return Math.min(value, Math.max(1, maxSortOrder));
    }

    private List<HomeBanner> orderedBanners() {
        return homeBannerMapper.selectList(new LambdaQueryWrapper<HomeBanner>()
                .orderByAsc(HomeBanner::getSortOrder)
                .orderByAsc(HomeBanner::getId));
    }

    private void normalizeBannerSortOrders() {
        List<HomeBanner> banners = orderedBanners();
        for (int i = 0; i < banners.size(); i++) {
            HomeBanner banner = banners.get(i);
            int expectedSortOrder = i + 1;
            if (banner.getSortOrder() != null && banner.getSortOrder() == expectedSortOrder) {
                continue;
            }
            HomeBanner update = new HomeBanner();
            update.setId(banner.getId());
            update.setSortOrder(expectedSortOrder);
            update.setUpdatedAt(LocalDateTime.now());
            homeBannerMapper.updateById(update);
        }
    }

    private void shiftBannersFrom(int sortOrder) {
        homeBannerMapper.update(null, new LambdaUpdateWrapper<HomeBanner>()
                .ge(HomeBanner::getSortOrder, sortOrder)
                .setSql("sort_order = sort_order + 1"));
    }

    private void moveBannerSortOrder(Long id, int oldSortOrder, int newSortOrder) {
        if (newSortOrder == oldSortOrder) {
            return;
        }
        LambdaUpdateWrapper<HomeBanner> wrapper = new LambdaUpdateWrapper<HomeBanner>()
                .ne(HomeBanner::getId, id);
        if (newSortOrder < oldSortOrder) {
            homeBannerMapper.update(null, wrapper
                    .ge(HomeBanner::getSortOrder, newSortOrder)
                    .lt(HomeBanner::getSortOrder, oldSortOrder)
                    .setSql("sort_order = sort_order + 1"));
            return;
        }
        homeBannerMapper.update(null, wrapper
                .gt(HomeBanner::getSortOrder, oldSortOrder)
                .le(HomeBanner::getSortOrder, newSortOrder)
                .setSql("sort_order = sort_order - 1"));
    }

    private void closeBannerSortGap(int sortOrder) {
        homeBannerMapper.update(null, new LambdaUpdateWrapper<HomeBanner>()
                .gt(HomeBanner::getSortOrder, sortOrder)
                .setSql("sort_order = sort_order - 1"));
    }

    Long currentUserId() {
        return StpUtil.getLoginIdAsLong();
    }
}
