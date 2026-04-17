# 学院学生综合服务与党团管理平台

本项目是 2026SE 课程项目，包含学院学生服务平台与党团管理平台的前后端代码。

当前目标：**在本地跑通前后端联调的 Demo，前端能调用后端接口并展示真实数据。**

## 项目结构

```text
2026SE/
├─ miniprogram-1/          微信小程序前端
├─ demo/                   Spring Boot 后端 Demo
├─ API.md                  接口文档
├─ 后端脚手架.md             后端设计笔记
└─ README.md               本文件
```

---

## 一、本地运行指南（组员必读）

### 环境要求

| 软件 | 最低版本 | 说明 |
|------|----------|------|
| JDK | 17+ | 推荐使用 Adoptium / Temurin |
| Maven | 3.9+ | 或使用项目自带的 `mvnw` 包装器 |
| 微信开发者工具 | 最新版 | 用于运行小程序前端 |

> **提示**：如果未安装 Maven，项目根目录下有 `mvnw`（Windows）和 `mvnw`（Linux/macOS）包装器，可直接使用。

### 1. 启动后端服务（三选一）

项目支持三种数据库模式，**推荐组员使用 H2 模式，零配置即可运行**。

#### 方式一：H2 内存库（推荐，零配置）

```bash
cd demo
mvn spring-boot:run
```

或直接双击：

```bash
cd demo
.\mvnw spring-boot:run
```

#### 方式二：PostgreSQL

```bash
cd demo
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

#### 方式三：人大金仓 Kingbase

```bash
cd demo
mvn spring-boot:run -Dspring-boot.run.profiles=kingbase
```

> **Windows PowerShell 用户注意**：如果命令报错，请用引号包裹参数：
> ```bash
> mvn spring-boot:run "-Dspring-boot.run.profiles=kingbase"
> ```

### 2. 如何确认后端启动成功

看到以下日志即为成功：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

INFO --- Starting PlatformApplication ...
INFO --- The following 1 profile is active: "h2"    <-- 显示当前激活的 profile
INFO --- Tomcat started on port 18080 (http) ...
INFO --- Started PlatformApplication in X.XXX seconds ...
```

**关键检查点**：
- ✅ `The following 1 profile is active: "h2"` — 显示当前使用的数据库模式
- ✅ `Tomcat started on port 18080` — 服务已启动，默认端口 18080
- ✅ 无红色错误信息

### 3. 启动小程序前端

1. 打开 **微信开发者工具**
2. 导入项目：选择 `miniprogram-1/` 目录（**不要导入项目根目录**）
3. 在开发者工具中：
   - 点击「详情」→「本地设置」
   - ✅ 勾选「不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书」
4. 确保后端已启动后，点击「编译」即可看到效果

### 4. 如何确认前后端联调成功

启动小程序后，如果看到以下内容，说明**前后端联调成功**：

| 页面 | 应显示的内容 |
|------|-------------|
| 首页 | 显示「张同学」的待办事项、通知等聚合数据 |
| 知识库 | 能看到党团流程、学籍事务等分类和文章列表 |
| 党团进度 | 显示当前用户的党团阶段（积极分子/发展对象等） |
| 个人中心 | 显示用户信息：张同学（2023001） |

**如果页面显示空数据或报错**：
1. 检查后端是否正常启动（查看终端日志）
2. 检查小程序是否配置了正确的后端地址：`http://127.0.0.1:18080`
3. 确认开发者工具中已关闭域名校验

---

## 二、后端数据库配置说明

项目支持多种数据库，通过 Spring Profile 进行切换：

| Profile | 数据库 | 用途 | 是否需要额外安装 |
|---------|--------|------|------------------|
| `h2`（默认） | H2 内存库 | 本地演示、快速原型验证 | ❌ 否 |
| `postgres` | PostgreSQL | 开发环境、生产环境 | ✅ 是 |
| `kingbase` | 人大金仓 | 国产化部署环境 | ✅ 是 |

### H2 控制台（仅 H2 模式可用）

启动 H2 模式后，可以通过浏览器访问 H2 数据库控制台：

