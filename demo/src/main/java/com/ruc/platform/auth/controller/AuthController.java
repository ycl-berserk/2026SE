package com.ruc.platform.auth.controller;

import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.service.AuthService;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;
import com.ruc.platform.common.api.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理微信登录、用户信息查询、退出登录等认证相关请求
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 微信登录接口
     * 小程序端调用，换取平台token
     * 
     * @param wxLoginDTO 微信登录参数
     * @return 登录结果（token、用户信息、是否需要绑定）
     */
    @PostMapping("/wx-login")
    public Result<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO wxLoginDTO) {
        log.info("收到微信登录请求");
        LoginVO loginVO = authService.wxLogin(wxLoginDTO);
        return Result.ok(loginVO);
    }

    /**
     * 获取当前登录用户信息
     * 需要登录态，从token中解析用户ID
     * 
     * @return 用户信息
     */
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        log.info("获取当前用户信息");
        UserVO userVO = authService.getCurrentUser();
        return Result.ok(userVO);
    }

    /**
     * 退出登录
     * 清除当前用户的token
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        log.info("退出登录");
        authService.logout();
        return Result.ok();
    }
}
