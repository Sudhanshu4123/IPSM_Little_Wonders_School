package com.littlewonders.repository;

import com.littlewonders.model.PerformanceReport;
import com.littlewonders.model.User;
import com.littlewonders.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PerformanceReportRepository extends JpaRepository<PerformanceReport, Long> {
    List<PerformanceReport> findByStudent(User student);
    List<PerformanceReport> findByExam(Exam exam);
    List<PerformanceReport> findByStudentAndExam(User student, Exam exam);
    void deleteByStudent(User student);
}
