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
        // 开发、测试环境（h2、postgres、kingbase、dev）都启用自动登录
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
                    if ("dev".equalsIgnoreCase(profile)
                        || "h2".equalsIgnoreCase(profile)
                        || "postgres".equalsIgnoreCase(profile)
                        || "kingbase".equalsIgnoreCase(profile)
                        || "docker-postgres".equalsIgnoreCase(profile)
                        || "docker-kingbase".equalsIgnoreCase(profile)
                        || "test".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        // 如果没有显式指定 profile，也默认启用自动登录（本地开发）
        return activeProfiles.length == 0;
    }
}
