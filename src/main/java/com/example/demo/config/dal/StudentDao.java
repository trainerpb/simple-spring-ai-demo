package com.example.demo.config.dal;

import com.example.demo.model.Student;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDao {
    private final JdbcTemplate jdbcTemplate;

    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(name = "queryStudents", description = "Query students modules by marks")
    public List<Student> query(
            @ToolParam(description = "marks") int marks
    ) {
        String sql = "SELECT * FROM student WHERE marks > ?";
        return jdbcTemplate.queryForList(sql, Student.class,  marks);
    }
}
