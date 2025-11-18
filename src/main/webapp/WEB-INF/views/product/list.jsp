<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - MiniStore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-theme-light">
<!-- Navbar -->
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

<div class="container-fluid px-4 py-4">
    <!-- Header with Add Button -->
    <div class="row mb-4">
        <div class="col-md-6">
            <h2 class="text-theme-primary fw-bold mb-1">
                <i class="fas fa-box-open me-2"></i>Products
            </h2>
            <p class="text-theme-dark">Manage your product inventory</p>
        </div>
        <div class="col-md-6 text-end">
            <a href="${pageContext.request.contextPath}/products?action=ADD" class="btn btn-theme btn-lg">
                <i class="fas fa-plus me-2"></i>Add New Product
            </a>
        </div>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i>${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Products Grid -->
    <div class="row">
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach var="p" items="${products}">
                    <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
                        <div class="card h-100 product-card">
                            <div class="card-body d-flex flex-column">
                                <!-- Product Icon/Image Placeholder -->
                                <div class="product-icon-wrapper text-center mb-3">
                                    <div class="product-icon">
                                        <i class="fas fa-box-open fa-3x text-theme-primary"></i>
                                    </div>
                                </div>

                                <!-- Product Info -->
                                <h5 class="card-title text-theme-dark fw-bold mb-2">${p.name}</h5>
                                <p class="card-text text-theme-dark small mb-2 flex-grow-1">
                                    <c:choose>
                                        <c:when test="${not empty p.description}">
                                            ${p.description}
                                        </c:when>
                                        <c:otherwise>
                                            No description available
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="mb-3">
                                    <small class="text-theme-dark">
                                        <i class="fas fa-calendar me-1"></i>Created: ${p.createdAt}
                                    </small>
                                </div>

                                <div class="product-price mb-3">
                                    <h4 class="text-theme-primary fw-bold mb-0">$${p.price}</h4>
                                </div>

                                <!-- Action Buttons -->
                                <div class="d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/products/edit?id=${p.id}"
                                       class="btn btn-outline-primary btn-sm flex-grow-1">
                                        <i class="fas fa-edit me-1"></i>Edit
                                    </a>
                                    <button type="button"
                                            class="btn btn-outline-danger btn-sm"
                                            onclick="confirmDelete(${p.id}, '${p.name}')">
                                        <i class="fas fa-trash me-1"></i>Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="card text-center py-5">
                        <div class="card-body">
                            <i class="fas fa-box-open fa-5x text-theme-overlay mb-3"></i>
                            <h4 class="text-theme-dark">No Products Found</h4>
                            <p class="text-theme-dark mb-4">Start by adding your first product to the inventory</p>
                            <a href="${pageContext.request.contextPath}/products?action=ADD" class="btn btn-theme">
                                <i class="fas fa-plus me-2"></i>Add Product
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header border-0">
                <h5 class="modal-title text-theme-danger">
                    <i class="fas fa-exclamation-triangle me-2"></i>Confirm Delete
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <p class="text-theme-dark">Are you sure you want to delete <strong id="productName"></strong>?</p>
                <p class="text-theme-dark small mb-0">This action cannot be undone.</p>
            </div>
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <form id="deleteForm" method="post" style="display: inline;">
                    <input type="hidden" name="_method" value="DELETE">
                    <button type="submit" class="btn btn-danger">
                        <i class="fas fa-trash me-1"></i>Delete
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function confirmDelete(productId, productName) {
        document.getElementById('productName').textContent = productName;
        document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/products?id=' + productId;

        const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
        modal.show();
    }
</script>

<style>
    .product-card {
        transition: all 0.3s ease;
        border: 1px solid rgba(137, 180, 250, 0.1);
    }

    .product-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 40px rgba(137, 180, 250, 0.2) !important;
    }

    .product-icon-wrapper {
        background: linear-gradient(135deg, rgba(137, 180, 250, 0.1) 0%, rgba(148, 226, 213, 0.1) 100%);
        border-radius: 12px;
        padding: 20px;
    }

    .btn-outline-primary {
        color: var(--theme-primary);
        border-color: var(--theme-primary);
    }

    .btn-outline-primary:hover {
        background-color: var(--theme-primary);
        border-color: var(--theme-primary);
        color: white;
    }

    .btn-outline-danger {
        color: var(--theme-danger);
        border-color: var(--theme-danger);
    }

    .btn-outline-danger:hover {
        background-color: var(--theme-danger);
        border-color: var(--theme-danger);
        color: white;
    }

    .bg-theme-accent {
        background-color: var(--theme-accent) !important;
    }

    .navbar {
        border-bottom: 3px solid var(--theme-primary);
    }

    .modal-content {
        border-radius: 16px;
        border: none;
    }
</style>
</body>
</html>