# Docker 部署说明（开发与验收）

## 1. 目标

本说明用于统一团队本地运行环境，减少“同一份代码在不同同学电脑上运行结果不一致”的问题。

当前提供两套编排：

- `docker-compose.postgres.yml`：可直接运行，适合日常开发
- `docker-compose.kingbase.example.yml`：Kingbase 示例模板，需替换镜像后使用

## 2. 前置条件

- 已安装 Docker Desktop（Windows/macOS）或 Docker Engine（Linux）
- 可执行 `docker compose version`

## 3. PostgreSQL 开发环境（推荐）

在项目根目录执行：

```bash
docker compose -f docker-compose.postgres.yml up -d --build
```

启动后：

- 后端地址：`http://localhost:18080`
- 数据库地址：`localhost:5432`
- 数据库名：`ruc_platform`
- 用户名：`postgres`
- 密码：`postgres`

查看日志：

```bash
docker compose -f docker-compose.postgres.yml logs -f app
```

停止服务：

```bash
docker compose -f docker-compose.postgres.yml down
```

## 4. Kingbase 验收环境（按课程要求）

仓库提供：`docker-compose.kingbase.example.yml`

使用前需要：

1. 替换 `db-kingbase.image` 为你们可用的 Kingbase 镜像地址
2. 根据镜像文档调整环境变量和数据目录
3. 确认容器内数据库监听端口（示例为 `54321`）

启动命令：

```bash
docker compose -f docker-compose.kingbase.example.yml up -d --build
```

## 5. 应用配置说明

后端新增两个 profile：

- `docker-postgres` -> `demo/src/main/resources/application-docker-postgres.yml`
- `docker-kingbase` -> `demo/src/main/resources/application-docker-kingbase.yml`

由 Compose 注入：

- `SPRING_PROFILES_ACTIVE`（已配置为 `docker-postgres` 或 `docker-kingbase`，并在代码中纳入开发环境自动登录白名单）
- `DB_HOST` / `DB_PORT` / `DB_NAME` / `DB_USER` / `DB_PASSWORD`

Flyway 会在应用启动时自动执行迁移脚本。

## 6. 常见问题

### 6.1 18080 / 5432 端口被占用

- 修改 compose 文件中的主机端口映射（左侧端口）

### 6.2 `app` 启动失败，提示连接数据库超时

- 先执行 `docker compose ... logs -f db-postgres` 检查数据库是否正常启动
- 再执行 `docker compose ... logs -f app` 查看应用报错

### 6.3 Flyway 校验失败

- 若为历史脏数据导致，可在开发环境删除卷后重建：

```bash
docker compose -f docker-compose.postgres.yml down -v
docker compose -f docker-compose.postgres.yml up -d --build
```
