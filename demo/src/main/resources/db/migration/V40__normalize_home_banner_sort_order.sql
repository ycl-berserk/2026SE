-- 将首页轮播图排序统一调整为 1 起步。
-- 若历史数据中存在 0 号排序，则整体后移一位，避免继续显示 0。
UPDATE home_banner
SET sort_order = sort_order + 1
WHERE EXISTS (
    SELECT 1 FROM home_banner WHERE sort_order = 0
);
