
package com.myshop.service;

public interface AdminService {
    // Products
    int createProduct(String name, String category, double price, int stock) throws Exception;
    boolean updateProduct(int id, String name, String category, double price, int stock, boolean active) throws Exception;
    boolean deleteProduct(int id) throws Exception; // soft or hard delete depending on impl

    // Orders
    boolean processOrder(int orderId, int adminUserId) throws Exception;      // PLACED -> PROCESSING
    boolean shipOrder(int orderId, int adminUserId) throws Exception;         // PROCESSING -> SHIPPED
    boolean cancelOrder(int orderId, int adminUserId, String reason) throws Exception;

    // Users
    boolean resetUserEmail(int targetUserId, String newEmail, int adminUserId) throws Exception;
    boolean resetUserPassword(int targetUserId, String newTempPassword, int adminUserId) throws Exception;
}
