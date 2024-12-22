package org.hotels.dao;

import org.hotels.models.Customer;
import org.hotels.models.Transaction;

public interface TransactionDAO extends GeneralDAO{

    String executeTransaction(Customer customer, Transaction transaction);
}
