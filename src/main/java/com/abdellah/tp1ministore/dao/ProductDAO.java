package com.abdellah.tp1ministore.dao;

import com.abdellah.tp1ministore.model.Product;
import com.abdellah.tp1ministore.util.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String query = "SELECT * FROM products";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)){

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("created_at")
                ));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return products;
    }
}