- 地址：`http://127.0.0.1:18080/h2-console`
- JDBC URL：`jdbc:h2:mem:ruc_platform`
- 用户名：`sa`
- 密码：（留空）

### 如何切换数据库

修改 `demo/src/main/resources/application.yml` 中的配置：

```yaml
spring:
  profiles:
    active: h2        # 默认使用 H2
    # active: postgres  # 切换到 PostgreSQL
    # active: kingbase  # 切换到 Kingbase
```

详细数据库配置说明请参考 `demo/README_DATABASE.md`。

---

## 三、常用接口测试

### 模拟登录（开发环境自动登录）

前端使用 `code=demo` 调用登录接口时，后端会**自动登录为测试用户「张同学」**：

```http
POST http://127.0.0.1:18080/api/auth/wx-login
Content-Type: application/json

{"code": "demo"}
```

返回示例：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "token": "xxxxx-xxxxx-xxxxx",
    "needBind": false,
    "user": {
      "id": 1001,
      "realName": "张同学",
      "studentNo": "2023001",
      "roles": ["student"]
    }
  }
}
```

### 其他常用接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `GET /api/home` | GET | 首页聚合数据（需登录） |
| `GET /api/student/profile` | GET | 获取学生档案（需登录） |
| `GET /api/knowledge/articles` | GET | 知识库文章列表 |
| `GET /api/knowledge/articles/{id}` | GET | 知识库文章详情 |
| `GET /api/knowledge/templates` | GET | 知识模板列表 |
| `GET /api/party/me/overview` | GET | 党团进度概览（需登录） |
| `GET /api/party/me/records` | GET | 党团记录列表（需登录） |
| `GET /api/party/me/reminders` | GET | 党团提醒列表（需登录） |

---

## 四、测试用户数据

后端启动时会自动初始化测试数据，包含以下测试用户：

| 用户 ID | 学号 | 姓名 | 党团阶段 | 专业 |
|---------|------|------|----------|------|
| 1001 | 2023001 | 张同学 | 积极分子 | 计算机科学与技术 |
| 1002 | 2023002 | 李同学 | 入党申请人 | 软件工程 |
| 1003 | 2023003 | 王同学 | 发展对象 | 信息安全 |

开发环境使用 `code=demo` 登录时，**默认使用用户 1001（张同学）**。

---

## 五、常见问题

### Q1：启动时提示端口 18080 被占用

**解决方法**：

```bash
# 方式一：指定其他端口
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=18081

# 方式二：关闭占用 18080 端口的程序
netstat -ano | findstr :18080   # 找到 PID
taskkill /PID <PID> /F          # 强制关闭
```

### Q2：前端请求返回 401 未授权

**原因**：后端未正确登录。

**解决方法**：
1. 确保后端已正常启动
2. 检查前端 `miniprogram-1/utils/config.js` 中的 `BASE_URL` 是否为 `http://127.0.0.1:18080`
3. 确认使用的是 `code=demo` 进行登录

### Q3：数据库连接失败

**H2 模式**：不需要额外配置，直接启动即可。

**PostgreSQL/Kingbase 模式**：
1. 确保对应数据库服务已启动
2. 检查连接信息（地址、端口、用户名、密码）是否正确
3. 查看 `demo/src/main/resources/application-postgres.yml` 或 `application-kingbase.yml`

### Q4：小程序页面显示空数据

1. 检查后端是否正常启动（查看终端日志）
2. 检查开发者工具中是否关闭了域名校验
3. 打开小程序控制台，查看网络请求是否成功

---

## 六、当前限制

本项目为演示版本，存在以下限制：

- H2 模式下，**重启后端服务后数据会重置**
- 登录流程为开发环境简化版，使用 `code=demo` 自动登录
- 部分模块仍为占位符，功能未完全实现
- 模板下载功能未实现
- 小程序使用 `127.0.0.1` 地址，**仅限开发者工具模拟测试**，真机需替换为局域网 IP

## 七、后续计划

1. 提交并推送当前可运行版本
2. 添加更多前端页面，对接已有后端接口
3. 将演示登录替换为真实的 Token 认证流程
4. 从 H2 演示模式迁移到持久化数据库
5. 完善缺失的功能模块
