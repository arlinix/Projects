package com.myshop.repository;

import com.myshop.model.DefaultProduct;

import java.sql.Connection;
import java.util.List;

public interface ProductRepository {
    int save(Connection conn, DefaultProduct product) throws Exception; // returns generated id
    DefaultProduct findById(Connection conn, int id) throws Exception;
    List<DefaultProduct> findAll(Connection conn) throws Exception;
    boolean update(Connection conn, DefaultProduct product) throws Exception;
    boolean delete(Connection conn, int id) throws Exception;
    int count(Connection conn) throws Exception;
}