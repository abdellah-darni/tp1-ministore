<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - MiniStore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --gradient-primary: linear-gradient(135deg, #89b4fa 0%, #94e2d5 100%);
            --text-dark: #4c4f69;
            --text-muted: #6c6f85;
            --bg-light: #eff1f5;
        }

        body {
            background-color: var(--bg-light);
            font-family: system-ui, -apple-system, sans-serif;
            min-height: 100vh;
        }

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

        /* Card Styling */
        .product-card {
            border: none;
            border-radius: 16px;
            background: white;
            box-shadow: 0 4px 12px rgba(137, 180, 250, 0.1);
            transition: all 0.3s ease;
            overflow: hidden;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 32px rgba(137, 180, 250, 0.2);
        }

        .product-icon-wrapper {
            background: linear-gradient(135deg, rgba(137, 180, 250, 0.1) 0%, rgba(148, 226, 213, 0.1) 100%);
            padding: 2rem;
            border-radius: 12px;
            margin-bottom: 1rem;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .product-icon-wrapper i {
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        /* Typography */
        h2 {
            color: var(--text-dark);
            font-weight: 800;
        }

        .text-theme-dark { color: var(--text-dark); }
        .text-theme-muted { color: var(--text-muted); }

        .product-price {
            font-size: 1.5rem;
            font-weight: 800;
            color: #89b4fa;
        }

        /* Buttons */
        .btn-theme {
            background: var(--gradient-primary);
            border: none;
            border-radius: 12px;
            color: white;
            font-weight: 600;
            padding: 0.8rem 1.5rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(137, 180, 250, 0.3);
        }

        .btn-theme:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(137, 180, 250, 0.4);
            color: white;
        }

        .btn-outline-custom {
            border: 2px solid #89b4fa;
            color: #89b4fa;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-outline-custom:hover {
            background: #89b4fa;
            color: white;
        }

        .btn-outline-danger-custom {
            border: 2px solid #f38ba8;
            color: #f38ba8;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-outline-danger-custom:hover {
            background: #f38ba8;
            color: white;
        }

        /* Modal */
        .modal-content {
            border-radius: 16px;
            border: none;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
        }

        .modal-header { border-bottom: 1px solid #eff1f5; }
        .modal-footer { border-top: 1px solid #eff1f5; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg sticky-top">
    <div class="container-fluid px-4">
        <a class="navbar-brand" href="#">
            <i class="fas fa-store me-2"></i>MiniStore
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">
                        <i class="fas fa-box me-1"></i>Products
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/login?logout=true">
                        <i class="fas fa-sign-out-alt me-1"></i>Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container px-4 py-5">
    <div class="row mb-5 align-items-center">
        <div class="col-md-6">
            <h2 class="mb-1">Products</h2>
            <p class="text-theme-muted mb-0">Manage your product inventory</p>
        </div>
        <div class="col-md-6 text-md-end mt-3 mt-md-0">
            <a href="${pageContext.request.contextPath}/products/new" class="btn btn-theme">
                <i class="fas fa-plus me-2"></i>Add New Product
            </a>
        </div>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm border-0" role="alert"
             style="background-color: #d1fae5; color: #065f46; border-radius: 12px;">
            <i class="fas fa-check-circle me-2"></i>${success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0" role="alert"
             style="background-color: #f8d7da; color: #842029; border-radius: 12px;">
            <i class="fas fa-exclamation-circle me-2"></i>${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach var="p" items="${products}">
                    <div class="col-md-6 col-lg-4 col-xl-3">
                        <div class="card h-100 product-card p-3">
                            <div class="card-body d-flex flex-column p-0">
                                <div class="product-icon-wrapper">
                                    <i class="fas fa-box-open fa-3x"></i>
                                </div>

                                <h5 class="fw-bold text-theme-dark mb-2">${p.name}</h5>
                                <p class="text-theme-muted small mb-3 flex-grow-1">
                                    <c:out value="${p.description}" default="No description available" />
                                </p>

                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <span class="text-theme-muted small">
                                        <i class="far fa-clock me-1"></i>${p.createdAt}
                                    </span>
                                    <div class="product-price">${p.price}</div>
                                </div>

                                <div class="d-flex gap-2 mt-auto">
                                    <a href="${pageContext.request.contextPath}/products/edit?id=${p.id}"
                                       class="btn btn-outline-custom btn-sm flex-grow-1">
                                        <i class="fas fa-edit me-1"></i>Edit
                                    </a>
                                    <button type="button"
                                            class="btn btn-outline-danger-custom btn-sm"
                                            onclick="confirmDelete(${p.id}, '${p.name}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12 text-center py-5">
                    <div class="text-theme-muted opacity-50 mb-3">
                        <i class="fas fa-box-open fa-4x"></i>
                    </div>
                    <h4 class="text-theme-dark">No Products Found</h4>
                    <p class="text-theme-muted">Get started by creating your first product.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger fw-bold">
                    <i class="fas fa-exclamation-triangle me-2"></i>Confirm Delete
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body text-center py-4">
                <p class="text-theme-dark mb-1">Are you sure you want to delete <strong id="productName"></strong>?</p>
                <p class="text-theme-muted small">This action cannot be undone.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-light text-muted fw-bold" data-bs-dismiss="modal">Cancel</button>
                <form id="deleteForm" method="post" style="display: inline;">
                    <input type="hidden" name="_method" value="DELETE">
                    <input type="hidden" id="idInput" name="id" value="">
                    <button type="submit" class="btn btn-danger px-4" style="border-radius: 10px;">Delete</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function confirmDelete(productId, productName) {
        document.getElementById('productName').textContent = productName;
        document.getElementById('idInput').value = productId;
        document.getElementById('deleteForm').action = '${pageContext.request.contextPath}/products/delete';

        const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
        modal.show();
    }
</script>
</body>
</html>