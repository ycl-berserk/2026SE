package com.ruc.platform.party.service;

import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.party.dto.PartyReportDTO;
import com.ruc.platform.party.entity.PartyReminder;
import com.ruc.platform.party.entity.PartyReport;
import com.ruc.platform.party.entity.PartyStudentProgress;
import com.ruc.platform.party.entity.PartyStudentRecord;
import com.ruc.platform.party.mapper.PartyReminderMapper;
import com.ruc.platform.party.mapper.PartyReportMapper;
import com.ruc.platform.party.mapper.PartyStudentProgressMapper;
import com.ruc.platform.party.mapper.PartyStudentRecordMapper;
import com.ruc.platform.party.vo.PartyOverviewVO;
import com.ruc.platform.party.vo.PartyRecordVO;
import com.ruc.platform.party.vo.PartyReminderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyStudentProgressMapper progressMapper;
    private final PartyStudentRecordMapper recordMapper;
    private final PartyReminderMapper reminderMapper;
    private final PartyReportMapper reportMapper;

    @Override
    public PartyOverviewVO getOverview(Long userId) {
        PartyStudentProgress progress = progressMapper.selectByUserId(userId);
        if (progress == null) {
            throw new BizException(ResultCode.NOT_FOUND, "党团进度不存在");
        }

        List<PartyReminder> pendingReminders = reminderMapper.selectPendingByUserId(userId);
        PartyOverviewVO vo = new PartyOverviewVO();
        vo.setCurrentStageCode(progress.getCurrentStageCode());
        vo.setCurrentStageName(getStageNameByCode(progress.getCurrentStageCode()));
        vo.setPendingReminders(pendingReminders.size());
        return vo;
    }

    @Override
    public List<PartyRecordVO> getRecords(Long userId) {
        return recordMapper.selectByUserId(userId).stream()
                .map(this::convertToRecordVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PartyReminderVO> getReminders(Long userId) {
        return reminderMapper.selectPendingByUserId(userId).stream()
                .map(this::convertToReminderVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitReport(Long userId, PartyReportDTO reportDTO) {
        PartyReport report = new PartyReport();
        report.setUserId(userId);
        report.setTitle(reportDTO.getTitle());
        report.setContent(reportDTO.getContent());
        report.setFileId(reportDTO.getFileId());
        report.setSubmitTime(LocalDateTime.now());
        report.setStatus(0);
        reportMapper.insert(report);
        log.info("提交思想汇报成功，userId: {}, reportId: {}", userId, report.getId());
    }

    private String getStageNameByCode(String stageCode) {
        return switch (stageCode) {
            case "applicant" -> "入党申请人";
            case "activist" -> "积极分子";
            case "development_target" -> "发展对象";
            case "probationary_member" -> "预备党员";
            case "full_member" -> "正式党员";
            default -> "未知阶段";
        };
    }

    private PartyRecordVO convertToRecordVO(PartyStudentRecord record) {
        PartyRecordVO vo = new PartyRecordVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }

    private PartyReminderVO convertToReminderVO(PartyReminder reminder) {
        PartyReminderVO vo = new PartyReminderVO();
        BeanUtils.copyProperties(reminder, vo);
        return vo;
    }
}
