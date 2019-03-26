package com.mcda.database.project.demo.controller;

import com.mcda.database.project.demo.dto.ReturnTables;
import com.mcda.database.project.demo.dto.TransactionInfo;
import com.mcda.database.project.demo.model.*;
import com.mcda.database.project.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class CustomerController {

    @Autowired
    private AllRepository allRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionItemsRepository transactionItemsRepository;

    @GetMapping("getTable/{tableName}")
    public ReturnTables findTable(@PathVariable String tableName) {
        ReturnTables returnTables = new ReturnTables();
        returnTables.setFields(allRepository.findAllTableFields(tableName));
        returnTables.setValues(allRepository.findAllTable(tableName));
        return returnTables;
    }

    @Transactional
    @PostMapping("create/article")
    public boolean createTable(@RequestBody Article article) {
        System.out.println(article.getPages());
        System.out.println(article.getAuthors());
        articleRepository.save(article);
        return true;
    }

    @Transactional
    @PostMapping("create/customer")
    public boolean createCustomer(@RequestBody Customer customer) throws Exception {
        if (customerRepository.existsByFirstNameAndLastName(customer.getFirstName(), customer.getLastName())) {
            throw new Exception("Already exists");
        }
        customerRepository.save(customer);
        return true;
    }

    @Transactional
    @PostMapping("create/transaction")
    public boolean createTransaction(@RequestBody TransactionInfo transactionInfo) {
        System.out.println("customer id is " + transactionInfo.getCustomerId());
        float doublePrice = 0;
        List<Items> items = transactionInfo.getItems();
        for (Items item : items) {
            doublePrice += item.getPrice();
        }
        Transaction transaction = new Transaction();
        transaction.setCustomerId(transactionInfo.getCustomerId());
        transaction.setTotalPurchasePrice(doublePrice);
        transaction.setTransactionDate(new Date());
        transactionRepository.save(transaction);

        // add items

        TransactionItems transactionItems;
        for (Items item : items) {
            transactionItems = new TransactionItems();

            transactionItems.setItemId(item.getId());
            transactionItems.setPrice(item.getPrice());
            System.out.println("price is " + transactionItems.getPrice());
            System.out.println("transaction number is " + transaction.getTransactionNumber());
            transactionItems.setTransactionNumber(transaction.getTransactionNumber());
            transactionItemsRepository.saveAndFlush(transactionItems);
            
            System.out.println("save successfully ------------");
        }

        // update discount code

        return true;
    }

}
