-- V10__leave_mvp.sql
-- 请假申请 MVP：扩展字段 + 演示数据

-- 1) 扩展申请表字段（请假专用）
ALTER TABLE certificate_application
    ADD COLUMN leave_start_date DATE;

ALTER TABLE certificate_application
    ADD COLUMN leave_end_date DATE;

ALTER TABLE certificate_application
    ADD COLUMN contact_phone VARCHAR(20);

COMMENT ON COLUMN certificate_application.leave_start_date IS '请假开始日期';
COMMENT ON COLUMN certificate_application.leave_end_date IS '请假结束日期';
COMMENT ON COLUMN certificate_application.contact_phone IS '联系电话';

-- 2) 给演示登录用户补辅导员角色，便于本地审批联调
INSERT INTO t_user_role (id, user_id, role_id)
SELECT 4, 1001, 2
WHERE NOT EXISTS (
    SELECT 1 FROM t_user_role WHERE user_id = 1001 AND role_id = 2
);

-- 3) 请假申请演示数据
INSERT INTO certificate_application (
    id, user_id, type_code, title, reason, status, submit_time,
    leave_start_date, leave_end_date, contact_phone, created_at, updated_at
) VALUES
(9301, 1001, 'leave', '参加学术会议请假', '参加学院推荐的学术会议，需外出两天。', 0, '2026-04-17 09:20:00',
 '2026-04-20', '2026-04-21', '13800138001', '2026-04-17 09:20:00', '2026-04-17 09:20:00'),
(9302, 1001, 'leave', '返乡办理证件请假', '需返乡办理身份证补办手续。', 2, '2026-04-10 11:00:00',
 '2026-04-12', '2026-04-13', '13800138001', '2026-04-10 11:00:00', '2026-04-10 18:00:00');

INSERT INTO approval_record (id, application_id, approver_id, action, comment, created_at) VALUES
(9401, 9301, 1001, 'submit', '提交请假申请', '2026-04-17 09:20:00'),
(9402, 9302, 1001, 'submit', '提交请假申请', '2026-04-10 11:00:00'),
(9403, 9302, 1001, 'approve', '材料完整，审批通过。', '2026-04-10 18:00:00');
