<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: SolChiSol
  Date: 24/09/22
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
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
            <h5 class="card-header">Firulais Asociados | Puesto</h5>
            <div class="card-body">
                <div class="row">
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title">Descripcion</h5>
                            <p class="card-text">Hola!</p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title">Modalidad</h5>
                            <p class="card-text">Virtual</p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title">Salario</h5>
                            <p class="card-text">$10000</p>
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
            </div>
        </div>
        </div>
    </div>
</body>
</html>
