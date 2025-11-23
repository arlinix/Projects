
package com.myshop.repository;

import com.myshop.model.Notification;
import java.sql.Connection;
import java.util.List;

public interface NotificationRepository {
    int add(Connection conn, Notification n) throws Exception;
    List<Notification> listUnreadByUser(Connection conn, int userId) throws Exception;
    int markAllRead(Connection conn, int userId) throws Exception;
}
