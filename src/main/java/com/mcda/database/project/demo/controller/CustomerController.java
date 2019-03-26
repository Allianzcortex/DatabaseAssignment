package com.mcda.database.project.demo.controller;

import com.mcda.database.project.demo.dto.ReturnTables;
import com.mcda.database.project.demo.model.Article;
import com.mcda.database.project.demo.repository.AllRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class CustomerController {

    @Autowired
    private AllRepository allRepository;

    @GetMapping("getTable/{tableName}")
    public ReturnTables findTable(@PathVariable String tableName) {
        ReturnTables returnTables = new ReturnTables();
        returnTables.setFields(allRepository.findAllTableFields(tableName));
        returnTables.setValues(allRepository.findAllTable(tableName));
        return returnTables;
    }

    @PostMapping("create/article")
    public boolean createTable(@RequestBody Article article) {
        System.out.println(article.getPages());
        System.out.println(article.getAuthors());
        return true;
    }

}
