package com.ruc.platform.common.exception;

import com.ruc.platform.common.api.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 * 用于抛出业务级别异常，全局异常处理器会统一处理
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 异常状态码
     */
    private final Integer code;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BizException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
