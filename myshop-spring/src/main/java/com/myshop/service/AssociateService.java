
package com.myshop.service;

import com.myshop.model.Order;

public interface AssociateService {
    Order[] listAllOrders() throws Exception;
    boolean processOrder(int orderId, int associateUserId) throws Exception;   // PLACED -> PROCESSING
    boolean cancelOrder(int orderId, int associateUserId, String reason) throws Exception;
}
