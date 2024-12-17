package org.hotels.dao;

public interface CustomerDAO extends GeneralDAO{
    int createCustomer(String email, String name, String phoneNumber);
}
