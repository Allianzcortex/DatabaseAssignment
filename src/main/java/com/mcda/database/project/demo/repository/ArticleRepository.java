package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

}
