package com.myshop.service.impl;

import com.myshop.model.Order;
import com.myshop.repository.OrderRepository;
import com.myshop.service.OrderManagementService;

public class OrderManagementServiceImpl implements OrderManagementService {

    private final OrderRepository orderRepository;

    public OrderManagementServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override public void addOrder(Order order) { orderRepository.save(order); }

    @Override public Order[] getOrdersByUserId(int userId) { return orderRepository.findByCustomerId(userId); }

    @Override public Order[] getOrders() { return orderRepository.findAll(); }

    @Override public void clearServiceState() { orderRepository.clear(); }
}
