package com.abdellah.tp1ministore.dao;

import com.abdellah.tp1ministore.model.User;
import com.abdellah.tp1ministore.util.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(User user){
        logger.debug("Attempting to register user: {}", user.getEmail());

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
                    logger.info("User registered successfully: {}", user.getEmail());
                }
            }
        } catch (Exception e) {
            logger.error("Registration Transaction Failed", e);

            if (conn != null){
                try{
                    conn.rollback();
                } catch (Exception rollbackerr){
                    logger.error("Rollback Transaction Failed", rollbackerr);
                }
            }
        } finally {
            if (conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception closeerr){
                    logger.error("Connection close failed", closeerr);
                }
            }
        }
        return success;
    }

    public boolean verifyPassword(String submittedPlainPassword, String storedHash) {
        boolean match = passwordEncoder.matches(submittedPlainPassword, storedHash);
        if(!match) {
            logger.warn("Password verification failed");
        }
        return match;
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
            logger.error("getUserByEmail failed", e);
        }
        return null;
    }


}
