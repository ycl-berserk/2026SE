-- V42__normalize_miniprogram_demo_grade_suffix.sql
-- Forward-only fix for V37 mini-program demo accounts.
--
-- V37 seeded student_profile.grade without the degree suffix (e.g. '2023'),
-- but the app expects a normalized suffix (e.g. '2023本'). V37 has already been
-- applied across environments, so it must not be edited in place; correct the
-- existing demo rows here instead.

UPDATE student_profile
SET grade = '2023本',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id IN (1001, 1002, 1003)
  AND grade = '2023';

UPDATE student_profile
SET grade = '2022本',
    updated_at = CURRENT_TIMESTAMP
WHERE user_id IN (3001, 3002)
  AND grade = '2022';
