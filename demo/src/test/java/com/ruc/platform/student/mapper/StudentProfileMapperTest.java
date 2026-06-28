package com.ruc.platform.student.mapper;

import com.ruc.platform.PlatformApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PlatformApplication.class)
@ActiveProfiles("h2")
class StudentProfileMapperTest {

    @Autowired
    private StudentProfileMapper studentProfileMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void normalizeSeedStudents() {
        jdbcTemplate.update("UPDATE t_user SET status = 1 WHERE id IN (1001, 1002, 1003)");
        jdbcTemplate.update("""
                UPDATE student_profile
                SET grade = '2023本', auth_type = CASE WHEN user_id = 1003 THEN 'cadre' ELSE 'student' END
                WHERE user_id IN (1001, 1002, 1003)
                """);
    }

    @Test
    void selectsTargetStudentUserIdsByMultipleGradesAndMajors() {
        List<Long> userIds = studentProfileMapper.selectTargetStudentUserIds(
                List.of("2023本"),
                List.of("计算机科学与技术", "软件工程", "信息安全"),
                null,
                null
        );

        assertThat(userIds).contains(1001L, 1002L, 1003L);
    }

    @Test
    void selectsTargetStudentUserIdsWhenOneOfMultipleGradesMatches() {
        List<Long> userIds = studentProfileMapper.selectTargetStudentUserIds(
                List.of("2024本", "2023本", "2022硕"),
                null,
                null,
                null
        );

        assertThat(userIds).contains(1001L, 1002L, 1003L);
    }

    @Test
    void selectsTargetStudentUserIdsWhenGradesAreSplitFromPastedText() {
        List<Long> userIds = studentProfileMapper.selectTargetStudentUserIds(
                List.of("2024本", "2023本", "2022硕"),
                null,
                null,
                null
        );

        assertThat(userIds).contains(1001L, 1002L, 1003L);
    }
}
