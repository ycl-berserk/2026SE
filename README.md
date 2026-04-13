# 2026SE

学院学生综合服务与党团管理平台课程项目仓库。

当前仓库包含两个主要部分：
- `miniprogram-1/`：微信小程序前端
- `demo/`：Spring Boot 后端演示实现

目前后端目标不是“完整生产版”，而是先保证本地可启动、主要接口可联调、演示流程可跑通。

## 目录说明

```text
2026SE/
├─ miniprogram-1/   微信小程序前端
├─ demo/            Spring Boot 后端 demo
├─ API.md           接口清单
├─ 后端脚手架.md     后端设计说明
└─ README.md
```

## 当前后端状态

`demo/` 已调整为可直接本地运行的演示版本，特点如下：
- 使用 Spring Boot 3
- 使用 MyBatis-Plus + Sa-Token
- `dev` 环境默认使用 H2 内存数据库
- 启动时自动执行 `schema.sql` 和 `data.sql`
- 不依赖本机预装 PostgreSQL / 金仓即可启动
- 已有最小演示数据，可直接验证接口

默认启动端口：
- `18080`

## 后端启动

环境要求：
- JDK 17
- Maven 3.9+

启动步骤：

```bash
cd demo
mvn spring-boot:run
```

如果启动成功，日志中会看到类似信息：

```text
Tomcat started on port 18080
```

如果 `18080` 被占用，可以临时换端口：

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=18081
```

## 后端快速验证

### 1. 登录

请求：

```http
POST /api/auth/wx-login
Content-Type: application/json

{"code":"demo"}
```

本地地址示例：

```bash
curl -X POST "http://127.0.0.1:18080/api/auth/wx-login" ^
  -H "Content-Type: application/json" ^
  -d "{\"code\":\"demo\"}"
```

预期：
- 返回 `code: 0`
- 返回 `data.token`
- 返回 `data.needBind: false`

### 2. 携带 token 调用受保护接口

登录成功后，将返回的 token 放入请求头：

```http
Authorization: <token>
```

建议优先验证这些接口：

- `GET /api/auth/me`
- `GET /api/home`
- `GET /api/student/profile`
- `GET /api/knowledge/articles`
- `GET /api/knowledge/articles/1`
- `GET /api/knowledge/templates`
- `GET /api/messages/recent`
- `GET /api/messages/unread-count`
- `GET /api/party/me/overview`
- `GET /api/party/me/records`
- `GET /api/party/me/reminders`

### 3. 验证更新接口

例如更新学生档案：

```http
PUT /api/student/profile
Authorization: <token>
Content-Type: application/json

{
  "gender": 1,
  "bio": "这是一次测试修改",
  "hometown": "北京",
  "dormitory": "1-101"
}
```

然后再次调用：

```http
GET /api/student/profile
```

确认返回数据发生变化。

## H2 控制台

开发环境启用了 H2 控制台，可用于查看演示数据。

访问地址：
- `http://127.0.0.1:18080/h2-console`

连接参数：
- JDBC URL: `jdbc:h2:mem:ruc_platform`
- User Name: `sa`
- Password: 留空

可直接执行：

```sql
select * from t_user;
select * from t_wx_bind;
select * from student_profile;
select * from knowledge_article;
select * from party_student_progress;
```

## 微信小程序前端说明

### 配置步骤

1. 复制模板配置文件：

```bash
cp miniprogram-1/project.config.template.json miniprogram-1/project.config.json
```

2. 将 `project.config.json` 中的 `YOUR_APP_ID_HERE` 替换为你自己的小程序 AppID。

3. 使用微信开发者工具打开 `miniprogram-1/` 目录。

说明：
- `miniprogram-1/project.config.json` 已加入 `.gitignore`
- 不会提交到仓库

## 文档位置

- [API.md](API.md)：接口清单
- [后端脚手架.md](后端脚手架.md)：后端结构和实现思路

## 当前限制

当前版本是“演示可运行版”，不是正式生产版，主要限制如下：
- `dev` 环境使用 H2 内存数据库，服务重启后数据会重置
- 微信登录仍是 mock 逻辑，`code=demo` 用于本地测试
- 尚未完成真实金仓 / PostgreSQL 持久化环境切换
- 部分接口是最小实现，优先保证能启动和能联调

## 建议的后续工作

建议后续按下面顺序推进：

1. 将当前可运行版本提交并推送到远程仓库
2. 让小程序前端对接当前后端地址和 token 逻辑
3. 整理一份接口测试清单或 Apifox/Postman 集合
4. 再逐步切回真实数据库和真实微信登录
