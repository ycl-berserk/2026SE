package com.ruc.platform.leave.service;

import com.ruc.platform.auth.entity.User;
import com.ruc.platform.auth.mapper.UserMapper;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.leave.dto.LeaveCreateDTO;
import com.ruc.platform.leave.entity.ApprovalRecord;
import com.ruc.platform.leave.entity.LeaveApplication;
import com.ruc.platform.leave.mapper.ApprovalRecordMapper;
import com.ruc.platform.leave.mapper.LeaveApplicationMapper;
import com.ruc.platform.leave.vo.LeaveApplicationDetailVO;
import com.ruc.platform.leave.vo.LeaveApplicationListItemVO;
import com.ruc.platform.leave.vo.LeaveTimelineVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 请假服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private static final String TYPE_CODE_LEAVE = "leave";

    private final LeaveApplicationMapper leaveApplicationMapper;
    private final ApprovalRecordMapper approvalRecordMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(Long userId, LeaveCreateDTO createDTO) {
        if (createDTO.getLeaveEndDate().isBefore(createDTO.getLeaveStartDate())) {
            throw new BizException(ResultCode.PARAM_ERROR, "请假结束日期不能早于开始日期");
        }

        LeaveApplication application = new LeaveApplication();
        application.setUserId(userId);
        application.setTypeCode(TYPE_CODE_LEAVE);
        application.setTitle(createDTO.getTitle());
        application.setReason(createDTO.getReason());
        application.setLeaveStartDate(createDTO.getLeaveStartDate());
        application.setLeaveEndDate(createDTO.getLeaveEndDate());
        application.setContactPhone(createDTO.getContactPhone());
        application.setFileId(createDTO.getFileId());
        application.setStatus(0);
        application.setSubmitTime(LocalDateTime.now());
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        leaveApplicationMapper.insert(application);

        insertRecord(application.getId(), userId, "submit", "提交请假申请");
        return application.getId();
    }

    @Override
    public List<LeaveApplicationListItemVO> listMine(Long userId) {
        return leaveApplicationMapper.selectLeaveByUserId(userId).stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveApplicationDetailVO getMineDetail(Long userId, Long id) {
        LeaveApplication application = getLeaveById(id);
        if (!application.getUserId().equals(userId)) {
            throw new BizException(ResultCode.FORBIDDEN, "无权查看该申请");
        }
        return toDetail(application);
    }

    @Override
    public List<LeaveApplicationListItemVO> listForReviewer(Long reviewerId, Integer status) {
        ensureReviewer(reviewerId);
        return leaveApplicationMapper.selectAllLeaves(status).stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveApplicationDetailVO getReviewerDetail(Long reviewerId, Long id) {
        ensureReviewer(reviewerId);
        LeaveApplication application = getLeaveById(id);
        return toDetail(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long reviewerId, Long id, String comment) {
        ensureReviewer(reviewerId);
        LeaveApplication application = getLeaveById(id);
        ensurePending(application);

        application.setStatus(2);
        application.setApprovedBy(reviewerId);
        application.setApprovedAt(LocalDateTime.now());
        application.setRejectReason(null);
        application.setUpdatedAt(LocalDateTime.now());
        leaveApplicationMapper.updateById(application);

        insertRecord(application.getId(), reviewerId, "approve", comment);
        log.info("请假申请审批通过，id: {}, reviewerId: {}", id, reviewerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long reviewerId, Long id, String comment) {
        ensureReviewer(reviewerId);
        LeaveApplication application = getLeaveById(id);
        ensurePending(application);

        application.setStatus(3);
        application.setApprovedBy(reviewerId);
        application.setApprovedAt(LocalDateTime.now());
        application.setRejectReason(comment);
        application.setUpdatedAt(LocalDateTime.now());
        leaveApplicationMapper.updateById(application);

        insertRecord(application.getId(), reviewerId, "reject", comment);
        log.info("请假申请驳回，id: {}, reviewerId: {}", id, reviewerId);
    }

    private void ensurePending(LeaveApplication application) {
        if (application.getStatus() == null || application.getStatus() != 0) {
            throw new BizException(ResultCode.BIZ_ERROR, "仅待审批申请可处理");
        }
    }

    private LeaveApplication getLeaveById(Long id) {
        LeaveApplication application = leaveApplicationMapper.selectById(id);
        if (application == null || !TYPE_CODE_LEAVE.equals(application.getTypeCode())) {
            throw new BizException(ResultCode.NOT_FOUND, "请假申请不存在");
        }
        return application;
    }

    private void ensureReviewer(Long reviewerId) {
        Set<String> roles = userMapper.selectRoleCodesByUserId(reviewerId).stream().collect(Collectors.toSet());
        if (!(roles.contains("counselor") || roles.contains("admin"))) {
            throw new BizException(ResultCode.FORBIDDEN, "仅辅导员或管理员可审批");
        }
    }

    private void insertRecord(Long applicationId, Long operatorId, String action, String comment) {
        ApprovalRecord record = new ApprovalRecord();
        record.setApplicationId(applicationId);
        record.setApproverId(operatorId);
        record.setAction(action);
        record.setComment(comment);
        record.setCreatedAt(LocalDateTime.now());
        approvalRecordMapper.insert(record);
    }

    private LeaveApplicationListItemVO toListItem(LeaveApplication application) {
        LeaveApplicationListItemVO vo = new LeaveApplicationListItemVO();
        vo.setId(application.getId());
        vo.setUserId(application.getUserId());
        vo.setTitle(application.getTitle());
        vo.setReason(application.getReason());
        vo.setStatus(application.getStatus());
        vo.setStatusText(getStatusText(application.getStatus()));
        vo.setLeaveStartDate(application.getLeaveStartDate());
        vo.setLeaveEndDate(application.getLeaveEndDate());
        vo.setSubmitTime(application.getSubmitTime());
        User applicant = userMapper.selectById(application.getUserId());
        if (applicant != null) {
            vo.setApplicantName(applicant.getRealName());
            vo.setApplicantStudentNo(applicant.getStudentNo());
        }
        return vo;
    }

    private LeaveApplicationDetailVO toDetail(LeaveApplication application) {
        LeaveApplicationDetailVO detailVO = new LeaveApplicationDetailVO();
        detailVO.setId(application.getId());
        detailVO.setUserId(application.getUserId());
        detailVO.setTitle(application.getTitle());
        detailVO.setReason(application.getReason());
        detailVO.setStatus(application.getStatus());
        detailVO.setStatusText(getStatusText(application.getStatus()));
        detailVO.setLeaveStartDate(application.getLeaveStartDate());
        detailVO.setLeaveEndDate(application.getLeaveEndDate());
        detailVO.setContactPhone(application.getContactPhone());
        detailVO.setFileId(application.getFileId());
        detailVO.setRejectReason(application.getRejectReason());
        detailVO.setSubmitTime(application.getSubmitTime());
        detailVO.setApprovedAt(application.getApprovedAt());

        User applicant = userMapper.selectById(application.getUserId());
        if (applicant != null) {
            detailVO.setApplicantName(applicant.getRealName());
            detailVO.setApplicantStudentNo(applicant.getStudentNo());
        }
        if (application.getApprovedBy() != null) {
            User approver = userMapper.selectById(application.getApprovedBy());
            detailVO.setApprovedByName(approver == null ? "" : approver.getRealName());
        }
        detailVO.setTimelines(toTimelineList(application.getId()));
        return detailVO;
    }

    private List<LeaveTimelineVO> toTimelineList(Long applicationId) {
        return approvalRecordMapper.selectByApplicationId(applicationId).stream()
                .map(record -> {
                    LeaveTimelineVO timelineVO = new LeaveTimelineVO();
                    timelineVO.setAction(record.getAction());
                    timelineVO.setActionText(getActionText(record.getAction()));
                    timelineVO.setComment(record.getComment());
                    timelineVO.setCreatedAt(record.getCreatedAt());
                    User user = userMapper.selectById(record.getApproverId());
                    timelineVO.setOperatorName(user == null ? "未知用户" : user.getRealName());
                    return timelineVO;
                })
                .collect(Collectors.toList());
    }

    private String getStatusText(Integer status) {
        return switch (status == null ? -1 : status) {
            case 0 -> "待审批";
            case 1 -> "处理中";
            case 2 -> "已通过";
            case 3 -> "已驳回";
            case 4 -> "已撤回";
            default -> "未知状态";
        };
    }

    private String getActionText(String action) {
        return switch (action) {
            case "submit" -> "已提交";
            case "approve" -> "审批通过";
            case "reject" -> "审批驳回";
            case "withdraw" -> "申请撤回";
            default -> "流程更新";
        };
    }
}
