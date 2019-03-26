package com.mcda.database.project.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "Transaction")
@NoArgsConstructor
public class Transaction {

    @Id
    private int transactionNumber;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "total_purchase_price")
    private float totalPurchasePrice;

    @Column(name = "customer_id")
    private int customerId;


}
