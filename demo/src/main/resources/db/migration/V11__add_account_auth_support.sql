ALTER TABLE t_user
    ADD COLUMN password_hash VARCHAR(100);

UPDATE t_user
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE password_hash IS NULL;

ALTER TABLE t_user
    ALTER COLUMN password_hash SET NOT NULL;

COMMENT ON COLUMN t_user.password_hash IS '账号密码 BCrypt 哈希';

ALTER TABLE student_profile
    ADD COLUMN auth_type VARCHAR(32) NOT NULL DEFAULT 'student';

UPDATE student_profile
SET auth_type = 'student'
WHERE auth_type IS NULL;

COMMENT ON COLUMN student_profile.auth_type IS '学生身份类型：student-普通学生，cadre-学生骨干';
