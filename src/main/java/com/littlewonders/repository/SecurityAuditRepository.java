package com.littlewonders.repository;

import com.littlewonders.model.SecurityAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecurityAuditRepository extends JpaRepository<SecurityAudit, Long> {
    List<SecurityAudit> findTop10ByOrderByAttemptedAtDesc();
}
