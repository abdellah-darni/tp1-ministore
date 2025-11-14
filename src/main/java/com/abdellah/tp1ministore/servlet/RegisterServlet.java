package com.abdellah.tp1ministore.servlet;

import com.abdellah.tp1ministore.dao.UserDAO;
import com.abdellah.tp1ministore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="registerServlet", value="/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req,resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String username = req.getParameter("userName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        

        User user = new User(username, email, password);

        boolean success = userDAO.registerUser(user);

        if(success){
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("error", "Registration failed. Maybe email already exists?");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }

    }
}
