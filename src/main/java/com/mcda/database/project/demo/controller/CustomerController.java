package com.mcda.database.project.demo.controller;

import com.mcda.database.project.demo.dto.ReturnTables;
import com.mcda.database.project.demo.dto.TransactionInfo;
import com.mcda.database.project.demo.model.*;
import com.mcda.database.project.demo.repository.*;
import com.mcda.database.project.demo.utils.TransactionUtils;
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
    @Autowired
    private ArticleAuthorsRepository articleAuthorsRepository;

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
    public int createTable(@RequestBody Article article) {

        // check constraints , the best practice is to use controlleradvice to handle exception
        // but this way is faster......
        if (
                articleRepository.
                        findByMagazineIdAndVolume(article.getMagazineId(), article.getVolume()).size() != 0) {
            if (articleRepository.
                    findByMagazineIdAndVolume(article.getMagazineId(), article.getVolume()).get(0).getVolumeNumber()
                    != article.getVolumeNumber()) {
                return -1;
            }

            if (!articleRepository.
                    findByMagazineIdAndVolume(article.getMagazineId(), article.getVolume()).get(0).getPublicationYear()
                    .equals(article.getPublicationYear())) {
                return -2;
            }
        }


        // save tag first, save article then
        System.out.println(article.getPages());
        System.out.println(article.getAuthor());
        List<Integer> temp = new ArrayList<>();
        for (String authorName : article.getAuthor().split(";")) {
            System.out.println(authorName);
            String lname = authorName.split(":")[0];
            String fname = authorName.split(":")[1];
            String email = authorName.split(":")[2];
            Author author = new Author(lname, fname, email);
            Author tempAuthor = authorRepository.saveAndFlush(author);
            temp.add(tempAuthor.getId());
        }
        Article tempArticle = articleRepository.saveAndFlush(article);
        int articleId = tempArticle.getId();
        for (int i = 0; i < temp.size(); i++) {
            ArticleAuthors aa = new ArticleAuthors(articleId, temp.get(i));
            articleAuthorsRepository.save(aa);
        }

        return 200;
    }

    @Transactional
    @PostMapping("create/customer")
    public boolean createCustomer(@RequestBody Customer customer, @RequestParam boolean checkExists) throws Exception {
        System.out.println("check whether exists " + checkExists);
        if (checkExists && customerRepository.existsByFirstNameAndLastName(customer.getFirstName(), customer.getLastName())) {
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

        // 1. get discount_code

        int discount_code = TransactionUtils.getDiscountCode(transactionInfo.getCustomerId(), transactionRepository);
        Transaction transaction = new Transaction();
        transaction.setCustomerId(transactionInfo.getCustomerId());
        transaction.setTotalPurchasePrice((float) (doublePrice * (1 - 2.5 * discount_code / 100)));
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
        System.out.println(transactionItemsList);
        transactionItemsRepository.saveAll(transactionItemsList);

        // update discount code
        Customer currentCustomer = customerRepository.findById(transactionInfo.getCustomerId()).get();
        currentCustomer.setDiscountCode(TransactionUtils.getDiscountCode(transactionInfo.getCustomerId(), transactionRepository));

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
        // customerId should be found before the transaction is deleted
        int customerId = transactionRepository.findByTransactionNumber(transactionNumber).get().getCustomerId();

        transactionRepository.deleteByTransactionNumber(transactionNumber);
        transactionItemsRepository.deleteByTransactionNumber(transactionNumber);

        // update the discountCode

        customerRepository.findById(customerId).get().setDiscountCode(TransactionUtils.getDiscountCode(customerId, transactionRepository));

        // provided the
        // transaction occurred no more than 30 days before the current day

        return transactionRepository.findByTransactionDateAfter(today30);
    }

}
