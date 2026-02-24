package com.littlewonders.repository;

import com.littlewonders.model.CareerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends JpaRepository<CareerSubmission, Long> {
}
