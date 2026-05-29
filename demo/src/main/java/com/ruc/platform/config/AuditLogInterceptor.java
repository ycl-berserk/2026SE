package com.ruc.platform.config;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.admin.audit.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuditLogInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "auditStartTime";

    private final AuditLogService auditLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (shouldRecord(request)) {
            request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!shouldRecord(request)) {
            return;
        }
        long executionTime = executionTime(request);
        boolean success = ex == null && response.getStatus() < 400;
        auditLogService.record(
                currentUserId(),
                module(request.getRequestURI()),
                action(request.getMethod(), request.getRequestURI()),
                description(request.getMethod(), request.getRequestURI()),
                request,
                executionTime,
                success,
                ex == null ? null : ex.getMessage()
        );
    }

    private boolean shouldRecord(HttpServletRequest request) {
        String method = request.getMethod();
        if (!"POST".equalsIgnoreCase(method) && !"PUT".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
            return false;
        }
        String path = request.getRequestURI();
        return path.startsWith("/api/admin/")
                && !path.startsWith("/api/admin/audit-logs");
    }

    private Long currentUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception ignored) {
            return null;
        }
    }

    private long executionTime(HttpServletRequest request) {
        Object start = request.getAttribute(START_TIME_ATTR);
        if (start instanceof Long startTime) {
            return System.currentTimeMillis() - startTime;
        }
        return 0L;
    }

    private String module(String path) {
        if (path.startsWith("/api/admin/counselors") || path.startsWith("/api/admin/roles")) {
            return "user";
        }
        if (path.startsWith("/api/admin/knowledge")) {
            return "knowledge";
        }
        if (path.startsWith("/api/admin/party")) {
            return "party";
        }
        if (path.startsWith("/api/admin/notices") || path.startsWith("/api/admin/notice")) {
            return "notice";
        }
        return "admin";
    }

    private String action(String method, String path) {
        if (path.contains("/approve") || path.contains("/review") || path.contains("/status") || path.contains("/activate")) {
            return "update";
        }
        if ("POST".equalsIgnoreCase(method)) {
            return "create";
        }
        if ("PUT".equalsIgnoreCase(method)) {
            return "update";
        }
        if ("DELETE".equalsIgnoreCase(method)) {
            return "delete";
        }
        return method.toLowerCase();
    }

    private String description(String method, String path) {
        return "管理端操作：" + method.toUpperCase() + " " + path;
    }
}
