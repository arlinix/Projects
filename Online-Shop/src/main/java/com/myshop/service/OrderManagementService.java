package com.myshop.service;

import com.myshop.model.Order;

public interface OrderManagementService {

    void addOrder(Order order);

    Order[] getOrdersByUserId(int userId);

    Order[] getOrders();
}
