package com.ruc.platform.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应状态码枚举
 * 定义业务状态码规范：
 * - 0: 成功
 * - 400xx: 业务错误
 * - 401xx: 鉴权错误
 * - 403xx: 权限错误
 * - 500xx: 系统错误
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 参数错误
     */
    PARAM_ERROR(40000, "参数错误"),

    /**
     * 业务错误
     */
    BIZ_ERROR(40001, "业务错误"),

    /**
     * 数据不存在
     */
    NOT_FOUND(40004, "数据不存在"),

    /**
     * 未登录
     */
    UNAUTHORIZED(40100, "未登录"),

    /**
     * Token无效或已过期
     */
    TOKEN_INVALID(40101, "Token无效或已过期"),

    /**
     * 权限不足
     */
    FORBIDDEN(40300, "权限不足"),

    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(50000, "系统内部错误");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 提示信息
     */
    private final String message;
}
