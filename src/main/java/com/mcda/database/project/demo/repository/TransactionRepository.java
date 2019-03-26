package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
}
