<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="isEditMode" value="${not empty product}" />
    <c:if test="${!isEditMode}"><title>Add Product - MiniStore</title></c:if>
    <c:if test="${isEditMode}"><title>Edit Product - MiniStore</title></c:if>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --gradient-primary: linear-gradient(135deg, #89b4fa 0%, #94e2d5 100%);
            --text-dark: #4c4f69;
            --text-muted: #6c6f85;
        }

        body {
            background: #eff1f5;
            font-family: system-ui, -apple-system, sans-serif;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* --- NAVBAR STYLES --- */
        .navbar {
            background: white;
            border-bottom: none;
            box-shadow: 0 4px 20px rgba(137, 180, 250, 0.15);
            padding: 1rem 0;
        }

        .navbar-brand {
            font-weight: 800;
            font-size: 1.5rem;
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .nav-link {
            color: var(--text-dark) !important;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .nav-link:hover {
            color: #89b4fa !important;
            transform: translateY(-1px);
        }

        .main-content {
            flex-grow: 1;
            display: flex;
            align-items: center;
            padding-top: 1rem;
            padding-bottom: 2rem;
        }

        .form-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(137, 180, 250, 0.15);
            padding: 2rem;
            border: none;
            max-width: 700px;
            margin: 0 auto;
        }

        .form-title {
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            font-weight: 800;
            font-size: 1.8rem;
            margin-bottom: 0;
        }

        .form-label {
            font-weight: 600;
            color: #4c4f69;
            margin-bottom: 0.3rem;
            font-size: 0.85rem;
        }

        .input-wrapper {
            position: relative;
            margin-bottom: 1rem;
        }

        .form-control {
            border: 2px solid #dce0e8;
            border-radius: 10px;
            padding: 0.7rem 1rem;
            font-size: 0.9rem;
            transition: all 0.3s ease;
            background: #f5f5f5;
        }

        .form-control:focus {
            border-color: #89b4fa;
            box-shadow: 0 0 0 3px rgba(137, 180, 250, 0.1);
            background: white;
            outline: none;
        }

        .input-icon {
            position: absolute;
            right: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca0b0;
            transition: all 0.3s ease;
            pointer-events: none;
            font-size: 0.9rem;
        }

        textarea ~ .input-icon {
            top: 20px;
            transform: none;
        }

        .form-control:focus ~ .input-icon {
            color: #89b4fa;
        }

        .btn-submit {
            background: var(--gradient-primary);
            border: none;
            border-radius: 10px;
            padding: 0.8rem;
            font-weight: 600;
            color: white;
            text-transform: uppercase;
            font-size: 0.9rem;
            letter-spacing: 1px;
            box-shadow: 0 4px 15px rgba(137, 180, 250, 0.3);
            transition: all 0.3s ease;
            width: 100%;
        }

        .btn-submit:hover {
            transform: translateY(-1px);
            box-shadow: 0 8px 20px rgba(137, 180, 250, 0.4);
            color: white;
        }

        .btn-cancel {
            background: #eff1f5;
            color: #6c6f85;
            border: none;
            border-radius: 10px;
            padding: 0.8rem;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.3s;
            width: 100%;
            display: block;
            text-align: center;
            text-decoration: none;
        }

        .btn-cancel:hover { background: #dce0e8; color: #4c4f69; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container-fluid px-4">
        <a class="navbar-brand text-theme-primary fw-bold" href="#">
            <i class="fas fa-store me-2"></i>MiniStore
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link text-theme-dark" href="${pageContext.request.contextPath}/products">
                        <i class="fas fa-box me-1"></i>Products
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-theme-dark" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt me-1"></i>Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="main-content">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-10 col-lg-8">
                <div class="card form-card">

                    <div class="d-flex align-items-center mb-4 pb-3 border-bottom border-light">
                        <div class="d-inline-flex align-items-center justify-content-center rounded-circle me-3"
                             style="width: 50px; height: 50px; background: rgba(137, 180, 250, 0.1);">
                            <i class="fas fa-plus" style="color: #89b4fa; font-size: 1.2rem;"></i>
                        </div>
                        <div>
                            <h2 class="form-title">
                                <c:if test="${isEditMode}">Edit Product</c:if>
                                <c:if test="${!isEditMode}">Add New Product</c:if>
                            </h2>
                            <p class="text-muted small mb-0">
                                <c:if test="${isEditMode}">Update product details</c:if>
                                <c:if test="${!isEditMode}">Create new inventory item</c:if>
                            </p>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/products" method="post">

                        <input type="hidden" name="id" value="${product.id}">
                        <input type="hidden" name="createdAt" value="${product.createdAt}">

                        <div class="row g-3">
                            <div class="col-md-7">
                                <div class="input-wrapper">
                                    <label class="form-label">Product Name</label>
                                    <input type="text" name="name" value="${product.name}" class="form-control" placeholder="e.g. Wireless Mouse" required>
                                    <i class="fas fa-tag input-icon"></i>
                                </div>
                            </div>

                            <div class="col-md-5">
                                <div class="input-wrapper">
                                    <label class="form-label">Price ($)</label>
                                    <input type="number" name="price" value="${product.price}" class="form-control" step="0.01" min="0" placeholder="0.00" required>
                                    <i class="fas fa-dollar-sign input-icon"></i>
                                </div>
                            </div>
                        </div>

                        <div class="input-wrapper">
                            <label class="form-label">Description</label>
                            <textarea name="description" class="form-control" rows="2" placeholder="Enter product details...">${product.description}</textarea>
                            <i class="fas fa-align-left input-icon"></i>
                        </div>

                        <div class="row g-3 mt-2">
                            <div class="col-6">
                                <a href="${pageContext.request.contextPath}/products" class="btn-cancel">Cancel</a>
                            </div>
                            <div class="col-6">
                                <button type="submit" class="btn-submit">Save Product</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>