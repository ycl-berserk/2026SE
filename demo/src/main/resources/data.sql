INSERT INTO t_user (id, student_no, real_name, phone, email, status, created_at, updated_at) VALUES
(1001, '2023001', '张三', '13800138001', 'zhangsan@example.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO t_role (id, role_code, role_name, description, status, created_at) VALUES
(1, 'student', '学生', '默认学生角色', 1, CURRENT_TIMESTAMP);

INSERT INTO t_user_role (id, user_id, role_id, created_at) VALUES
(1, 1001, 1, CURRENT_TIMESTAMP);

INSERT INTO t_wx_bind (id, user_id, open_id, union_id, session_key_enc, last_login_at, created_at) VALUES
(1, 1001, 'mock_openid_demo', NULL, NULL, NULL, CURRENT_TIMESTAMP);

INSERT INTO student_profile (id, user_id, student_no, gender, grade, major, class_name, political_status, id_card, avatar_url, bio, hometown, dormitory, created_at, updated_at) VALUES
(1, 1001, '2023001', 1, '2023', '软件工程', '软工1班', '共青团员', NULL, NULL, '用于演示的测试账号', '北京', '1-101', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_message (id, user_id, notice_id, title, summary, read_status, read_time, created_at) VALUES
(1, 1001, NULL, '系统通知', '欢迎使用演示后端', 0, NULL, CURRENT_TIMESTAMP),
(2, 1001, NULL, '办事提醒', '请查看党团流程进度', 0, NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

INSERT INTO party_student_progress (id, user_id, current_stage_code, updated_at) VALUES
(1, 1001, 'activist', CURRENT_TIMESTAMP);

INSERT INTO party_student_record (id, user_id, stage_code, record_type, title, description, event_time, status, created_at) VALUES
(1, 1001, 'applicant', 'submit', '提交入党申请书', '已完成材料提交', DATEADD('DAY', -30, CURRENT_TIMESTAMP), 1, CURRENT_TIMESTAMP),
(2, 1001, 'activist', 'training', '参加积极分子培训', '已完成首次培训', DATEADD('DAY', -7, CURRENT_TIMESTAMP), 1, CURRENT_TIMESTAMP);

INSERT INTO party_reminder (id, user_id, title, content, deadline, status, reminder_type, created_at) VALUES
(1, 1001, '思想汇报', '请在本周内提交思想汇报', DATEADD('DAY', 5, CURRENT_TIMESTAMP), 0, 'report', CURRENT_TIMESTAMP),
(2, 1001, '材料补充', '请补充个人信息材料', DATEADD('DAY', 10, CURRENT_TIMESTAMP), 0, 'material', CURRENT_TIMESTAMP);

INSERT INTO knowledge_category (id, name, code, sort_order, status) VALUES
(1, '党团流程', 'party_process', 1, 1),
(2, '日常服务', 'daily_service', 2, 1);

INSERT INTO knowledge_article (id, category_id, title, summary, content, answer, source, status, publish_time, view_count, created_by, created_at, updated_at) VALUES
(1, 1, '入党申请流程说明', '介绍从申请到培养考察的基本流程', '这是演示内容。', '先提交申请书，再进入后续培养流程。', '演示数据', 1, CURRENT_TIMESTAMP, 3, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, '学生证明办理指南', '说明常用证明的申请步骤', '这是演示内容。', '在线提交后等待审核即可。', '演示数据', 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP), 8, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO knowledge_template (id, name, description, category, file_id, format, download_count, status, created_at, updated_at) VALUES
(1, '思想汇报模板', '用于提交思想汇报的演示模板', '党团流程', NULL, 'DOCX', 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
