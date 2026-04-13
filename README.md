# 2026SE

Course project repository for the student service and party-affairs platform.

This repository currently contains:
- `miniprogram-1/`: WeChat Mini Program frontend
- `demo/`: Spring Boot backend demo

The current goal is not a production-ready release. The goal is a runnable local demo where the frontend can call the backend and display real data.

## Structure

```text
2026SE/
├─ miniprogram-1/   Mini Program frontend
├─ demo/            Spring Boot backend demo
├─ API.md           API list
├─ 后端脚手架.md     Backend design notes
└─ README.md
```

## Backend Demo

The backend in `demo/` is set up for local demo usage.

Key points:
- Spring Boot 3
- MyBatis-Plus
- Sa-Token
- H2 in-memory database in `dev`
- auto-load `schema.sql` and `data.sql` on startup
- no external PostgreSQL / Kingbase required for the demo

Default port:
- `18080`

## Run The Backend

Requirements:
- JDK 17
- Maven 3.9+

Commands:

```bash
cd demo
mvn spring-boot:run
```

Expected log:

```text
Tomcat started on port 18080
```

If port `18080` is occupied:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=18081
```

## Backend Quick Check

Login request:

```http
POST /api/auth/wx-login
Content-Type: application/json

{"code":"demo"}
```

Useful endpoints:
- `GET /api/home`
- `GET /api/student/profile`
- `GET /api/knowledge/articles`
- `GET /api/knowledge/articles/{id}`
- `GET /api/knowledge/templates`
- `GET /api/party/me/overview`
- `GET /api/party/me/records`
- `GET /api/party/me/reminders`

## H2 Console

The `dev` profile enables H2 console.

URL:
- `http://127.0.0.1:18080/h2-console`

Connection:
- JDBC URL: `jdbc:h2:mem:ruc_platform`
- User: `sa`
- Password: empty

Useful queries:

```sql
select * from t_user;
select * from student_profile;
select * from knowledge_article;
select * from knowledge_template;
select * from party_student_progress;
select * from party_student_record;
select * from party_reminder;
```

## Mini Program Setup

Project directory for WeChat DevTools:
- `miniprogram-1/`

Do not open:
- repo root
- `demo/`

Create local Mini Program config from template:

```bash
cp miniprogram-1/project.config.template.json miniprogram-1/project.config.json
```

Then replace `YOUR_APP_ID_HERE` with your own AppID if needed.

In WeChat DevTools:
1. Import `miniprogram-1/`
2. Disable domain / TLS / HTTPS certificate checks for local development
3. Compile after backend startup

Current frontend integration:
- home page uses backend data
- knowledge list uses backend data
- knowledge detail uses backend data
- party progress page uses backend data
- fallback mock data is kept only for request failure cases

## Local Integration Notes

Current frontend base URL:
- `http://127.0.0.1:18080`

File:
- [miniprogram-1/utils/config.js](miniprogram-1/utils/config.js)

Important:
- `127.0.0.1` works for local DevTools simulation
- it will not work on a real phone
- for real-device testing, replace it with your computer LAN IP

## Current Limits

This is still a demo build.

Known limits:
- H2 data resets after backend restart
- login is still demo-oriented
- `dev` backend currently allows demo fallback login for easier frontend integration
- some modules are still placeholders
- template download is not implemented

## Suggested Next Steps

1. Commit and push the current runnable version
2. Add more frontend pages for the already available backend APIs
3. Replace demo login with a stricter real token flow
4. Move from H2 demo mode to a persistent database setup
