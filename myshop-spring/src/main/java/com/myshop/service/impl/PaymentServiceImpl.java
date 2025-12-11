
package com.myshop.service.impl;

import com.myshop.db.DbConnectionFactory;
import com.myshop.model.PaymentMethod;
import com.myshop.repository.PaymentMethodRepository;
import com.myshop.repository.jdbc.PaymentMethodRepositoryJdbcImpl;

import java.security.MessageDigest;
import java.sql.Connection;

public class PaymentServiceImpl implements com.myshop.service.PaymentService {
    private final PaymentMethodRepository repo = new PaymentMethodRepositoryJdbcImpl();

    @Override
    public int saveOrUpdateDefaultPaymentMethod(int userId, String brand, String pan, int expMonth, int expYear) throws Exception {
        String last4 = pan.substring(Math.max(0, pan.length()-4));
        String token = tokenize(pan, userId); // opaque token derived; replace with gateway token when available

        PaymentMethod pm = new PaymentMethod();
        pm.setUserId(userId);
        pm.setBrand(brand);
        pm.setLast4(last4);
        pm.setExpMonth(expMonth);
        pm.setExpYear(expYear);
        pm.setToken(token);
        pm.setDefault(true);

        try (Connection conn = DbConnectionFactory.getConnection()) {
            return repo.upsertDefault(conn, pm);
        }
    }

    @Override
    public PaymentMethod getDefaultPaymentMethod(int userId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return repo.findDefaultByUserId(conn, userId);
        }
    }

    // Simple local tokenization (replace with gateway-issued token in real world)
    private String tokenize(String pan, int userId) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String salt = "pm_salt_" + userId; // per-user salt
        byte[] digest = md.digest((salt + ":" + pan).getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return "tok_" + sb;
    }
}
