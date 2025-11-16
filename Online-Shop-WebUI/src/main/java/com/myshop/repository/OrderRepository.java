package com.myshop.repository;

import com.myshop.model.Order;

public interface OrderRepository {
    void save(Order order);
    Order[] findByCustomerId(int customerId);
    Order[] findAll();
    void clear();
}
