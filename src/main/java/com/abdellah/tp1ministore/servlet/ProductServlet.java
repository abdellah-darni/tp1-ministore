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
import java.util.Enumeration;
import java.util.List;

@WebServlet(name = "ProductServlet", value = {"/products", "/products/*"})
public class ProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private ProductDAO productDAO;

    @Override
    public void init() {
        logger.info("Initializing ProductServlet...");
        productDAO = new ProductDAO();
        logger.info("ProductDAO initialized successfully.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if(true) throw new RuntimeException("Test Error");
        String path = request.getPathInfo();

        logger.info(">>> ENTERING doGet | URI: {} | PathInfo: {}", request.getRequestURI(), path);
        logRequestParams(request);

        processMessages(request);

        if (path == null || path.equals("/")) {
            logger.debug("Routing to: listProducts");
            listProducts(request, response);
        } else if (path.equals("/new")) {
            logger.debug("Routing to: Form (New)");
            request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
        } else if (path.equals("/edit")) {
            logger.debug("Routing to: Form (Edit)");
            showEditForm(request, response);
        } else {
            logger.warn("Unknown GET path requested: {}", path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        logger.info("<<< EXITING doGet");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        logger.info(">>> ENTERING doPost | URI: {} | PathInfo: {}", request.getRequestURI(), path);
        logRequestParams(request);

        if (path == null || path.equals("/")) {
            logger.debug("Action detected: CREATE Product");
            createProduct(request, response);
        } else if (path.equals("/update")) {
            logger.debug("Action detected: UPDATE Product");
            updateProduct(request, response);
        } else if (path.equals("/delete")) {
            logger.debug("Action detected: DELETE Product");
            deleteProduct(request, response);
        } else {
            logger.warn("Unknown POST path requested: {}", path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        logger.info("<<< EXITING doPost");
    }

    private void logRequestParams(HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            Enumeration<String> params = request.getParameterNames();
            StringBuilder sb = new StringBuilder("Request Params: { ");
            while (params.hasMoreElements()) {
                String name = params.nextElement();
                sb.append(name).append("='").append(request.getParameter(name)).append("' ");
            }
            sb.append("}");
            logger.debug(sb.toString());
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Fetching all products from DAO...");
        List<Product> products = productDAO.getAllProducts();
        logger.info("DAO returned {} products.", products.size());

        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        logger.debug("Attempting to load product for edit with raw ID: '{}'", idParm);

        try {
            if (idParm == null) throw new NumberFormatException();
            int id = Integer.parseInt(idParm);

            Product product = productDAO.getProductById(id);

            if (product != null) {
                logger.info("Product found: {} (ID: {})", product.getName(), product.getId());
                request.setAttribute("product", product);
                request.getRequestDispatcher("/WEB-INF/views/product/form.jsp").forward(request, response);
            } else {
                logger.warn("Product with ID {} not found in database.", id);
                response.sendRedirect(request.getContextPath() + "/products?error=InvalidID");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid ID passed to Edit: {}", idParm);
            response.sendRedirect(request.getContextPath() + "/products?error=InvalidID");
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameParm = request.getParameter("name");
        String priceParm = request.getParameter("price");

        logger.debug("Processing CREATE - Name: '{}', Price: '{}'", nameParm, priceParm);

        try {
            if (nameParm == null || nameParm.trim().isEmpty() || priceParm == null || priceParm.trim().isEmpty()) {
                logger.warn("Validation Failed: Name or Price is missing.");
                throw new IllegalArgumentException();
            }

            Product product = new Product(
                    nameParm,
                    request.getParameter("description"),
                    Double.parseDouble(priceParm)
            );

            logger.debug("Product object created: {}", product);

            boolean success = productDAO.insert(product);
            if (!success) {
                logger.error("DAO Insert Failed (returned false).");
                response.sendRedirect(request.getContextPath() + "/products?error=Added");
                return;
            }
            logger.info("DAO Insert Successful. New Product Name: {}", product.getName());
            response.sendRedirect(request.getContextPath() + "/products?success=Added");

        } catch (IllegalArgumentException e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/products/new?error=InvalidData");
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        String nameParm = request.getParameter("name");
        String priceParm = request.getParameter("price");

        logger.debug("Processing UPDATE - ID: '{}', Name: '{}'", idParm, nameParm);

        try {
            if (idParm == null || idParm.trim().isEmpty() || nameParm == null || nameParm.trim().isEmpty() || priceParm == null || priceParm.trim().isEmpty()) {
                logger.warn("Validation Failed: Name or Price is missing.");
                throw new IllegalArgumentException();
            }

            Product product = new Product(
                    Integer.parseInt(idParm),
                    nameParm,
                    request.getParameter("description"),
                    Double.parseDouble(priceParm),
                    request.getParameter("createdAt")
            );

            logger.debug("Product object prepared for update: {}", product);

            boolean success = productDAO.update(product);

            if (!success) {
                logger.error("Product UPDATE failed: ID {}", idParm);
                response.sendRedirect(request.getContextPath() + "/products?error=Updated");
                return;
            }
            logger.info("DAO Update Successful for ID: {}", idParm);
            response.sendRedirect(request.getContextPath() + "/products?success=Updated");
        } catch (IllegalArgumentException e) {
            logger.error("Exception during update for ID: {}", idParm, e);
            response.sendRedirect(request.getContextPath() + "/products/edit?id=" + idParm + "&error=InvalidData");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParm = request.getParameter("id");
        logger.debug("Processing DELETE - Raw ID: '{}'", idParm);

        try {
            if (idParm == null || idParm.trim().isEmpty()) {
                logger.warn("Delete failed: ID is missing.");
                throw new IllegalArgumentException();
            }

            int id = Integer.parseInt(idParm);
            boolean success = productDAO.delete(id);
            if (!success) {
                logger.error("DAO Delete Failed (returned false) for ID: {}", id);
                response.sendRedirect(request.getContextPath() + "/products?error=Deleted");
                return;
            }
            logger.info("DAO Delete Successful for ID: {}", id);
            response.sendRedirect(request.getContextPath() + "/products?success=Deleted");
        } catch (IllegalArgumentException e) {
            logger.error("Exception during delete logic", e);
            response.sendRedirect(request.getContextPath() + "/products?error=InvalidData");
        }
    }

    private void processMessages(HttpServletRequest request) {
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if (success != null) {
            logger.debug("Success message detected in URL: {}", success);
            switch (success) {
                case "Deleted" -> request.setAttribute("success", "Product deleted successfully!");
                case "Updated" -> request.setAttribute("success", "Product updated successfully!");
                case "Added" -> request.setAttribute("success", "Product added successfully!");
                default -> request.setAttribute("success", success);
            }
        }

        if (error != null) {
            logger.debug("Error message detected in URL: {}", error);
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
