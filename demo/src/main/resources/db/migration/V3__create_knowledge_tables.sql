-- V3__create_knowledge_tables.sql
-- 知识库与资料管理相关表
-- 包括：知识分类、知识条目、关键词、模板文件

-- ==========================================
-- 1. 知识分类表
-- ==========================================
CREATE TABLE knowledge_category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    code VARCHAR(32) UNIQUE,
    sort_order INT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE knowledge_category IS '知识分类表';
COMMENT ON COLUMN knowledge_category.name IS '分类名称';
COMMENT ON COLUMN knowledge_category.code IS '分类编码';
COMMENT ON COLUMN knowledge_category.sort_order IS '排序顺序';
COMMENT ON COLUMN knowledge_category.status IS '状态：1-启用，0-禁用';

-- ==========================================
-- 2. 知识条目表
-- ==========================================
CREATE TABLE knowledge_article (
    id BIGINT PRIMARY KEY,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    summary VARCHAR(500),
    content TEXT NOT NULL,
    answer TEXT,
    source VARCHAR(255),
    status SMALLINT NOT NULL DEFAULT 0,
    publish_time TIMESTAMP,
    view_count BIGINT NOT NULL DEFAULT 0,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_article_category FOREIGN KEY (category_id) REFERENCES knowledge_category(id)
);

COMMENT ON TABLE knowledge_article IS '知识条目表';
COMMENT ON COLUMN knowledge_article.title IS '标题';
COMMENT ON COLUMN knowledge_article.summary IS '摘要';
COMMENT ON COLUMN knowledge_article.content IS '详细内容';
COMMENT ON COLUMN knowledge_article.answer IS '标准答案/办理建议';
COMMENT ON COLUMN knowledge_article.source IS '来源：制度摘要/辅导说明/办理指引';
COMMENT ON COLUMN knowledge_article.status IS '状态：0-草稿，1-已发布，2-已下架';
COMMENT ON COLUMN knowledge_article.publish_time IS '发布时间';
COMMENT ON COLUMN knowledge_article.view_count IS '浏览次数';

-- ==========================================
-- 3. 关键词表
-- ==========================================
CREATE TABLE knowledge_keyword (
    id BIGINT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    keyword VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_keyword_article FOREIGN KEY (article_id) REFERENCES knowledge_article(id)
);

COMMENT ON TABLE knowledge_keyword IS '知识条目关键词表';

-- ==========================================
-- 4. 模板文件表
-- ==========================================
CREATE TABLE knowledge_template (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    category VARCHAR(64),
    file_id BIGINT,
    format VARCHAR(16),
    download_count BIGINT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE knowledge_template IS '模板文件表';
COMMENT ON COLUMN knowledge_template.category IS '模板分类';
COMMENT ON COLUMN knowledge_template.file_id IS '关联文件 ID';
COMMENT ON COLUMN knowledge_template.format IS '文件格式：DOCX/XLSX/PDF';
COMMENT ON COLUMN knowledge_template.download_count IS '下载次数';
COMMENT ON COLUMN knowledge_template.status IS '状态：1-启用，0-禁用';

-- ==========================================
-- 5. 索引优化
-- ==========================================
CREATE INDEX idx_article_category ON knowledge_article(category_id);
CREATE INDEX idx_article_status ON knowledge_article(status);
CREATE INDEX idx_article_publish_time ON knowledge_article(publish_time DESC);
CREATE INDEX idx_keyword_article ON knowledge_keyword(article_id);
CREATE INDEX idx_keyword_keyword ON knowledge_keyword(keyword);
CREATE INDEX idx_template_status ON knowledge_template(status);
