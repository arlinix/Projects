package com.myshop.service.impl;

import com.myshop.model.Order;
import com.myshop.service.OrderManagementService;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementServiceImpl implements OrderManagementService {

    private static OrderManagementServiceImpl instance;
    private final List<Order> orders = new ArrayList<>();

    OrderManagementServiceImpl() {}

    public static synchronized OrderManagementServiceImpl getInstance() {
        if (instance == null) {
            instance = new OrderManagementServiceImpl();
        }
        return instance;

    }

    @Override
    public synchronized void addOrder(Order order) {
        if (order != null) orders.add(order);
    }

    @Override
    public Order[] getOrdersByUserId(int userId) {
        List<Order> resOrders = new ArrayList<>();
        for (Order o : orders){
            if(o.getCustomerId() == userId){
                resOrders.add(o);
            }
        }
            return resOrders.toArray(new Order[0]);
    }


    @Override
    public Order[] getOrders() {
        return orders.toArray(new Order[0]);
    }

    public void clearServiceState() {
        this.orders.clear();
    }
}
