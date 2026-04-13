-- V1__create_auth_tables.sql
-- 认证与用户管理相关表
-- 包括：平台用户、角色、用户角色关联、微信绑定

-- ==========================================
-- 1. 平台用户表（避免使用 sys_user，因为人大金仓已有该系统表）
-- ==========================================
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY,
    student_no VARCHAR(32),
    real_name VARCHAR(64) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_user ADD CONSTRAINT uk_user_student_no UNIQUE (student_no);

COMMENT ON TABLE t_user IS '平台用户表';
COMMENT ON COLUMN t_user.student_no IS '学号';
COMMENT ON COLUMN t_user.real_name IS '真实姓名';
COMMENT ON COLUMN t_user.phone IS '手机号';
COMMENT ON COLUMN t_user.email IS '邮箱';
COMMENT ON COLUMN t_user.status IS '状态：1-正常，0-禁用';

-- ==========================================
-- 2. 角色表
-- ==========================================
CREATE TABLE t_role (
    id BIGINT PRIMARY KEY,
    role_code VARCHAR(32) NOT NULL,
    role_name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_role ADD CONSTRAINT uk_role_code UNIQUE (role_code);

COMMENT ON TABLE t_role IS '角色表';
COMMENT ON COLUMN t_role.role_code IS '角色编码：student/counselor/admin';
COMMENT ON COLUMN t_role.role_name IS '角色名称';
COMMENT ON COLUMN t_role.status IS '状态：1-启用，0-禁用';

-- ==========================================
-- 3. 用户角色关联表
-- ==========================================
CREATE TABLE t_user_role (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_user_role ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES t_user(id);
ALTER TABLE t_user_role ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES t_role(id);

COMMENT ON TABLE t_user_role IS '用户角色关联表';

-- ==========================================
-- 4. 微信绑定表
-- ==========================================
CREATE TABLE t_wx_bind (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    open_id VARCHAR(128) NOT NULL,
    union_id VARCHAR(128),
    session_key_enc VARCHAR(256),
    last_login_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE t_wx_bind ADD CONSTRAINT uk_wx_open_id UNIQUE (open_id);
ALTER TABLE t_wx_bind ADD CONSTRAINT fk_wx_bind_user FOREIGN KEY (user_id) REFERENCES t_user(id);

COMMENT ON TABLE t_wx_bind IS '微信绑定表';
COMMENT ON COLUMN t_wx_bind.open_id IS '微信 OpenID';
COMMENT ON COLUMN t_wx_bind.union_id IS '微信 UnionID';
COMMENT ON COLUMN t_wx_bind.session_key_enc IS '加密的 SessionKey';
