package com.ruc.platform.auth.controller;

import com.ruc.platform.auth.dto.AccountLoginDTO;
import com.ruc.platform.auth.dto.AccountRegisterDTO;
import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.service.AuthService;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;
import com.ruc.platform.common.api.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody AccountRegisterDTO registerDTO) {
        log.info("收到账号注册请求，studentNo: {}", registerDTO.getStudentNo());
        return Result.ok(authService.register(registerDTO));
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody AccountLoginDTO loginDTO) {
        log.info("收到账号登录请求，studentNo: {}", loginDTO.getStudentNo());
        return Result.ok(authService.login(loginDTO));
    }

    @PostMapping("/wx-login")
    public Result<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO wxLoginDTO) {
        return Result.ok(authService.wxLogin(wxLoginDTO));
    }

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        return Result.ok(authService.getCurrentUser());
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.ok();
    }
}
