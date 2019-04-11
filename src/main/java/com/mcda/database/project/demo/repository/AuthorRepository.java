package com.mcda.database.project.demo.repository;

import com.mcda.database.project.demo.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author,Integer> {
}
