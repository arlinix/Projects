
package com.myshop.service;

import com.myshop.model.PaymentMethod;

public interface PaymentService {
    int saveOrUpdateDefaultPaymentMethod(int userId, String brand, String pan, int expMonth, int expYear) throws Exception;
    PaymentMethod getDefaultPaymentMethod(int userId) throws Exception;
}
