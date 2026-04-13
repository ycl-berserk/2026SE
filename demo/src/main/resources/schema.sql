CREATE TABLE t_user (
    id BIGINT PRIMARY KEY,
    student_no VARCHAR(32),
    real_name VARCHAR(64) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    status INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE t_role (
    id BIGINT PRIMARY KEY,
    role_code VARCHAR(32) NOT NULL,
    role_name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status INTEGER NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE t_user_role (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE t_wx_bind (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    open_id VARCHAR(128) NOT NULL,
    union_id VARCHAR(128),
    session_key_enc VARCHAR(256),
    last_login_at TIMESTAMP,
    created_at TIMESTAMP
);

CREATE TABLE student_profile (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    student_no VARCHAR(32),
    gender INTEGER,
    grade VARCHAR(16),
    major VARCHAR(100),
    class_name VARCHAR(100),
    political_status VARCHAR(64),
    id_card VARCHAR(32),
    avatar_url VARCHAR(255),
    bio VARCHAR(500),
    hometown VARCHAR(100),
    dormitory VARCHAR(64),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE user_message (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notice_id BIGINT,
    title VARCHAR(200),
    summary VARCHAR(500),
    read_status INTEGER,
    read_time TIMESTAMP,
    created_at TIMESTAMP
);

CREATE TABLE party_student_progress (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    current_stage_code VARCHAR(64),
    updated_at TIMESTAMP
);

CREATE TABLE party_student_record (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    stage_code VARCHAR(64),
    record_type VARCHAR(64),
    title VARCHAR(200),
    description VARCHAR(500),
    event_time TIMESTAMP,
    status INTEGER,
    created_at TIMESTAMP
);

CREATE TABLE party_reminder (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200),
    content VARCHAR(500),
    deadline TIMESTAMP,
    status INTEGER,
    reminder_type VARCHAR(64),
    created_at TIMESTAMP
);

CREATE TABLE party_report (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    stage_code VARCHAR(64),
    title VARCHAR(200),
    content CLOB,
    file_id BIGINT,
    submit_time TIMESTAMP,
    status INTEGER,
    review_comment VARCHAR(500),
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP
);

CREATE TABLE knowledge_category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    code VARCHAR(64),
    sort_order INTEGER,
    status INTEGER
);

CREATE TABLE knowledge_article (
    id BIGINT PRIMARY KEY,
    category_id BIGINT,
    title VARCHAR(200),
    summary VARCHAR(500),
    content CLOB,
    answer CLOB,
    source VARCHAR(255),
    status INTEGER,
    publish_time TIMESTAMP,
    view_count BIGINT,
    created_by BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE knowledge_template (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200),
    description VARCHAR(500),
    category VARCHAR(100),
    file_id BIGINT,
    format VARCHAR(32),
    download_count BIGINT,
    status INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE file_metadata (
    id BIGINT PRIMARY KEY,
    biz_type VARCHAR(64),
    origin_name VARCHAR(255),
    stored_name VARCHAR(255),
    storage_path VARCHAR(500),
    mime_type VARCHAR(100),
    file_size BIGINT,
    uploader_id BIGINT,
    status INTEGER,
    created_at TIMESTAMP
);
