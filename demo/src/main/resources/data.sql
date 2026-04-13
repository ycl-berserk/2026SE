INSERT INTO t_user (id, student_no, real_name, phone, email, status, created_at, updated_at) VALUES
(1001, '2023001', 'Zhang San', '13800138001', 'zhangsan@example.com', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO t_role (id, role_code, role_name, description, status, created_at) VALUES
(1, 'student', 'Student', 'Default student role', 1, CURRENT_TIMESTAMP);

INSERT INTO t_user_role (id, user_id, role_id, created_at) VALUES
(1, 1001, 1, CURRENT_TIMESTAMP);

INSERT INTO t_wx_bind (id, user_id, open_id, union_id, session_key_enc, last_login_at, created_at) VALUES
(1, 1001, 'mock_openid_demo', NULL, NULL, NULL, CURRENT_TIMESTAMP);

INSERT INTO student_profile (id, user_id, student_no, gender, grade, major, class_name, political_status, id_card, avatar_url, bio, hometown, dormitory, created_at, updated_at) VALUES
(1, 1001, '2023001', 1, '2023', 'Software Engineering', 'SE-1', 'League Member', NULL, NULL, 'Demo account for local testing', 'Beijing', '1-101', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_message (id, user_id, notice_id, title, summary, read_status, read_time, created_at) VALUES
(1, 1001, NULL, 'System Notice', 'Welcome to the backend demo.', 0, NULL, CURRENT_TIMESTAMP),
(2, 1001, NULL, 'Task Reminder', 'Please check your party progress page.', 0, NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

INSERT INTO party_student_progress (id, user_id, current_stage_code, updated_at) VALUES
(1, 1001, 'activist', CURRENT_TIMESTAMP);

INSERT INTO party_student_record (id, user_id, stage_code, record_type, title, description, event_time, status, created_at) VALUES
(1, 1001, 'applicant', 'submit', 'Submit application', 'Application materials have been submitted.', DATEADD('DAY', -30, CURRENT_TIMESTAMP), 1, CURRENT_TIMESTAMP),
(2, 1001, 'activist', 'training', 'Attend training', 'First training session completed.', DATEADD('DAY', -7, CURRENT_TIMESTAMP), 1, CURRENT_TIMESTAMP);

INSERT INTO party_reminder (id, user_id, title, content, deadline, status, reminder_type, created_at) VALUES
(1, 1001, 'Thought report', 'Please submit your report within this week.', DATEADD('DAY', 5, CURRENT_TIMESTAMP), 0, 'report', CURRENT_TIMESTAMP),
(2, 1001, 'Material update', 'Please complete your profile materials.', DATEADD('DAY', 10, CURRENT_TIMESTAMP), 0, 'material', CURRENT_TIMESTAMP);

INSERT INTO knowledge_category (id, name, code, sort_order, status) VALUES
(1, 'Party Process', 'party_process', 1, 1),
(2, 'Daily Service', 'daily_service', 2, 1);

INSERT INTO knowledge_article (id, category_id, title, summary, content, answer, source, status, publish_time, view_count, created_by, created_at, updated_at) VALUES
(1, 1, 'Party application process', 'Basic process from application to training review.', 'Demo content.', 'Submit the application first, then continue with the review process.', 'Demo data', 1, CURRENT_TIMESTAMP, 3, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'Student certificate guide', 'How to apply for common certificates.', 'Demo content.', 'Submit the application online and wait for review.', 'Demo data', 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP), 8, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO knowledge_template (id, name, description, category, file_id, format, download_count, status, created_at, updated_at) VALUES
(1, 'Thought report template', 'Template metadata for the thought report.', 'Party Process', NULL, 'DOCX', 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
