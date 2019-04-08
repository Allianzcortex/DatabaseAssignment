package com.mcda.database.project.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Article")
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Article_ID")
    private int id;

    @NotNull
    private String title;

    @NotNull
    @Column(name = "magazineId")
    private int magazineId;

    @NotNull
    @Column(name = "volume_number")
    private int volumeNumber;

    @NotNull
    @Column(name = "page_number")
    private int pages;

    @Column(name = "publication_year")
    private String publicationYear = null;

    @ManyToMany(cascade = {CascadeType.ALL})
    // https://stackoverflow.com/questions/8624633/org-hibernate-mappingexception-unable-to-find-column-with-logical-name

    @JoinTable(name = "article_authors",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<Author>();

}
