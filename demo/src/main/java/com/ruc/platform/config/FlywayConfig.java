package com.ruc.platform.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Flyway 配置类
 * 人大金仓兼容PostgreSQL协议，需要自定义配置
 */
@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 自定义Flyway迁移策略
     * 强制使用PostgreSQL方言适配人大金仓
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // 使用默认的Flyway执行迁移
            // 由于我们使用PostgreSQL驱动连接人大金仓，Flyway会自动识别为PostgreSQL
            flyway.migrate();
        };
    }
}
