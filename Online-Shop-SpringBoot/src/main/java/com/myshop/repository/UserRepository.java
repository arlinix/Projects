package com.myshop.repository;

import com.myshop.model.DefaultUser;

import java.sql.Connection;
import java.util.List;

public interface UserRepository {
    int save(Connection conn, DefaultUser user) throws Exception; // returns generated id
    DefaultUser findById(Connection conn, int id) throws Exception;
    DefaultUser findByEmail(Connection conn, String email) throws Exception;
    List<DefaultUser> findAll(Connection conn) throws Exception;
    boolean update(Connection conn, DefaultUser user) throws Exception;
    boolean delete(Connection conn, int id) throws Exception;
}