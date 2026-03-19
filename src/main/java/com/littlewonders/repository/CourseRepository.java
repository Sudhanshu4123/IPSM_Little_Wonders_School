package com.littlewonders.repository;

import com.littlewonders.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByActiveTrue();
    Optional<Course> findByName(String name);
}
