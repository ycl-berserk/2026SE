-- V4__create_party_progress_tables.sql
-- 党团流程与进度管理相关表
-- 包括：阶段定义、学生进度、阶段记录、提醒事项、思想汇报

-- ==========================================
-- 1. 阶段定义表
-- ==========================================
CREATE TABLE party_stage_def (
    id BIGINT PRIMARY KEY,
    stage_code VARCHAR(64) NOT NULL UNIQUE,
    stage_name VARCHAR(64) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    description VARCHAR(500),
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE party_stage_def IS '党团阶段定义表';
COMMENT ON COLUMN party_stage_def.stage_code IS '阶段编码：applicant/activist/development_target/probationary_member/full_member';
COMMENT ON COLUMN party_stage_def.stage_name IS '阶段名称：入党申请人/积极分子/发展对象/预备党员/正式党员';
COMMENT ON COLUMN party_stage_def.sort_order IS '排序顺序';
COMMENT ON COLUMN party_stage_def.status IS '状态：1-启用，0-禁用';

-- ==========================================
-- 2. 学生进度表
-- ==========================================
CREATE TABLE party_student_progress (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    current_stage_code VARCHAR(64) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_party_progress_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);

COMMENT ON TABLE party_student_progress IS '学生党团进度表';
COMMENT ON COLUMN party_student_progress.current_stage_code IS '当前阶段编码';

-- ==========================================
-- 3. 阶段记录表
-- ==========================================
CREATE TABLE party_student_record (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    stage_code VARCHAR(64) NOT NULL,
    record_type VARCHAR(32) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    event_time TIMESTAMP,
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_party_record_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);

COMMENT ON TABLE party_student_record IS '学生党团阶段记录表';
COMMENT ON COLUMN party_student_record.record_type IS '记录类型：stage_change/report/training/exam';
COMMENT ON COLUMN party_student_record.title IS '记录标题';
COMMENT ON COLUMN party_student_record.event_time IS '事件发生时间';
COMMENT ON COLUMN party_student_record.status IS '状态：1-有效，0-无效';

-- ==========================================
-- 4. 提醒事项表
-- ==========================================
CREATE TABLE party_reminder (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(500),
    deadline TIMESTAMP,
    status SMALLINT NOT NULL DEFAULT 0,
    reminder_type VARCHAR(32),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_party_reminder_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);

COMMENT ON TABLE party_reminder IS '党团提醒事项表';
COMMENT ON COLUMN party_reminder.title IS '提醒标题';
COMMENT ON COLUMN party_reminder.content IS '提醒内容';
COMMENT ON COLUMN party_reminder.deadline IS '截止时间';
COMMENT ON COLUMN party_reminder.status IS '状态：0-待处理，1-已完成，2-已过期';
COMMENT ON COLUMN party_reminder.reminder_type IS '提醒类型：report/training/meeting';

-- ==========================================
-- 5. 思想汇报表
-- ==========================================
CREATE TABLE party_report (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    stage_code VARCHAR(64),
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_id BIGINT,
    submit_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status SMALLINT NOT NULL DEFAULT 0,
    review_comment VARCHAR(500),
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_party_report_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);

COMMENT ON TABLE party_report IS '思想汇报表';
COMMENT ON COLUMN party_report.content IS '汇报内容';
COMMENT ON COLUMN party_report.file_id IS '附件文件 ID';
COMMENT ON COLUMN party_report.status IS '状态：0-待审核，1-已通过，2-已驳回';
COMMENT ON COLUMN party_report.review_comment IS '审核意见';
COMMENT ON COLUMN party_report.reviewed_by IS '审核人 ID';
COMMENT ON COLUMN party_report.reviewed_at IS '审核时间';

-- ==========================================
-- 6. 索引优化
-- ==========================================
CREATE INDEX idx_party_progress_stage ON party_student_progress(current_stage_code);
CREATE INDEX idx_party_record_user ON party_student_record(user_id);
CREATE INDEX idx_party_record_stage ON party_student_record(stage_code);
CREATE INDEX idx_party_record_time ON party_student_record(event_time DESC);
CREATE INDEX idx_party_reminder_user ON party_reminder(user_id);
CREATE INDEX idx_party_reminder_status ON party_reminder(status);
CREATE INDEX idx_party_reminder_deadline ON party_reminder(deadline);
CREATE INDEX idx_party_report_user ON party_report(user_id);
CREATE INDEX idx_party_report_status ON party_report(status);
