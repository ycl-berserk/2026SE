package com.ruc.platform.admin.notice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruc.platform.admin.notice.dto.NoticeCreateDTO;
import com.ruc.platform.admin.notice.dto.NoticeQueryDTO;
import com.ruc.platform.admin.notice.dto.NoticeTargetDTO;
import com.ruc.platform.admin.notice.dto.NoticeUpdateDTO;
import com.ruc.platform.admin.notice.vo.NoticeListItemVO;
import com.ruc.platform.admin.notice.vo.NoticeDetailVO;
import com.ruc.platform.common.api.PageResult;
import com.ruc.platform.home.entity.HomeBanner;
import com.ruc.platform.home.mapper.HomeBannerMapper;
import com.ruc.platform.notice.entity.Notice;
import com.ruc.platform.notice.mapper.NoticeMapper;
import com.ruc.platform.notice.mapper.UserMessageMapper;
import com.ruc.platform.student.mapper.StudentProfileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminNoticeServiceImplTest {

    @Test
    void createNoticePersistsCadresWithoutFinalFeedbackOwnerUntilPublish() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        when(userMessageMapper.countByNoticeId(any())).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);
        NoticeCreateDTO dto = new NoticeCreateDTO();
        dto.setTitle("反馈通知");
        dto.setContent("可反馈疑问。");
        dto.setFeedbackCadreIds(List.of(2002L, 2003L));

        NoticeDetailVO detail = service.createNotice(100L, dto);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).insert(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getFeedbackCounselorId()).isNull();
        assertThat(noticeCaptor.getValue().getFeedbackCadreIds()).isEqualTo("[2002,2003]");
        assertThat(detail.getFeedbackCounselorId()).isNull();
        assertThat(detail.getFeedbackCadreIds()).containsExactly(2002L, 2003L);
    }

    @Test
    void publishNoticeSetsFinalFeedbackOwnerToPublisher() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        StudentProfileMapper studentProfileMapper = mock(StudentProfileMapper.class);
        Notice notice = new Notice();
        notice.setId(91001L);
        notice.setTitle("发布通知");
        notice.setContent("谁发布谁负责。");
        notice.setStatus(0);
        when(noticeMapper.selectById(91001L)).thenReturn(notice);
        when(userMessageMapper.countByNoticeId(91001L)).thenReturn(0L);
        doReturn(List.of(1001L)).when(studentProfileMapper)
                .selectTargetStudentUserIds(any(), any(), nullable(String.class), nullable(String.class));
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, studentProfileMapper, new ObjectMapper(), null, null);

        service.publishNotice(91001L, 2002L);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getFeedbackCounselorId()).isEqualTo(2002L);
        assertThat(noticeCaptor.getValue().getStatus()).isEqualTo(1);
    }

    @Test
    void publishBannerNoticeCreatesHomeBannerEntry() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        StudentProfileMapper studentProfileMapper = mock(StudentProfileMapper.class);
        HomeBannerMapper homeBannerMapper = mock(HomeBannerMapper.class);
        Notice notice = new Notice();
        notice.setId(91006L);
        notice.setTitle("首页轮播通知");
        notice.setSummary("同步到轮播图");
        notice.setContent("发布后应展示。");
        notice.setStatus(0);
        notice.setIsBanner(true);
        notice.setCreatedBy(100L);
        when(noticeMapper.selectById(91006L)).thenReturn(notice);
        when(userMessageMapper.countByNoticeId(91006L)).thenReturn(0L);
        doReturn(List.of(1001L)).when(studentProfileMapper)
                .selectTargetStudentUserIds(any(), any(), nullable(String.class), nullable(String.class));
        when(homeBannerMapper.selectOne(any())).thenReturn(null);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(
                noticeMapper,
                userMessageMapper,
                studentProfileMapper,
                new ObjectMapper(),
                null,
                homeBannerMapper
        );

        service.publishNotice(91006L, 2002L);

        org.mockito.ArgumentCaptor<HomeBanner> bannerCaptor = org.mockito.ArgumentCaptor.forClass(HomeBanner.class);
        verify(homeBannerMapper).insert(bannerCaptor.capture());
        assertThat(bannerCaptor.getValue().getTitle()).isEqualTo("首页轮播通知");
        assertThat(bannerCaptor.getValue().getSubtitle()).isEqualTo("同步到轮播图");
        assertThat(bannerCaptor.getValue().getTargetType()).isEqualTo("notice");
        assertThat(bannerCaptor.getValue().getTargetId()).isEqualTo(91006L);
        assertThat(bannerCaptor.getValue().getSourceType()).isEqualTo("notice");
        assertThat(bannerCaptor.getValue().getSourceNoticeId()).isEqualTo(91006L);
        assertThat(bannerCaptor.getValue().getStatus()).isEqualTo(1);
    }

    @Test
    void createNoticePersistsAndReturnsAttachmentFileId() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        when(userMessageMapper.countByNoticeId(any())).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);
        NoticeCreateDTO dto = new NoticeCreateDTO();
        dto.setTitle("附件通知");
        dto.setSummary("带附件");
        dto.setContent("请下载附件查看材料。");
        dto.setAttachmentFileId(99001L);

        NoticeDetailVO detail = service.createNotice(100L, dto);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).insert(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getAttachmentFileId()).isEqualTo(99001L);
        assertThat(detail.getAttachmentFileId()).isEqualTo(99001L);
    }

    @Test
    void createNoticePersistsAndReturnsBannerFlag() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        when(userMessageMapper.countByNoticeId(any())).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);
        NoticeCreateDTO dto = new NoticeCreateDTO();
        dto.setTitle("首页轮播通知");
        dto.setContent("展示在首页轮播。");
        dto.setIsBanner(true);

        NoticeDetailVO detail = service.createNotice(100L, dto);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).insert(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getIsBanner()).isTrue();
        assertThat(detail.getIsBanner()).isTrue();
    }

    @Test
    void updateNoticeReturnsBannerFlagForEditEcho() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        Notice notice = new Notice();
        notice.setId(91002L);
        notice.setTitle("待编辑通知");
        notice.setContent("旧内容");
        notice.setStatus(0);
        when(noticeMapper.selectById(91002L)).thenReturn(notice);
        when(userMessageMapper.countByNoticeId(91002L)).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);
        NoticeUpdateDTO dto = new NoticeUpdateDTO();
        dto.setTitle("待编辑通知");
        dto.setContent("新内容");
        dto.setIsBanner(true);

        NoticeDetailVO detail = service.updateNotice(91002L, dto);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        verify(noticeMapper, times(2)).selectById(91002L);
        assertThat(noticeCaptor.getValue().getIsBanner()).isTrue();
        assertThat(detail.getIsBanner()).isTrue();
    }

    @Test
    void updateNoticeAllowsRemovingAttachmentFileId() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        Notice notice = new Notice();
        notice.setId(91004L);
        notice.setTitle("待移除附件通知");
        notice.setContent("旧内容");
        notice.setStatus(0);
        notice.setAttachmentFileId(99001L);
        when(noticeMapper.selectById(91004L)).thenReturn(notice);
        when(userMessageMapper.countByNoticeId(91004L)).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);
        NoticeUpdateDTO dto = new NoticeUpdateDTO();
        dto.setTitle("待移除附件通知");
        dto.setContent("新内容");
        dto.setAttachmentFileId(null);

        NoticeDetailVO detail = service.updateNotice(91004L, dto);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        assertThat(noticeCaptor.getValue().getAttachmentFileId()).isNull();
        assertThat(detail.getAttachmentFileId()).isNull();
    }

    @Test
    void restoreNoticePutsOfflineNoticeBackOnlineWithoutRedelivery() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        Notice notice = new Notice();
        notice.setId(91005L);
        notice.setTitle("误下架通知");
        notice.setContent("内容");
        notice.setStatus(2);
        when(noticeMapper.selectById(91005L)).thenReturn(notice);
        when(userMessageMapper.countByNoticeId(91005L)).thenReturn(3L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);

        service.restoreNotice(91005L);

        org.mockito.ArgumentCaptor<Notice> noticeCaptor = org.mockito.ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper).updateById(noticeCaptor.capture());
        verify(userMessageMapper, times(0)).insert(any());
        assertThat(noticeCaptor.getValue().getStatus()).isEqualTo(1);
    }

    @Test
    void listNoticesReturnsBannerFlag() {
        NoticeMapper noticeMapper = mock(NoticeMapper.class);
        UserMessageMapper userMessageMapper = mock(UserMessageMapper.class);
        Notice notice = new Notice();
        notice.setId(91003L);
        notice.setTitle("列表轮播通知");
        notice.setContent("内容");
        notice.setStatus(1);
        notice.setPriority(0);
        notice.setIsBanner(true);
        when(noticeMapper.selectCount(any())).thenReturn(1L);
        when(noticeMapper.selectList(any())).thenReturn(List.of(notice));
        when(userMessageMapper.countByNoticeId(91003L)).thenReturn(0L);
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(noticeMapper, userMessageMapper, null, new ObjectMapper(), null, null);

        PageResult<NoticeListItemVO> result = service.listNotices(new NoticeQueryDTO());

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getIsBanner()).isTrue();
    }

    @Test
    void normalizeTargetSplitsPastedGradeText() {
        AdminNoticeServiceImpl service = new AdminNoticeServiceImpl(null, null, null, new ObjectMapper(), null, null);
        NoticeTargetDTO target = new NoticeTargetDTO();
        target.setGrades(List.of("2024本 2023本 2022硕"));

        NoticeTargetDTO normalized = ReflectionTestUtils.invokeMethod(service, "normalizeTarget", target);

        assertThat(normalized.getGrades()).containsExactly("2024本", "2023本", "2022硕");
        assertThat(normalized.getGrade()).isEqualTo("2024本");
    }
}
