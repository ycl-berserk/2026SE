-- V5__create_notice_message_tables.sql
-- 通知与消息管理相关表
-- 包括：通知公告、用户消息

-- ==========================================
-- 1. 通知表
-- ==========================================
CREATE TABLE notice (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500),
    content TEXT NOT NULL,
    notice_type VARCHAR(32),
    tag VARCHAR(32),
    status SMALLINT NOT NULL DEFAULT 0,
    publish_time TIMESTAMP,
    priority SMALLINT NOT NULL DEFAULT 0,
    created_by BIGINT,
    target_tags VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE notice IS '通知公告表';
COMMENT ON COLUMN notice.title IS '通知标题';
COMMENT ON COLUMN notice.summary IS '摘要';
COMMENT ON COLUMN notice.content IS '详细内容';
COMMENT ON COLUMN notice.notice_type IS '通知类型：就业/党团/教学/生活/其他';
COMMENT ON COLUMN notice.tag IS '标签';
COMMENT ON COLUMN notice.status IS '状态：0-草稿，1-已发布，2-已下架';
COMMENT ON COLUMN notice.publish_time IS '发布时间';
COMMENT ON COLUMN notice.priority IS '优先级：0-普通，1-重要，2-紧急';
COMMENT ON COLUMN notice.created_by IS '创建人 ID';
COMMENT ON COLUMN notice.target_tags IS '目标人群标签：年级/专业/班级，逗号分隔';

-- ==========================================
-- 2. 用户消息表（收件箱）
-- ==========================================
CREATE TABLE user_message (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notice_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500),
    read_status SMALLINT NOT NULL DEFAULT 0,
    read_time TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_message_notice FOREIGN KEY (notice_id) REFERENCES notice(id)
);

COMMENT ON TABLE user_message IS '用户消息表（收件箱）';
COMMENT ON COLUMN user_message.title IS '消息标题';
COMMENT ON COLUMN user_message.read_status IS '阅读状态：0-未读，1-已读';
COMMENT ON COLUMN user_message.read_time IS '阅读时间';

-- ==========================================
-- 3. 索引优化
-- ==========================================
CREATE INDEX idx_notice_status ON notice(status);
CREATE INDEX idx_notice_publish_time ON notice(publish_time DESC);
CREATE INDEX idx_notice_type ON notice(notice_type);
CREATE INDEX idx_message_user ON user_message(user_id);
CREATE INDEX idx_message_read_status ON user_message(read_status);
CREATE INDEX idx_message_notice ON user_message(notice_id);
