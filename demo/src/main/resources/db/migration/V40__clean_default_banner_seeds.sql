-- V40__clean_default_banner_seeds.sql
-- 删除旧版 V30 中插入的默认轮播 seed 数据。
-- 轮播图现在只由 home_banner 表管理，没有默认兜底内容。

DELETE FROM home_banner WHERE id IN (60001, 60002, 60003);
