package com.myshop.repository.impl;

import com.myshop.model.Order;
import com.myshop.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override public void save(Order order) { if (order != null) orders.add(order); }

    @Override
    public Order[] findByCustomerId(int customerId) {
        List<Order> res = new ArrayList<>();
        for (Order o : orders) if (o.getCustomerId() == customerId) res.add(o);
        return res.toArray(new Order[0]);
    }

    @Override public Order[] findAll() { return orders.toArray(new Order[0]); }
    @Override public void clear() { orders.clear(); }
}
