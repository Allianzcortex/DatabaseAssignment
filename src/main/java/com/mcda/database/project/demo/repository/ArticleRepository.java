package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
}
