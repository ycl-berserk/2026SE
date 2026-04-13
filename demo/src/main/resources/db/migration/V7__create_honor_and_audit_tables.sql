-- V7__create_honor_and_audit_tables.sql
-- 荣誉展示与审计日志相关表
-- 包括：荣誉记录、荣誉类别、审计日志

-- ==========================================
-- 1. 荣誉类别表
-- ==========================================
CREATE TABLE honor_category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    code VARCHAR(32) UNIQUE,
    sort_order INT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE honor_category IS '荣誉类别表';
COMMENT ON COLUMN honor_category.name IS '类别名称：学术竞赛/社会实践/志愿服务/文体活动';
COMMENT ON COLUMN honor_category.code IS '类别编码';
COMMENT ON COLUMN honor_category.status IS '状态：1-启用，0-禁用';

-- ==========================================
-- 2. 荣誉记录表
-- ==========================================
CREATE TABLE honor_record (
    id BIGINT PRIMARY KEY,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    student_id BIGINT NOT NULL,
    student_name VARCHAR(64) NOT NULL,
    student_no VARCHAR(32) NOT NULL,
    description VARCHAR(500),
    award_level VARCHAR(32),
    award_date DATE,
    certificate_image VARCHAR(500),
    status SMALLINT NOT NULL DEFAULT 0,
    publish_time TIMESTAMP,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_honor_category FOREIGN KEY (category_id) REFERENCES honor_category(id),
    CONSTRAINT fk_honor_student FOREIGN KEY (student_id) REFERENCES t_user(id)
);

COMMENT ON TABLE honor_record IS '荣誉记录表';
COMMENT ON COLUMN honor_record.title IS '荣誉名称';
COMMENT ON COLUMN honor_record.student_id IS '获奖学生 ID';
COMMENT ON COLUMN honor_record.student_no IS '获奖学生学号';
COMMENT ON COLUMN honor_record.award_level IS '获奖级别：国家级/省级/校级/院级';
COMMENT ON COLUMN honor_record.award_date IS '获奖日期';
COMMENT ON COLUMN honor_record.certificate_image IS '证书图片 URL';
COMMENT ON COLUMN honor_record.status IS '状态：0-草稿，1-已展示，2-已隐藏';
COMMENT ON COLUMN honor_record.publish_time IS '发布时间';

-- ==========================================
-- 3. 审计日志表
-- ==========================================
CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    module VARCHAR(64) NOT NULL,
    action VARCHAR(64) NOT NULL,
    description VARCHAR(500),
    request_method VARCHAR(10),
    request_url VARCHAR(500),
    ip_address VARCHAR(64),
    user_agent VARCHAR(500),
    execution_time BIGINT,
    status SMALLINT NOT NULL DEFAULT 1,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE audit_log IS '审计日志表';
COMMENT ON COLUMN audit_log.module IS '操作模块：user/knowledge/party/notice';
COMMENT ON COLUMN audit_log.action IS '操作类型：create/update/delete/login/logout';
COMMENT ON COLUMN audit_log.request_method IS '请求方法：GET/POST/PUT/DELETE';
COMMENT ON COLUMN audit_log.request_url IS '请求 URL';
COMMENT ON COLUMN audit_log.ip_address IS 'IP 地址';
COMMENT ON COLUMN audit_log.user_agent IS '用户代理';
COMMENT ON COLUMN audit_log.execution_time IS '执行时间（毫秒）';
COMMENT ON COLUMN audit_log.status IS '状态：1-成功，0-失败';
COMMENT ON COLUMN audit_log.error_message IS '错误信息';

-- ==========================================
-- 4. 索引优化
-- ==========================================
CREATE INDEX idx_honor_category ON honor_record(category_id);
CREATE INDEX idx_honor_student ON honor_record(student_id);
CREATE INDEX idx_honor_status ON honor_record(status);
CREATE INDEX idx_honor_award_date ON honor_record(award_date DESC);
CREATE INDEX idx_audit_user ON audit_log(user_id);
CREATE INDEX idx_audit_module ON audit_log(module);
CREATE INDEX idx_audit_created ON audit_log(created_at DESC);
