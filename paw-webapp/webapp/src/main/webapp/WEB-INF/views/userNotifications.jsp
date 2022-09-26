<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
    <title><spring:message code="navbar_notifications"/> | ClonedIn</title>
</head>
<body>
<jsp:include page="../components/navbar.jsp">
    <jsp:param name="id" value="${loggedUserID}" />
</jsp:include>
<div class="jumbotron jumbotron-fluid" style="height: 100vh; padding: 10px; margin: 10px; background: #F2F2F2">
    <div class="card">
        <h5 class="card-header"> Empresa | Posicion</h5>
        <div class="card-body">
            <div class="row">
                <div class="col">
                    <div class="row">
                        <h5 class="card-title">Modalidad</h5>
                        <p class="card-text">Virtual</p>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h5 class="card-title">Salario</h5>
                        <p class="card-text">$1000</p>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h5 class="card-title">Skills</h5>
                        <p class="card-text">Ser capo</p>
                    </div>
                </div>
                <div class="col">
                    <div class="row" style="margin-bottom: 5px">
                        <a href="#" class="btn btn-success">Accept</a>
                    </div>
                    <div class="row">
                        <a href="#" class="btn btn-danger">Decline</a>
                    </div>
                </div>
            </div>
            <div class="row">
                <h5 class="card-title">Descripcion</h5>
                <p class="card-text">Lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet lorem ipsum dolor sit amet</p>
            </div>
        </div>
    </div>
    <!-- Pagination -->
    <nav class="d-flex justify-content-center align-items-center">
        <ul class="pagination">
            <li class="page-item">
                <a class="page-link text-decoration-none" style="color: black" href="<c:url value = "/?page=1"/>">
                    <spring:message code="index.pagination.first"/>
                </a>
            </li>
            <c:forEach var="i" begin="1" end="${pages}">
                <li class="page-item">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <a class="page-link text-decoration-none" style="color: black; font-weight: bold;" href="<c:url value="/?page=${i}"/>">
                                <c:out value="${i}"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link text-decoration-none" style="color: black" href="<c:url value="/?page=${i}"/>">
                                <c:out value="${i}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
            <li class="page-item">
                <a class="page-link text-decoration-none" style="color: black" href="<c:url value = "/?page=${pages}"/>">
                    <spring:message code="index.pagination.end"/>
                </a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
