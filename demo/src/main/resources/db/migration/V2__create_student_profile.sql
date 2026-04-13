-- V2__create_student_profile.sql
-- 学生档案与基础信息表

-- ==========================================
-- 1. 学生档案表
-- ==========================================
CREATE TABLE student_profile (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    student_no VARCHAR(32) NOT NULL UNIQUE,
    gender SMALLINT,
    grade VARCHAR(16),
    major VARCHAR(64),
    class_name VARCHAR(64),
    political_status VARCHAR(32),
    id_card VARCHAR(18),
    avatar_url VARCHAR(500),
    bio VARCHAR(500),
    hometown VARCHAR(255),
    dormitory VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_profile_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);

COMMENT ON TABLE student_profile IS '学生档案表';
COMMENT ON COLUMN student_profile.student_no IS '学号';
COMMENT ON COLUMN student_profile.gender IS '性别：1-男，2-女';
COMMENT ON COLUMN student_profile.grade IS '年级：如 2023';
COMMENT ON COLUMN student_profile.major IS '专业';
COMMENT ON COLUMN student_profile.class_name IS '班级';
COMMENT ON COLUMN student_profile.political_status IS '政治面貌：群众/共青团员/预备党员/正式党员';
COMMENT ON COLUMN student_profile.id_card IS '身份证号';
COMMENT ON COLUMN student_profile.avatar_url IS '头像 URL';
COMMENT ON COLUMN student_profile.bio IS '个人简介';
COMMENT ON COLUMN student_profile.hometown IS '生源地';
COMMENT ON COLUMN student_profile.dormitory IS '宿舍号';

-- ==========================================
-- 2. 索引优化
-- ==========================================
CREATE INDEX idx_student_profile_grade ON student_profile(grade);
CREATE INDEX idx_student_profile_major ON student_profile(major);
CREATE INDEX idx_student_profile_class ON student_profile(class_name);
