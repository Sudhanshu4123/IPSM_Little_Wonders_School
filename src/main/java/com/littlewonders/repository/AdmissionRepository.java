package com.littlewonders.repository;

import com.littlewonders.model.AdmissionApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionApplication, Long> {
}
