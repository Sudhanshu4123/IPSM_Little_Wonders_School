package com.littlewonders.repository;

import com.littlewonders.model.FranchiseEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository extends JpaRepository<FranchiseEnquiry, Long> {
}
