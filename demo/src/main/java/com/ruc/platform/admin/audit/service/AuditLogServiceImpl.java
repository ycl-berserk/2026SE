package com.ruc.platform.admin.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruc.platform.admin.audit.dto.AuditLogQueryDTO;
import com.ruc.platform.admin.audit.entity.AuditLog;
import com.ruc.platform.admin.audit.mapper.AuditLogMapper;
import com.ruc.platform.admin.audit.vo.AuditLogVO;
import com.ruc.platform.common.api.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private static final int MAX_AUDIT_LOG_ROWS = 1000;

    private final AuditLogMapper auditLogMapper;

    @Override
    public PageResult<AuditLogVO> list(AuditLogQueryDTO query) {
        IPage<?> page = new Page<>(query.getPageNum(), query.getPageSize());
        List<AuditLogVO> records = auditLogMapper.selectPage(page, query);
        Long total = auditLogMapper.count(query);
        return PageResult.of(total, query.getPageNum(), query.getPageSize(), records);
    }

    @Override
    public void record(Long userId,
                       String module,
                       String action,
                       String description,
                       HttpServletRequest request,
                       Long executionTime,
                       boolean success,
                       String errorMessage) {
        try {
            AuditLog logRecord = new AuditLog();
            logRecord.setUserId(userId);
            logRecord.setModule(limit(module, 64));
            logRecord.setAction(limit(action, 64));
            logRecord.setDescription(limit(description, 500));
            logRecord.setRequestMethod(request == null ? null : limit(request.getMethod(), 10));
            logRecord.setRequestUrl(request == null ? null : limit(request.getRequestURI(), 500));
            logRecord.setIpAddress(request == null ? null : limit(clientIp(request), 64));
            logRecord.setUserAgent(request == null ? null : limit(request.getHeader("User-Agent"), 500));
            logRecord.setExecutionTime(executionTime);
            logRecord.setStatus(success ? 1 : 0);
            logRecord.setErrorMessage(errorMessage);
            logRecord.setCreatedAt(LocalDateTime.now());
            auditLogMapper.insert(logRecord);
            auditLogMapper.deleteOlderThanLimit(MAX_AUDIT_LOG_ROWS);
        } catch (Exception e) {
            log.warn("审计日志写入失败，module: {}, action: {}", module, action, e);
        }
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
