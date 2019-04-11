package com.mcda.database.project.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "author")
@NoArgsConstructor
@RequiredArgsConstructor
public class Author {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore

    private String lname = "";

    @JsonIgnore
    private String fname = "";

    @JsonIgnore
    private String email = "";


}
