package com.ruc.platform.admin.banner.controller;

import com.ruc.platform.admin.banner.dto.BannerSaveDTO;
import com.ruc.platform.home.entity.HomeBanner;
import com.ruc.platform.home.mapper.HomeBannerMapper;
import com.ruc.platform.notice.entity.Notice;
import com.ruc.platform.notice.mapper.NoticeMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminBannerControllerTest {

    @Test
    void listShouldNormalizeSortOrdersToContinuousSequence() {
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        HomeBanner first = banner(60002L, 2);
        HomeBanner second = banner(60003L, 3);
        HomeBanner third = banner(60004L, 4);
        when(homeBannerMapper.selectList(any()))
                .thenReturn(List.of(first, second, third))
                .thenReturn(List.of(banner(60002L, 1), banner(60003L, 2), banner(60004L, 3)));
        AdminBannerController controller = new AdminBannerController(homeBannerMapper, mock(NoticeMapper.class));

        var result = controller.list();

        ArgumentCaptor<HomeBanner> bannerCaptor = ArgumentCaptor.forClass(HomeBanner.class);
        verify(homeBannerMapper, org.mockito.Mockito.times(3)).updateById(bannerCaptor.capture());
        assertThat(bannerCaptor.getAllValues()).extracting(HomeBanner::getSortOrder).containsExactly(1, 2, 3);
        assertThat(result.getData()).extracting(HomeBanner::getSortOrder).containsExactly(1, 2, 3);
    }

    @Test
    void createBannerShouldShiftExistingSortOrdersBeforeInsert() {
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        when(homeBannerMapper.selectList(any())).thenReturn(List.of());
        when(homeBannerMapper.selectCount(null)).thenReturn(0L);
        AdminBannerController controller = spy(new AdminBannerController(homeBannerMapper, mock(NoticeMapper.class)));
        doReturn(1L).when(controller).currentUserId();
        BannerSaveDTO dto = new BannerSaveDTO();
        dto.setTitle("新轮播图");
        dto.setTargetType("none");
        dto.setSortOrder(0);

        controller.create(dto);

        var ordered = inOrder(homeBannerMapper);
        ordered.verify(homeBannerMapper).update(any(), any());
        ArgumentCaptor<HomeBanner> bannerCaptor = ArgumentCaptor.forClass(HomeBanner.class);
        ordered.verify(homeBannerMapper).insert(bannerCaptor.capture());
        assertThat(bannerCaptor.getValue().getSortOrder()).isEqualTo(1);
    }

    @Test
    void updateBannerShouldMoveSortRangeBeforePersistingNewSortOrder() {
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        HomeBanner existing = new HomeBanner();
        existing.setId(60001L);
        existing.setSortOrder(3);
        when(homeBannerMapper.selectById(60001L)).thenReturn(existing);
        when(homeBannerMapper.selectList(any())).thenReturn(List.of());
        when(homeBannerMapper.selectCount(null)).thenReturn(3L);
        AdminBannerController controller = spy(new AdminBannerController(homeBannerMapper, mock(NoticeMapper.class)));
        doReturn(1L).when(controller).currentUserId();
        BannerSaveDTO dto = new BannerSaveDTO();
        dto.setTitle("调整轮播图");
        dto.setTargetType("none");
        dto.setSortOrder(0);

        controller.update(60001L, dto);

        var ordered = inOrder(homeBannerMapper);
        ordered.verify(homeBannerMapper).selectById(60001L);
        ordered.verify(homeBannerMapper).update(any(), any());
        ArgumentCaptor<HomeBanner> bannerCaptor = ArgumentCaptor.forClass(HomeBanner.class);
        ordered.verify(homeBannerMapper).updateById(bannerCaptor.capture());
        assertThat(bannerCaptor.getValue().getSortOrder()).isEqualTo(1);
    }

    @Test
    void deleteNoticeSourceBannerTurnsOffNoticeBannerFlag() {
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        HomeBanner banner = new HomeBanner();
        banner.setId(60006L);
        banner.setSourceType("notice");
        banner.setSourceNoticeId(91006L);
        banner.setSortOrder(2);
        when(homeBannerMapper.selectById(60006L)).thenReturn(banner);
        when(homeBannerMapper.selectList(any())).thenReturn(List.of());
        AdminBannerController controller = new AdminBannerController(homeBannerMapper, noticeMapper);

        controller.delete(60006L);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getId()).isEqualTo(91006L);
        assertThat(noticeCaptor.getValue().getIsBanner()).isFalse();
        assertThat(noticeCaptor.getValue().getUpdatedAt()).isNotNull();
        verify(homeBannerMapper).deleteById(60006L);
        verify(homeBannerMapper).update(any(), any());
    }

    private HomeBanner banner(Long id, Integer sortOrder) {
        HomeBanner banner = new HomeBanner();
        banner.setId(id);
        banner.setSortOrder(sortOrder);
        return banner;
    }
}
