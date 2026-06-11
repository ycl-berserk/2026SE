package com.ruc.platform.admin.banner.controller;

import com.ruc.platform.home.entity.HomeBanner;
import com.ruc.platform.home.mapper.HomeBannerMapper;
import com.ruc.platform.notice.entity.Notice;
import com.ruc.platform.notice.mapper.NoticeMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminBannerControllerTest {

    @Test
    void deleteNoticeSourceBannerTurnsOffNoticeBannerFlag() {
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        HomeBanner banner = new HomeBanner();
        banner.setId(60006L);
        banner.setSourceType("notice");
        banner.setSourceNoticeId(91006L);
        when(homeBannerMapper.selectById(60006L)).thenReturn(banner);
        AdminBannerController controller = new AdminBannerController(homeBannerMapper, noticeMapper);

        controller.delete(60006L);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getId()).isEqualTo(91006L);
        assertThat(noticeCaptor.getValue().getIsBanner()).isFalse();
        assertThat(noticeCaptor.getValue().getUpdatedAt()).isNotNull();
        verify(homeBannerMapper).deleteById(60006L);
    }
}
