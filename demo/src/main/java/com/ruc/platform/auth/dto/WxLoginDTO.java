package com.ruc.platform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WxLoginDTO {

    /**
     * 微信小程序登录code
     */
    @NotBlank(message = "登录code不能为空")
    private String code;
}
