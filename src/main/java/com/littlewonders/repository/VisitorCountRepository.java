package com.littlewonders.repository;

import com.littlewonders.model.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {
}
