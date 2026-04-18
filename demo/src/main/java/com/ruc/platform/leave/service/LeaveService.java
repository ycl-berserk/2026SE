package com.ruc.platform.leave.service;

import com.ruc.platform.leave.dto.LeaveCreateDTO;
import com.ruc.platform.leave.vo.LeaveApplicationDetailVO;
import com.ruc.platform.leave.vo.LeaveApplicationListItemVO;

import java.util.List;

/**
 * 请假服务
 */
public interface LeaveService {

    Long create(Long userId, LeaveCreateDTO createDTO);

    List<LeaveApplicationListItemVO> listMine(Long userId);

    LeaveApplicationDetailVO getMineDetail(Long userId, Long id);

    void approve(Long reviewerId, Long id, String comment);

    void reject(Long reviewerId, Long id, String comment);
}
