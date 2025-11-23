package com.abdellah.tp1ministore.servlet;

import com.abdellah.tp1ministore.dao.ProductDAO;
import com.abdellah.tp1ministore.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = {"/products", "/products/*"})
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if(true) throw new RuntimeException("Test Error");

        processMessages(request);

        String path = request.getPathInfo();
        logger.trace("doGet called. Path: {}", path);

        if (path == null || path.equals("/")) {
            listProducts(request, response);
        } else if (path.equals("/new")) {
            request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
        } else if (path.equals("/edit")) {
            showEditForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        logger.trace("doPost called. Path: {}", path);

        if (path == null || path.equals("/")) {
            createProduct(request, response);
        } else if (path.equals("/update")) {
            updateProduct(request, response);
        } else if (path.equals("/delete")) {
            deleteProduct(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        logger.trace("Loaded {} products.", products.size());
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");

        try {
            if (idParm == null) throw new NumberFormatException();
            int id = Integer.parseInt(idParm);
            logger.debug("Fetching product for Edit. ID: {}", id);

            Product product = productDAO.getProductById(id);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.error("Invalid ID passed to Edit: {}", idParm);
            response.sendRedirect(request.getContextPath() + "/products?error=InvalidID");
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Handling creating a New Product...");
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
                logger.error("Product ADD failed (DAO returned false)");
                response.sendRedirect(request.getContextPath() + "/products?error=Added");
                return;
            }
            logger.info("Product ADDED successfully: {}", product.getName());
            response.sendRedirect(request.getContextPath() + "/products?success=Added");

        } catch (IllegalArgumentException e) {
            logger.error("ADD Error", e);
            response.sendRedirect(request.getContextPath() + "/products/new?error=InvalidData");
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        logger.debug("Handling EDIT Product. ID: {}", idParm);
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
                logger.error("Product UPDATE failed: ID {}", idParm);
                response.sendRedirect(request.getContextPath() + "/products?error=Updated");
                return;
            }
            logger.info("Product UPDATED: ID {}", idParm);
            response.sendRedirect(request.getContextPath() + "/products?success=Updated");
        } catch (IllegalArgumentException e) {
            logger.error("Edit Error", e);
            response.sendRedirect(request.getContextPath() + "/products/edit?id=" + idParm + "&error=InvalidData");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        logger.info("Handling DELETE Product. ID: {}", idParm);

        try {
            if (idParm == null || idParm.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            int id = Integer.parseInt(idParm);
            boolean success = productDAO.delete(id);
            if (!success) {
                logger.error("Product DELETE failed: ID {}", id);
                response.sendRedirect(request.getContextPath() + "/products?error=Deleted");
                return;
            }
            logger.info("Product DELETED: ID {}", id);
            response.sendRedirect(request.getContextPath() + "/products?success=Deleted");
        } catch (IllegalArgumentException e) {
            logger.error("Delete Error", e);
            response.sendRedirect(request.getContextPath() + "/products?error=InvalidData");
        }
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

}
