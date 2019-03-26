package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    void deleteByTransactionNumber(int transactionNumber);

    List<Transaction> findByTransactionDateAfter(Date thirtyDaysAgoDate);
}
