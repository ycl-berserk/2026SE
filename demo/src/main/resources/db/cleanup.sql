-- ============================================
-- 清理脚本 - 仅用于开发环境重置数据库
-- 执行此脚本将删除所有表和 Flyway 历史记录
-- 生产环境严禁使用！
-- ============================================

-- 删除整个 public schema 并重新创建（最彻底的方式）
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- 授权
GRANT ALL ON SCHEMA public TO system;
GRANT ALL ON SCHEMA public TO PUBLIC;

-- 注释
COMMENT ON SCHEMA public IS 'standard public schema';

-- 验证是否清理完成
-- SELECT tablename FROM pg_tables WHERE schemaname = 'public';
