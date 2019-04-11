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

    @Autowired
    private AuthorRepository authorRepository;
//    @Autowired
//    private ArticleAuthorsRepository articleAuthorsRepository;

    @CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("getTable/{tableName}")
    public ReturnTables findTable(@PathVariable String tableName) {
        ReturnTables returnTables = new ReturnTables();
        returnTables.setFields(allRepository.findAllTableFields(tableName));
        returnTables.setValues(allRepository.findAllTable(tableName));
        return returnTables;
    }

    @GetMapping("getAllTables")
    public List<String> findAllAvailableTables() {
        return allRepository.findAllAvailableTable();
    }

    @Transactional
    @PostMapping("create/article")
    public boolean createTable(@RequestBody Article article) {
        System.out.println(article.getPages());
        System.out.println("authors 是：" + article.getAuthors());
        articleRepository.save(article);
        for(Author author:article.getAuthors()){
            author.getArticles().add(article);
        }
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
        /**
         * Based on the formula : s = Sum*(1-2.5*DC/100)
         * define the X
         * the total purchase value of items by customer in the last five years

         *  x>=500: Dc = 5
         *  400<=x<500: Dc = 4
         *  300<=x<400: Dc = 3
         *  200<=x<300: Dc = 2
         *  100<=x<200: Dc = 1
         *  x<100: Dc = 0
         */

        // 1. get all transaction price
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.YEAR, -5);
        Date yearago5 = cal.getTime();
        System.out.println(yearago5.getTime());
        List<Transaction> results = transactionRepository.findByCustomerIdAndTransactionDateAfter(
                transactionInfo.getCustomerId(), yearago5);
        // TODO use java8 foreach to simplify the code
        int price_all = 0;
        int dicount_code = 0;
        for (Transaction item : results) {
            price_all += item.getTotalPurchasePrice();
        }

        // Java switch doesn't support condition sentence
        // Certainly you can also extract the following login into utils function
        if (price_all < 100) {
            dicount_code = 0;
        } else if (price_all >= 100 && price_all < 200) {
            dicount_code = 1;
        } else if (price_all >= 200 && price_all < 300) {
            dicount_code = 2;
        } else if (price_all >= 300 && price_all < 400) {
            dicount_code = 3;
        } else if (price_all >= 400 && price_all < 500) {
            dicount_code = 4;
        } else if (price_all >= 500) {
            dicount_code = 5;
        }
        System.out.println("Currently discount_code is : " + dicount_code);


        Transaction transaction = new Transaction();
        transaction.setCustomerId(transactionInfo.getCustomerId());
        transaction.setTotalPurchasePrice((float) (doublePrice * (1 - 2.5 * dicount_code / 100)));
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
        transactionItemsRepository.saveAll(transactionItemsList);


        return true;
    }

    @Transactional
    @DeleteMapping("cancel/transaction/{transactionNumber}")
    public List<Transaction> cancelTransaction(@PathVariable int transactionNumber) {
        // should check whether transactionNumber exists
        Date today = new Date();
        //this line is supposedly to get the date that is 30 days ago
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date today30 = cal.getTime();
        System.out.println("30 days ago is ：");
        System.out.println(today30.getTime());


        transactionRepository.deleteByTransactionNumber(transactionNumber);
        transactionItemsRepository.deleteByTransactionNumber(transactionNumber);

        // provided the
        // transaction occurred no more than 30 days before the current day

        return transactionRepository.findByTransactionDateAfter(today30);
    }

}
