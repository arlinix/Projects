
package com.myshop.repository;

import com.myshop.model.PaymentMethod;
import java.sql.Connection;

public interface PaymentMethodRepository {
    int upsertDefault(Connection conn, PaymentMethod pm) throws Exception;
    PaymentMethod findDefaultByUserId(Connection conn, int userId) throws Exception;
}
