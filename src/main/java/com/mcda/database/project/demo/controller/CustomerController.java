package com.mcda.database.project.demo.controller;

import com.mcda.database.project.demo.dto.ReturnTables;
import com.mcda.database.project.demo.model.Article;
import com.mcda.database.project.demo.model.Customer;
import com.mcda.database.project.demo.repository.AllRepository;
import com.mcda.database.project.demo.repository.ArticleRepository;
import com.mcda.database.project.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

}
