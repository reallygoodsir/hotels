package org.hotels.dao;

import org.hotels.models.Customer;
import org.hotels.models.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public interface TransactionDAO extends GeneralDAO{

    void executeTransaction(Customer customer, Transaction transaction);
}
