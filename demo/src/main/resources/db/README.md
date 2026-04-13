# 数据库迁移脚本说明

## ✅ 测试状态

**所有 8 个迁移脚本已成功在人大金仓数据库（PostgreSQL 协议）上执行！**

测试时间：2026-04-13  
数据库版本：人大金仓 (PostgreSQL 12.1 兼容)  
Flyway 版本：9.22.3  

---

## 📋 迁移脚本列表

| 版本号 | 文件名 | 描述 | 状态 |
|--------|--------|------|------|
| V1 | `V1__create_auth_tables.sql` | 创建认证与用户管理相关表 | ✅ 成功 |
| V2 | `V2__create_student_profile.sql` | 创建学生档案与基础信息表 | ✅ 成功 |
| V3 | `V3__create_knowledge_tables.sql` | 创建知识库与资料管理相关表 | ✅ 成功 |
| V4 | `V4__create_party_progress_tables.sql` | 创建党团流程与进度管理相关表 | ✅ 成功 |
| V5 | `V5__create_notice_message_tables.sql` | 创建通知与消息管理相关表 | ✅ 成功 |
| V6 | `V6__create_file_and_approval_tables.sql` | 创建文件管理与审批流程相关表 | ✅ 成功 |
| V7 | `V7__create_honor_and_audit_tables.sql` | 创建荣誉展示与审计日志相关表 | ✅ 成功 |
| V8 | `V8__seed_initial_data.sql` | 插入初始基础数据 | ✅ 成功 |

---

## 🗄️ 创建的数据库表

### V1 - 认证与用户管理（4 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `t_user` | 平台用户表 | 8 |
| `t_role` | 角色表 | 6 |
| `t_user_role` | 用户角色关联表 | 4 |
| `t_wx_bind` | 微信绑定表 | 7 |

### V2 - 学生档案（1 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `student_profile` | 学生档案表 | 14 |

### V3 - 知识库（4 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `knowledge_category` | 知识分类表 | 6 |
| `knowledge_article` | 知识条目表 | 12 |
| `knowledge_keyword` | 关键词表 | 4 |
| `knowledge_template` | 模板文件表 | 9 |

### V4 - 党团流程（5 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `party_stage_def` | 阶段定义表 | 7 |
| `party_student_progress` | 学生党团进度表 | 4 |
| `party_student_record` | 阶段记录表 | 9 |
| `party_reminder` | 提醒事项表 | 8 |
| `party_report` | 思想汇报表 | 12 |

### V5 - 通知消息（2 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `notice` | 通知公告表 | 12 |
| `user_message` | 用户消息表 | 7 |

### V6 - 文件与审批（4 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `file_metadata` | 文件元数据表 | 10 |
| `certificate_type` | 证明/申请类型表 | 8 |
| `certificate_application` | 证明/申请表 | 14 |
| `approval_record` | 审批记录表 | 5 |

### V7 - 荣誉与审计（3 张表）

| 表名 | 描述 | 字段数 |
|------|------|--------|
| `honor_category` | 荣誉类别表 | 6 |
| `honor_record` | 荣誉记录表 | 14 |
| `audit_log` | 审计日志表 | 13 |

### V8 - 初始数据

插入的基础数据：
- ✅ 3 个系统角色（student、counselor、admin）
- ✅ 5 个党团阶段定义
- ✅ 4 个知识分类
- ✅ 3 个证明/申请类型
- ✅ 4 个荣誉类别
- ✅ 3 个测试用户及学生档案
- ✅ 3 个测试用户的党团进度

---

## 📊 总计

- **迁移脚本数量**：8 个
- **创建的表数量**：23 张
- **插入的基础数据**：约 30 条记录
- **创建的索引**：30+ 个
- **执行时间**：0.223 秒（全部 8 个脚本）

---

## ⚠️ 重要说明

### 1. 表名命名规范

由于人大金仓数据库已有 `sys_user` 和 `sys_role` 等系统表，所有业务表采用以下命名规范：

- 核心业务表：`t_` 前缀（如 `t_user`、`t_role`）
- 业务模块表：使用业务名称（如 `student_profile`、`knowledge_article`）

### 2. 主键策略

所有表使用 **BIGINT 主键**，由应用层生成（雪花 ID），不使用数据库自增。

### 3. 外键约束

所有外键约束都已创建，确保数据完整性。

### 4. 索引优化

为常用查询字段创建了索引，包括：
- 外键字段
- 状态字段
- 时间字段（降序）
- 分类/类型字段

### 5. 注释完整

所有表和字段都添加了中文注释，方便后续维护。

---

## 🔧 开发工具

### 数据库清理工具

位置：`src/main/java/com/example/demo/DatabaseCleaner.java`

用途：开发环境快速重置数据库

使用方法：
```bash
mvnw.cmd compile exec:java -Dexec.mainClass="com.example.demo.DatabaseCleaner"
```

⚠️ **警告**：此工具会删除整个 public schema，仅限开发环境使用！

---

## 🚀 Flyway 配置

`application.properties` 中的配置：

```properties
# Flyway 配置（人大金仓 - 使用 PostgreSQL 协议兼容模式）
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=0
spring.flyway.baseline-description=Initial baseline for KingbaseES (PostgreSQL compatible)
spring.flyway.encoding=UTF-8
spring.flyway.clean-disabled=true
spring.flyway.validate-on-migrate=true
spring.flyway.placeholder-replacement=false
```

---

## 📝 后续开发建议

1. **第二阶段**：开始编写后端业务代码（Controller、Service、Mapper）
2. **API 对接**：逐步实现 `API.md` 中定义的接口
3. **数据验证**：为关键接口添加参数校验
4. **单元测试**：为核心业务逻辑编写测试
5. **性能优化**：根据实际查询情况调整索引策略

---

## ✅ 测试日志摘要

```
Flyway Community Edition 9.22.3 by Redgate
Database: jdbc:postgresql://localhost:54321/test (PostgreSQL 12.1)
Successfully validated 8 migrations (execution time 00:00.016s)
Migrating schema "public" to version "1 - create auth tables"
Migrating schema "public" to version "2 - create student profile"
Migrating schema "public" to version "3 - create knowledge tables"
Migrating schema "public" to version "4 - create party progress tables"
Migrating schema "public" to version "5 - create notice message tables"
Migrating schema "public" to version "6 - create file and approval tables"
Migrating schema "public" to version "7 - create honor and audit tables"
Migrating schema "public" to version "8 - seed initial data"
Successfully applied 8 migrations to schema "public", now at version v8 (execution time 00:00.223s)
Started DemoApplication in 5.371 seconds
```

---

**结论：Flyway 在人大金仓数据库（PostgreSQL 协议）下完全正常工作！所有迁移脚本执行成功！**
