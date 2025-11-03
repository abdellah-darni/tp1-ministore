<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Product List</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<h1>Products</h1>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Created At</th>
    </tr>
    <c:forEach var="p" items="${products}">
        <tr>
            <td>${p.id}</td>
            <td>${p.name}</td>
            <td>${p.description}</td>
            <td>${p.price}</td>
            <td>${p.createdAt}</td>
        </tr>
    </c:forEach>
</table>

<br>

<p>${pageContext.request.contextPath}</p>


</body>
</html>
