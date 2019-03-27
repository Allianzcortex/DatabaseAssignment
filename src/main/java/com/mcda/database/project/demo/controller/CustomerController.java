package com.mcda.database.project.demo.controller;

import com.mcda.database.project.demo.dto.ReturnTables;
import com.mcda.database.project.demo.dto.TransactionInfo;
import com.mcda.database.project.demo.model.*;
import com.mcda.database.project.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.*;

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

    @CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("getTable/{tableName}")
    public ReturnTables findTable(@PathVariable String tableName) {
        ReturnTables returnTables = new ReturnTables();
        System.out.println("fuck");
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
        Transaction tempTransaction = transactionRepository.saveAndFlush(transaction);

        // add items
        List<TransactionItems> transactionItemsList = new ArrayList<>();
        for (Items item : items) {
            TransactionItems transactionItems = new TransactionItems();
            transactionItems.setItemId(item.getId());
            transactionItems.setPrice(item.getPrice());
            System.out.println("price is " + tempTransaction.getTotalPurchasePrice());
            System.out.println("transaction number is " + tempTransaction.getTransactionNumber());
            transactionItems.setTransactionNumber(tempTransaction.getTransactionNumber());
            transactionItemsList.add(transactionItems);
        }
        System.out.println(transactionItemsList.get(0));
        System.out.println(transactionItemsList.get(1));
        transactionItemsRepository.saveAll(transactionItemsList);

        // TODO update discount code


        return true;
    }

    @Transactional
    @DeleteMapping("cancel/transaction/{transactionNumber}")
    public List<Transaction> cancelTransaction(@PathVariable int transactionNumber) {
        // should check whether transactionNumber exists
        transactionRepository.deleteByTransactionNumber(transactionNumber);
        transactionItemsRepository.deleteByTransactionNumber(transactionNumber);

        // provided the
        // transaction occurred no more than 30 days before the current day
        Date today = new Date();
        //this line is supposedly to get the date that is 30 days ago
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date today30 = cal.getTime();

        return transactionRepository.findByTransactionDateAfter(today30);
    }

}
