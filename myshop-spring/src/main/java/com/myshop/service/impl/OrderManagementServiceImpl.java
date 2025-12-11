package com.myshop.service.impl;

import com.myshop.db.DbConnectionFactory;
import com.myshop.model.DefaultOrder;
import com.myshop.repository.OrderRepository;
import com.myshop.service.OrderManagementService;

import java.sql.Connection;
import java.util.List;

public class OrderManagementServiceImpl implements OrderManagementService {

    private final OrderRepository orderRepository;

    public OrderManagementServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(com.myshop.model.Order order) {
        // not used - placing order uses OrderService (transactional), keep no-op
        throw new UnsupportedOperationException("Use OrderService.placeOrder for transactional order placement");
    }

    @Override
    public com.myshop.model.Order[] getOrdersByUserId(int userId) {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            List<DefaultOrder> list = orderRepository.findOrdersByUserId(conn, userId);
            return list.toArray(new com.myshop.model.Order[0]);
        } catch (Exception e) {
            return new com.myshop.model.Order[0];
        }
    }

    @Override
    public com.myshop.model.Order[] getOrders() {
        // For brevity, not implemented; could query all orders similarly
        return new com.myshop.model.Order[0];
    }

    @Override
    public void clearServiceState() {
        // no-op
    }
}