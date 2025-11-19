package com.abdellah.tp1ministore.dao;

import com.abdellah.tp1ministore.model.Product;
import com.abdellah.tp1ministore.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()){

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

    public Product getProductById(int id) {
        String query = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);){

            stmt.setInt(1,id);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("created_at")
                    );
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Product product){
        String query = "INSERT INTO products (name, description, price) VALUES(?, ?, ?)";
        boolean success = false;
        Connection conn = null;

        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query);) {

                stmt.setString(1, product.getName());
                stmt.setString(2, product.getDescription());
                stmt.setDouble(3, product.getPrice());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 1) {
                    conn.commit();
                    success = true;
                }
            }

        } catch (Exception e){
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackerr) {
                    rollbackerr.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeerr) {
                    closeerr.printStackTrace();
                }
            }
        }
        return success;
    }

    public boolean update(Product product){
        String query = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
        boolean success = false;
        Connection conn = null;

        try {
            conn = Database.getConnection();

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setString(1, product.getName());
                stmt.setString(2, product.getDescription());
                stmt.setDouble(3, product.getPrice());

                stmt.setInt(4, product.getId());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 1) {
                    conn.commit();
                    success = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackerr) {
                    rollbackerr.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeerr) {
                    closeerr.printStackTrace();
                }
            }
        }
        return success;
    }

    public boolean delete(int id){
        String query = "DELETE FROM products WHERE id = ?";
        boolean success = false;
        Connection conn = null;

        try{
            conn = Database.getConnection();

            conn.setAutoCommit(false);

            try(PreparedStatement stmt = conn.prepareStatement(query);){
                stmt.setInt(1, id);

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 1) {
                    conn.commit();
                    success = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackerr) {
                    rollbackerr.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeerr) {
                    closeerr.printStackTrace();
                }
            }
        }
        return success;
    }
}
