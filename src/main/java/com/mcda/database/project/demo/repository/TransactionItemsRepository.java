package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.TransactionItems;
import org.springframework.data.repository.CrudRepository;

public interface TransactionItemsRepository extends CrudRepository<TransactionItems, Integer> {
    
}
