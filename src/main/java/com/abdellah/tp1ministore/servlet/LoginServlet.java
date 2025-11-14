package com.abdellah.tp1ministore.servlet;


import com.abdellah.tp1ministore.dao.UserDAO;
import com.abdellah.tp1ministore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init(){
        userDAO = new UserDAO();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req,resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        System.out.println("username: " + email);
        String submittedPlainPassword = req.getParameter("password");

        User user  = userDAO.getUserByEmail(email);

        if (user == null){
            System.out.println("we did not foud the user");
            req.setAttribute("error", "No user found");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req,resp);
            return;
        }
        System.out.println("we find the user");
        boolean validPassword = userDAO.verifyPassword(submittedPlainPassword, user.getPasswordHash());
        System.out.println("password not hashed :  " + submittedPlainPassword);
        System.out.println("password in db :" + user.getPasswordHash());
        System.out.println("password boolean :"+validPassword);

        if (validPassword){
            req.getSession().setAttribute("user", user);
            req.getSession().setMaxInactiveInterval(60);
            resp.sendRedirect("products");

        } else {
            req.setAttribute("error", "Password incorrect");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req,resp);
        }
    }

}
