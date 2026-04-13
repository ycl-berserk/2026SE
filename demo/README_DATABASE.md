# 数据库配置说明

## 概述

本项目支持多种数据库，通过 Spring Profile 进行切换。默认使用 **H2 内存库**，便于本地 demo 最小化启动。

## 支持的数据库

| 数据库 | Profile | 用途 | 是否需要额外安装 |
|--------|---------|------|------------------|
| H2 内存库 | `h2` | 本地 demo、快速原型验证 | ❌ 否 |
| PostgreSQL | `postgres` | 开发环境、生产环境 | ✅ 是 |
| Kingbase 人大金仓 | `kingbase` | 国产化部署环境 | ✅ 是 |

## 快速开始

### 方式一：使用 H2 内存库（推荐，零配置启动）

```bash
# 直接启动，无需任何数据库配置
cd demo
./mvnw spring-boot:run

# 或使用 IDE 直接运行 PlatformApplication.java
```

访问 H2 控制台：http://localhost:18080/h2-console
- JDBC URL: `jdbc:h2:mem:ruc_platform`
- 用户名: `sa`
- 密码: (留空)

### 方式二：使用 PostgreSQL

#### 1. 安装 PostgreSQL

```bash
# macOS
brew install postgresql@15
brew services start postgresql@15

# Ubuntu
sudo apt install postgresql postgresql-client

# Windows
# 下载安装: https://www.postgresql.org/download/windows/
```

#### 2. 创建数据库

```bash
# 切换到 postgres 用户
sudo -i -u postgres

# 进入 PostgreSQL 命令行
psql

# 创建数据库和用户
CREATE DATABASE ruc_platform;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ruc_platform TO postgres;
\q
```

#### 3. 启动应用

```bash
# 通过 Maven 启动，指定 profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres

# 或在 application.yml 中修改 spring.profiles.active=postgres
```

### 方式三：使用 Kingbase 人大金仓

#### 1. 安装 Kingbase 数据库

按照人大金仓官方文档安装并启动数据库服务。

#### 2. 配置连接信息

编辑 `application-kingbase.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-host:54321/your-database
    username: your-username
    password: your-password
```

> **注意**：Kingbase 兼容 PostgreSQL 协议，因此使用 PostgreSQL 驱动连接。

#### 3. 启动应用

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=kingbase
```

## 配置文件说明

### 配置文件结构

```
demo/src/main/resources/
├── application.yml           # 主配置文件（通用配置）
├── application-dev.yml       # 开发环境通用配置（日志等）
├── application-h2.yml        # H2 内存库配置
├── application-postgres.yml  # PostgreSQL 配置
└── application-kingbase.yml  # Kingbase 人大金仓配置
```

### 切换数据库

修改 `application.yml` 中的 `spring.profiles.active`：

```yaml
spring:
  profiles:
    active: h2        # 默认使用 H2
    # active: postgres  # 切换到 PostgreSQL
    # active: kingbase  # 切换到 Kingbase
```

或在启动时通过命令行参数指定：

```bash
java -jar ruc-service-platform.jar --spring.profiles.active=postgres
```

## Flyway 数据库迁移

项目使用 Flyway 进行数据库版本管理，迁移脚本位于：

```
demo/src/main/resources/db/migration/
├── V1__create_auth_tables.sql
├── V2__create_student_profile.sql
├── V3__create_knowledge_tables.sql
├── V4__create_party_progress_tables.sql
├── V5__create_notice_message_tables.sql
├── V6__create_file_and_approval_tables.sql
├── V7__create_honor_and_audit_tables.sql
└── V8__seed_initial_data.sql
```

应用启动时会自动执行未运行的迁移脚本。

## 常见问题

### Q1: 启动时报数据库连接错误

**A**: 检查当前激活的 profile 是否正确：
- H2: `spring.profiles.active=h2`
- PostgreSQL: `spring.profiles.active=postgres`
- Kingbase: `spring.profiles.active=kingbase`

### Q2: H2 控制台无法访问

**A**: 确保使用的是 `h2` profile，H2 控制台仅在 H2 模式下启用。

### Q3: PostgreSQL/Kingbase 迁移失败

**A**: 
1. 检查数据库服务是否正常运行
2. 检查连接信息（URL、用户名、密码）是否正确
3. 查看日志中的详细错误信息

### Q4: 如何在开发和生产环境使用不同数据库？

**A**: 使用 Spring Profile 机制：
- 开发环境：`--spring.profiles.active=dev,h2` 或 `dev,postgres`
- 生产环境：`--spring.profiles.active=prod,kingbase`

## 技术栈

- **Spring Boot**: 3.2.5
- **MyBatis-Plus**: 3.5.5
- **Flyway**: 数据库版本管理
- **H2**: 内存数据库（开发测试）
- **PostgreSQL**: 关系型数据库（生产环境）
- **Kingbase**: 人大金仓（国产化适配）

## 数据库驱动说明

### 当前使用的驱动

项目默认使用 **PostgreSQL 官方驱动** 连接 PostgreSQL 和 Kingbase 数据库（Kingbase 兼容 PostgreSQL 协议）。

### Kingbase 官方驱动（可选）

如果需要使用 Kingbase 官方 JDBC 驱动：

1. 将 `kingbase8-8.6.0.jar` 放入 `demo/lib/` 目录
2. 在 `pom.xml` 中取消注释 Kingbase 依赖
3. 修改 `application-kingbase.yml` 中的 `driver-class-name` 为 `com.kingbase8.Driver`

## 联系与支持

如有数据库相关问题，请查阅：
- [Spring Boot 官方文档 - Data](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html)
- [MyBatis-Plus 文档](https://baomidou.com/)
- [Flyway 文档](https://flywaydb.org/documentation/)
- [PostgreSQL 文档](https://www.postgresql.org/docs/)
- [Kingbase 人大金仓文档](https://www.kingbase.com.cn/)
