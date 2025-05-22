package com.erat.service.impl;

import com.erat.dao.StudentDAO;
import com.erat.dao.SubmissionDAO;
import com.erat.model.Student;
import com.erat.service.ReportService;
import java.util.*;

public class ReportServiceImpl implements ReportService {
    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final SubmissionDAO submissionDAO = new SubmissionDAOImpl();

    @Override
    public Map<String, Integer> getStudentMissingStats(String studentId) {
        return submissionDAO.getMissingExperimentsByStudent(studentId);
    }

    @Override
    public void importStudentsFromFile(String filePath, String fileType) {
        // 工厂模式调用
        FileParser parser = FileParserFactory.getParser(fileType);
        List<Student> students = parser.parseStudentFile(filePath);
        students.forEach(student -> {
            try {
                studentDAO.addStudent(student);
            } catch (SQLException e) {
                LoggerUtil.logError("导入学生失败: " + e.getMessage());
            }
        });
    }
}