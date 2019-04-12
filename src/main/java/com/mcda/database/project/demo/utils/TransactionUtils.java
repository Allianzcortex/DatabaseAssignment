package com.mcda.database.project.demo.utils;

import com.mcda.database.project.demo.dto.TransactionInfo;
import com.mcda.database.project.demo.model.Customer;
import com.mcda.database.project.demo.model.Transaction;
import com.mcda.database.project.demo.repository.CustomerRepository;
import com.mcda.database.project.demo.repository.TransactionRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TransactionUtils {

    public static int getDiscountCode(int customerId, TransactionRepository transactionRepository) {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        // 2. get transactions nearly 5 years
        cal.add(Calendar.YEAR, -5);
        Date yearago5 = cal.getTime();
        System.out.println(yearago5.getTime());

        List<Transaction> results = transactionRepository.findByCustomerIdAndTransactionDateAfter(
                customerId, yearago5);
        // TODO use java8 foreach to simplify the code
        int price_all = 0;
        int discount_code = 0;
        for (Transaction item : results) {
            price_all += item.getTotalPurchasePrice();
        }

        // Java switch doesn't support condition sentence
        // Certainly you can also extract the following login into utils function
        if (price_all < 100) {
            discount_code = 0;
        } else if (price_all >= 100 && price_all < 200) {
            discount_code = 1;
        } else if (price_all >= 200 && price_all < 300) {
            discount_code = 2;
        } else if (price_all >= 300 && price_all < 400) {
            discount_code = 3;
        } else if (price_all >= 400 && price_all < 500) {
            discount_code = 4;
        } else if (price_all >= 500) {
            discount_code = 5;
        }
        System.out.println("Currently discount_code is : " + discount_code);
        return discount_code;


    }

}
