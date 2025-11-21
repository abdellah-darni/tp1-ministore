package com.abdellah.tp1ministore.servlet;

import com.abdellah.tp1ministore.dao.UserDAO;
import com.abdellah.tp1ministore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name="registerServlet", value="/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req,resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Processing Registration POST request");

        String username = req.getParameter("userName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User(username, email, password);

        boolean success = userDAO.registerUser(user);

        if(success){
            logger.info("Registration SUCCESS for user: {}", email);
            resp.sendRedirect(req.getContextPath() + "/login?success=Registered");
        } else {
            logger.warn("Registration FAILED for user: {}", email);
            req.setAttribute("error", "Registration failed. Email might already exist.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}