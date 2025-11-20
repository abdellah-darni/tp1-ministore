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
        processMessages(request);

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

        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    private void processMessages(HttpServletRequest request) {
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if (success != null) {
            switch (success) {
                case "Deleted" -> request.setAttribute("success", "Product deleted successfully!");
                case "Updated" -> request.setAttribute("success", "Product updated successfully!");
                case "Added" -> request.setAttribute("success", "Product added successfully!");
                default -> request.setAttribute("success", success);
            }
        }

        if (error != null) {
            switch (error) {
                case "Deleted" -> request.setAttribute("error", "Product deletion Failed!");
                case "Updated" -> request.setAttribute("error", "Product update Failed!");
                case "Added" -> request.setAttribute("error", "Product addition Failed!");
                case "InvalidData" -> request.setAttribute("error", "Please check your input data (Name/Price required).");
                case "InvalidID" -> request.setAttribute("error", "Invalid Product ID.");
                default -> request.setAttribute("error", error);
            }
        }
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
        String nameParm = request.getParameter("name");
        String priceParm = request.getParameter("price");

        try {
            if (nameParm == null || nameParm.trim().isEmpty() || priceParm == null || priceParm.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            Product product = new Product(
                    nameParm,
                    request.getParameter("description"),
                    Double.parseDouble(priceParm)
            );

            boolean success = productDAO.insert(product);
            if (!success) {
                response.sendRedirect(request.getContextPath() + "/products?error=Added");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/products?success=Added");

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/products?action=ADD&error=InvalidData");
        }
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        String nameParm = request.getParameter("name");
        String priceParm = request.getParameter("price");

        try {
            if (idParm == null || idParm.trim().isEmpty() || nameParm == null || nameParm.trim().isEmpty() || priceParm == null || priceParm.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            Product product = new Product(
                    Integer.parseInt(idParm),
                    nameParm,
                    request.getParameter("description"),
                    Double.parseDouble(priceParm),
                    request.getParameter("createdAt")
            );

            boolean success = productDAO.update(product);

            if (!success) {
                response.sendRedirect(request.getContextPath() + "/products?error=Updated");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/products?success=Updated");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/products?action=EDIT&id=" + idParm + "&error=InvalidData");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");

        try {
            if (idParm == null || idParm.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            int id = Integer.parseInt(idParm);
            boolean success = productDAO.delete(id);
            if (!success) {
                response.sendRedirect(request.getContextPath() + "/products?error=Deleted");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/products?success=Deleted");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=InvalidData");
        }
    }

}
