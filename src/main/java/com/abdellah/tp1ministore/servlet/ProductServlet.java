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
        String idParm = request.getParameter("id");

        if ("ADD".equals(action) || "EDIT".equals(action)) {
            if ("EDIT".equals(action)) {
                try {
                    if (idParm == null) throw new NumberFormatException();
                    int id = Integer.parseInt(idParm);
                    Product product = productDAO.getProductById(id);
                    request.setAttribute("product", product);
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/products?error=InvalidID");
                    return;
                }
            }
            request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
            return;
        }

        String success = request.getParameter("success");
        String error = request.getParameter("error");

        switch (success) {
            case "Deleted" -> request.setAttribute("success", "Product deleted successfully!");
            case "Updated" -> request.setAttribute("success", "Product updated successfully!");
            case "Added" -> request.setAttribute("success", "Product added successfully!");
            case null, default -> request.setAttribute("success", success);
        }

        switch (error) {
            case "Deleted" -> request.setAttribute("error", "Product deleted Failed!");
            case "Updated" -> request.setAttribute("error", "Product updated Failed!");
            case "Added" -> request.setAttribute("error", "Product added Failed!");
            case null, default -> request.setAttribute("error", error);
        }


        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("DELETE".equals(method)) {
            handleDelete(request, response);
            return;
        }

        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            handleAdd(request, response);
            return;
        }
        handleEdit(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = new Product(
                request.getParameter("name"),
                request.getParameter("description"),
                Double.parseDouble(request.getParameter("price"))
        );

        boolean success = productDAO.insert(product);
        if (!success) {
            response.sendRedirect(request.getContextPath() + "/products?error=Added");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?success=Added");
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = new Product(
                Integer.parseInt(request.getParameter("id")),
                request.getParameter("name"),
                request.getParameter("description"),
                Double.parseDouble(request.getParameter("price")),
                request.getParameter("createdAt")
        );

        boolean success = productDAO.update(product);

        if (!success) {
            response.sendRedirect(request.getContextPath() + "/products?error=Updated");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?success=Updated");
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
            response.sendRedirect(request.getContextPath() + "/products?error=Deleted");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products?success=Deleted");
    }

}
