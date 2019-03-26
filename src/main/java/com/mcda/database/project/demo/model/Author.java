package com.mcda.database.project.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AUTHOR")
@NoArgsConstructor
@RequiredArgsConstructor
public class Author {

    @Id
    @NonNull
    private int id;

    @JsonIgnore

    private String lname = "";

    @JsonIgnore
    private String fname = "";

    @JsonIgnore
    private String email = "";

}
