-- 将首页轮播图排序统一压缩为 1..N，修复历史数据中的排序空洞。
UPDATE home_banner
SET sort_order = ranked.rn
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY sort_order, id) AS rn
    FROM home_banner
) ranked
WHERE home_banner.id = ranked.id;
