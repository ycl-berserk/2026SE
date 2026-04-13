package com.ruc.platform.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class SaTokenConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
                    if (StpUtil.isLogin()) {
                        return;
                    }

                    if (isDevProfile()) {
                        StpUtil.login(1001L);
                        return;
                    }

                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/wx-login");
    }

    private boolean isDevProfile() {
        for (String profile : environment.getActiveProfiles()) {
            if ("dev".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
