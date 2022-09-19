<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title>Error 404 | ClonedIn</title>
        <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
    </head>
    <body>
        <div class="d-flex align-items-center justify-content-center vh-100">
            <div class="text-center">
                <h1 class="display-1 fw-bold">404</h1>
                <p class="fs-3"><span class="text-danger">Ups!</span>Pagina no encontrada.</p>
                <p class="lead">
                    La pagina que buscas no existe.
                </p>
                <a href="<c:url value="/"/>" class="btn btn-primary" style="background-color: #04704C">Vuelve a la pagina principal</a>
            </div>
        </div>
    </body>
</html>
