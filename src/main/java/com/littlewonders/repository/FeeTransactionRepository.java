package com.littlewonders.repository;

import com.littlewonders.model.FeeTransaction;
import com.littlewonders.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeTransactionRepository extends JpaRepository<FeeTransaction, Long> {
    List<FeeTransaction> findByStudent(User student);
    List<FeeTransaction> findByStudentAndFeeType(User student, String feeType);
    List<FeeTransaction> findByStudentAndFeeTypeAndMonthAndYear(User student, String feeType, String month, String year);
    List<FeeTransaction> findByStudentOrderByPaymentDateDesc(User student);
    void deleteByStudent(User student);
}
