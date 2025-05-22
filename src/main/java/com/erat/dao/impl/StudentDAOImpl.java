package com.erat.dao.impl;

import com.erat.model.Student;
import com.erat.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl {
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Students VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGrade());
            stmt.setString(4, student.getMajor());
            stmt.setString(5, student.getClassId());
            stmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("StudentID"),
                    rs.getString("Name"),
                    rs.getString("Grade"),
                    rs.getString("Major"),
                    rs.getString("ClassID")
                );
                students.add(student);
            }
        }
        return students;
    }
}