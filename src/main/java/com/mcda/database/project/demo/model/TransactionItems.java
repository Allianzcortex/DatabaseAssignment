package com.mcda.database.project.demo.model;

import javax.persistence.Column;
import javax.persistence.Id;

public class TransactionItems {
    @Id
    @Column(name = "Transaction_number")
    private int transactionNumber;

    @Column(name = "Item_ID")
    private int itemId;

    @Column(name = "price")
    private float price;
}
