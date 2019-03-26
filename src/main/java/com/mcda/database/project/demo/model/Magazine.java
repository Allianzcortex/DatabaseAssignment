package com.mcda.database.project.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MAGAZINE")
@NoArgsConstructor
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    private String name;
}
