  API 清单

  1. 认证与权限

  - POST /api/auth/wx-login 微信登录，换取平台 token、用户基础信息、角色列表
  - GET /api/auth/me 获取当前登录人信息、角色、权限点
  - POST /api/auth/logout 退出登录
  - GET /api/auth/permissions 获取当前用户菜单与权限点
  - POST /api/admin/users/{userId}/roles 分配角色
  - GET /api/admin/roles 角色列表
  - GET /api/admin/permissions 权限点列表

  2. 学生基础信息 / 画像

  - GET /api/student/profile 获取本人基础信息与公开画像
  - PUT /api/student/profile 更新本人可编辑信息
  - GET /api/admin/students 学生列表，支持年级/专业/班级筛选
  - GET /api/admin/students/{studentId} 学生详情
  - POST /api/admin/students/import 批量导入学生信息
  - GET /api/admin/students/export 批量导出学生信息
  - PUT /api/admin/students/{studentId} 管理员更新学生信息

  3. 政策知识库 / 智能问答

  - GET /api/knowledge/articles 知识条目列表，支持关键词、分类、状态
  - GET /api/knowledge/articles/{id} 知识条目详情
  - POST /api/knowledge/search 关键词检索
  - POST /api/knowledge/ask 智能问答，返回答案、引用依据、相关推荐
  - GET /api/knowledge/templates 常用模板/资料下载列表
  - GET /api/knowledge/templates/{id}/download 下载模板
  - POST /api/admin/knowledge/articles 新增知识条目
  - PUT /api/admin/knowledge/articles/{id} 编辑知识条目
  - POST /api/admin/knowledge/articles/{id}/publish 上下架知识条目
  - POST /api/admin/knowledge/documents/upload 上传政策文档并入库

  4. 党团事务流程

  - GET /api/party/me/progress 获取本人当前阶段、时间线、提醒事项
  - GET /api/party/me/records 获取本人党团过程记录
  - GET /api/party/me/reminders 获取本人节点提醒
  - POST /api/party/me/reports 提交思想汇报/阶段材料
  - GET /api/admin/party/students 党团对象列表，支持阶段筛选
  - GET /api/admin/party/students/{studentId} 某学生党团流程详情
  - PUT /api/admin/party/students/{studentId}/stage 修改阶段状态
  - POST /api/admin/party/stages 配置流程阶段
  - PUT /api/admin/party/stages/{id} 编辑流程阶段
  - POST /api/admin/party/reminders/send 批量发送流程提醒

  5. 通知与精准推送

  - GET /api/messages 本人消息列表
  - GET /api/messages/{id} 消息详情
  - POST /api/messages/{id}/read 标记已读
  - GET /api/admin/notices 通知列表
  - POST /api/admin/notices 新建通知
  - PUT /api/admin/notices/{id} 编辑通知
  - POST /api/admin/notices/{id}/publish 发布通知
  - POST /api/admin/notices/{id}/targeting 配置推送人群标签
  - POST /api/admin/notices/{id}/send 发送站内/微信/邮件通知
  - GET /api/admin/notices/{id}/delivery-records 查看送达记录与对象清单

  6. 电子证明与审批

  - GET /api/certificate/types 证明/申请类型列表
  - GET /api/certificate/templates 模板列表
  - POST /api/certificate/applications 发起证明/请假/盖章申请
  - GET /api/certificate/applications 获取本人申请列表
  - GET /api/certificate/applications/{id} 获取本人申请详情
  - POST /api/certificate/applications/{id}/withdraw 撤回申请
  - GET /api/certificate/applications/{id}/preview PDF 预览
  - GET /api/admin/approvals 审批列表
  - GET /api/admin/approvals/{id} 审批详情
  - POST /api/admin/approvals/{id}/approve 审批通过
  - POST /api/admin/approvals/{id}/reject 驳回
  - POST /api/admin/approvals/{id}/reopen 重批
  - POST /api/admin/certificate/templates 新增模板
  - PUT /api/admin/certificate/templates/{id} 编辑模板

  7. 荣誉展示

  - GET /api/honors 荣誉展示列表，支持年度/类别筛选
  - GET /api/honors/{id} 荣誉详情
  - POST /api/admin/honors 新增荣誉
  - PUT /api/admin/honors/{id} 编辑荣誉
  - POST /api/admin/honors/{id}/publish 上下架展示

  8. 数据导入导出 / 文档处理

  - POST /api/files/upload 通用文件上传
  - GET /api/files/{id} 文件元数据
  - GET /api/files/{id}/download 文件下载
  - POST /api/import/students 导入学生数据
  - POST /api/import/notices 导入通知数据
  - POST /api/import/party-records 导入党团流程数据
  - GET /api/import/tasks/{taskId} 查看导入结果与错误明细
  - GET /api/export/students 导出学生数据
  - GET /api/export/notices 导出通知数据
  - GET /api/export/approvals 导出审批数据

  9. 审计日志 / 后台支撑

  - GET /api/admin/dashboard/overview 后台概览统计
  - GET /api/admin/audit-logs 操作日志列表
  - GET /api/admin/audit-logs/{id} 操作日志详情
  - GET /api/admin/config/modules 模块配置读取
  - PUT /api/admin/config/modules 模块配置更新
  - GET /api/admin/dictionaries 字典项读取
  - PUT /api/admin/dictionaries/{code} 字典项维护

  10. P2 学业分析预留

  - GET /api/academic/plans 培养方案列表
  - POST /api/academic/transcripts/upload 上传成绩单
  - POST /api/academic/analyze 学分比对分析
  - GET /api/academic/analysis/{id} 分析结果详情