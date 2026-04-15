package com.littlewonders.repository;

import com.littlewonders.model.Attendance;
import com.littlewonders.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentAndDate(User student, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
    void deleteByStudent(User student);
}
