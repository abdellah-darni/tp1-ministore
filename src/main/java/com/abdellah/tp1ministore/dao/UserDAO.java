package com.abdellah.tp1ministore.dao;

import com.abdellah.tp1ministore.model.User;
import com.abdellah.tp1ministore.util.Database;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(User user){
        System.out.println("registerUser function line : 17  \nenterd password: "+ user.getPasswordHash());
                String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        System.out.println("registerUser function line : 21  \nhashed password: " + hashedPassword);

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

                System.out.println("regesterUser function: line  \nusername = " + user.getUserName() + "\nemail = " + user.getEmail()+"\npassword_hash = " + user.getPasswordHash());

                int affectedRow = stmt.executeUpdate();

                System.out.println("regestraionuser function : line 41 \naffectedRow = " + affectedRow);

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

    public boolean verifyPassword(String submittedPlainPassword, String storedHash) {
        return passwordEncoder.matches(submittedPlainPassword, storedHash);
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("created_at")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
