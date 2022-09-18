<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/navbar.css"/>">
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    </head>
    <body>
    <nav class="navbar navbar-dark mb-auto nav-fill w-100" style="background-color: #04704C; font-size: large;">
        <div class="container-fluid">
            <a class="navbar-brand" href="<c:url value="/"/>">
                <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
            </a>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <sec:authorize access="hasRole('ENTERPRISE')">
                    <a class="nav-link" href="<c:url value="/"/>">DESCUBRIR PERFILES<span class="sr-only">(current)</span></a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('USER')">
<%--                        <a class="nav-link" href="<c:url value="/"/>">MI PERFIL<span class="sr-only">(current)</span></a>--%>
                    </sec:authorize>
                </li>
            </ul>
            <img src="<c:url value="/assets/images/noimagen.jpeg"/>" height="40" class="d-inline-block align-top" alt="">
            <button type="button" class="btn btn-outline-success waves-effect" style="color: white; font-size:20px"><i class="bi bi-box-arrow-right"></i></button>
<%--            <form class="form-inline my-2 my-lg-0 d-flex">--%>
<%--                <input class="form-control mr-sm-2" type="search" placeholder="Buscar" aria-label="Search">--%>
<%--                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Buscar</button>--%>
<%--            </form>--%>
        </div>
    </nav>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body>
</html>
