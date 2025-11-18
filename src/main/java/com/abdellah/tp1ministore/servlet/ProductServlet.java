package com.abdellah.tp1ministore.servlet;


import com.abdellah.tp1ministore.dao.ProductDAO;
import com.abdellah.tp1ministore.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("ADD".equals(action)) {
            System.out.println("add method");
            request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
        }

        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if ("WDeleted".equals(success)) {
            request.setAttribute("success", "Product deleted successfully!");
        }

        if ("FDeleted".equals(error)) {
            request.setAttribute("error", "Product deleted Failed!");
        }


        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        System.out.println("Method: " + method);

        if ("DELETE".equals(method)) {
            handleDelete(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");

        if (idString == null) {
            response.sendRedirect(request.getContextPath() + "/products?error=MissingID");
            return;
        }

        int id = Integer.parseInt(idString);
        boolean success =  productDAO.delete(id);

        if (!success) {
            response.sendRedirect(request.getContextPath() + "/products?error=FDeleted");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?success=WDeleted");
    }

}
