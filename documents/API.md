# API 清单（2026-04 当前版本）

> 说明：本项目采用双端分工。
> - 学生端：微信小程序
> - 辅导员/管理员端：Vue 网页（`web-counselor`）
> - 统一返回：`code = 0` 表示成功

## 1. 已实现并联调通过

### 1.1 认证与登录态

- `POST /api/auth/wx-login` 微信登录（开发环境支持 `code=demo`）
- `GET /api/auth/me` 获取当前用户信息
- `POST /api/auth/logout` 退出登录

### 1.2 首页聚合

- `GET /api/home` 首页聚合数据

### 1.3 知识库与模板

- `GET /api/knowledge/articles` 知识条目列表
- `GET /api/knowledge/articles/{id}` 知识条目详情
- `GET /api/knowledge/templates` 模板列表
- `GET /api/knowledge/templates/{id}/download` 模板下载

### 1.4 党团进度

- `GET /api/party/me/progress` 获取本人党团进度
- `GET /api/party/me/records` 获取本人党团记录
- `GET /api/party/me/reminders` 获取本人提醒

### 1.5 请假申请（学生侧）

- `POST /api/leave/me/applications` 创建请假申请
- `GET /api/leave/me/applications` 我的请假列表
- `GET /api/leave/me/applications/{id}` 我的请假详情

### 1.6 请假审批（辅导员/管理员侧）

- `GET /api/leave/reviewer/applications?status={status}` 审批列表（可按状态筛选）
- `GET /api/leave/reviewer/applications/{id}` 审批详情
- `POST /api/leave/reviewer/applications/{id}/approve` 审批通过
- `POST /api/leave/reviewer/applications/{id}/reject` 审批驳回

## 2. 请求约定

- 鉴权头：`Authorization`
- 内容类型：`application/json`
- 时间字段：ISO 或后端默认序列化格式（按实际返回为准）

## 3. 关键 DTO 示例

### 3.1 创建请假

`POST /api/leave/me/applications`

```json
{
  "title": "参加学术会议请假",
  "reason": "参加学院推荐的学术会议",
  "leaveStartDate": "2026-04-23",
  "leaveEndDate": "2026-04-24",
  "contactPhone": "13800138001",
  "fileId": null
}
```

### 3.2 审批动作

`POST /api/leave/reviewer/applications/{id}/approve`

```json
{
  "comment": "材料完整，审批通过"
}
```

`POST /api/leave/reviewer/applications/{id}/reject`

```json
{
  "comment": "请补充附件后重新提交"
}
```

## 4. 规划中接口（未全部落地）

以下为需求文档中的后续规划，当前版本未承诺全部实现：

- 学生基础信息完善与管理端学生列表/导入导出
- 管理端通知发布与精准推送
- 电子证明、盖章、重批等完整审批族接口
- 荣誉展示的管理端维护能力
- 后台审计日志、配置中心、字典维护
- 学业分析相关预留接口
