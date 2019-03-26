package com.mcda.database.project.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionNumber;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "total_purchase_price")
    private float totalPurchasePrice;

    @Column(name = "customer_id")
    private int customerId;


}
