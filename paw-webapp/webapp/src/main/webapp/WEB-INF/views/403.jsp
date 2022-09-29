<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title>Error 403 | ClonedIn</title>
        <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/favicon.ico"/>">
    </head>
    <body>
        <div class="d-flex align-items-center justify-content-center vh-100">
            <div class="text-center">
                <h1 class="display-1 fw-bold">403</h1>
                <p class="fs-3"> <span class="text-danger"><spring:message code="error403_titlespan"/></span> <spring:message code="error403_title"/></p>
                <p class="lead">
                    <spring:message code="error403_message"/>
                </p>
                <a onclick="history.back()" class="btn btn-primary" style="background-color: #04704C">
                    <spring:message code="error403_button"/>
                </a>
            </div>
        </div>
    </body>
</html>
