package com.abdellah.tp1ministore.dao;

import com.abdellah.tp1ministore.model.User;
import com.abdellah.tp1ministore.util.Database;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(User user){
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        String query = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        boolean success= false;
        Connection conn = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(query)){

                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, hashedPassword);

                int affectedRow = stmt.executeUpdate();

                if (affectedRow == 1){
                    conn.commit();
                    success = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (conn != null){
                try{
                    conn.rollback();
                } catch (Exception rollbackerr){
                    rollbackerr.printStackTrace();
                }
            }
        } finally {
            if (conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeerr){
                    closeerr.printStackTrace();
                }
            }
        }
        return success;
    }


}
