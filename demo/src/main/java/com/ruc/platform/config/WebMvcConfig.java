package com.ruc.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 配置静态资源访问和文件下载
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源处理器
     * 映射文件上传目录到静态访问路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置文件上传目录的静态访问
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/ruc-platform/uploads/");
    }
}
