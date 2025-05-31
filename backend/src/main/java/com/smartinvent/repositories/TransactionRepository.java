package com.smartinvent.repositories;



import com.smartinvent.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByEmployee_EmployeeId(Long employeeId);

}
