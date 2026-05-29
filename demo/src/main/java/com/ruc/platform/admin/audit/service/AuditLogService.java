package com.ruc.platform.admin.audit.service;

import com.ruc.platform.admin.audit.dto.AuditLogQueryDTO;
import com.ruc.platform.admin.audit.vo.AuditLogVO;
import com.ruc.platform.common.api.PageResult;
import jakarta.servlet.http.HttpServletRequest;

public interface AuditLogService {
    PageResult<AuditLogVO> list(AuditLogQueryDTO query);

    void record(Long userId,
                String module,
                String action,
                String description,
                HttpServletRequest request,
                Long executionTime,
                boolean success,
                String errorMessage);
}
