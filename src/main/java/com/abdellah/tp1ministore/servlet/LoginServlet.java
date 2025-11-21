package com.abdellah.tp1ministore.servlet;


import com.abdellah.tp1ministore.dao.UserDAO;
import com.abdellah.tp1ministore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init(){
        userDAO = new UserDAO();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String logout = req.getParameter("logout");
        HttpSession session = req.getSession(false);

        if ("true".equals(logout)) {
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?success=LoggedOut");
            return;
        }

        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        String success = req.getParameter("success");
        if ("LoggedOut".equals(success)) {
            req.setAttribute("success", "You have been logged out successfully.");
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req,resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String submittedPlainPassword = req.getParameter("password");

        User user  = userDAO.getUserByEmail(email);

        if (user == null){
            req.setAttribute("error", "No user found");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req,resp);
            return;
        }

        boolean validPassword = userDAO.verifyPassword(submittedPlainPassword, user.getPasswordHash());

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
