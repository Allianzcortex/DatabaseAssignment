package com.mcda.database.project.demo.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "customer")
@RequiredArgsConstructor
// why add the follwing noArgsConstcutor and it will work ?
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "CID")
    private int Id;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "telephone_number")
    @NonNull
    private String telephoneNumber;

    @Column(name = "mailing_address")
    @NonNull
    private String mailingAddress;


    @NonNull
    @Column(name = "discount_code")
    private int discountCode;
}
