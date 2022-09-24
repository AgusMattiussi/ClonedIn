<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
            <div class="d-flex">
            <sec:authorize access="hasRole('ENTERPRISE')">
                <a class="navbar-brand" href="<c:url value="/"/>">
                    <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
                </a>
            </sec:authorize>
            <sec:authorize access="hasRole('USER')">
                <a class="navbar-brand" href="<c:url value="/profileUser/${param.id}"/>">
                    <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
                </a>
            </sec:authorize>

            <sec:authorize access="hasRole('ENTERPRISE')">
                <div class="d-flex">
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/"/>">
                            <spring:message code="navbar_profiles"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/profileEnterprise/${param.id}"/>">
                            <spring:message code="navbar_myjoboffers"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/contactsEnterprise/${param.id}"/>">
                            <spring:message code="navbar_mycontacts"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                </div>
            </sec:authorize>
            <sec:authorize access="hasRole('USER')">
                <div class="d-flex">
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem" href="<c:url value="/profileUser/${param.id}"/>">
                            <spring:message code="navbar_myprofile"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                </div>
                <div class="d-flex">
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem" href="<c:url value="/notificationsUser/${param.id}"/>">
                            <spring:message code="navbar_notifications"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                </div>
            </sec:authorize>
            </div>
            <sec:authorize access="hasRole('ENTERPRISE')">
            <form class="form-inline my-2 my-lg-0 d-flex">
                <input class="form-control mr-sm-2" type="search" placeholder="Buscar" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Buscar</button>
            </form>
            </sec:authorize>
            <a href="<c:url value="/logout"/>">
                <button type="button" class="btn btn-outline-success waves-effect" style="color: white">
                    <i class="bi bi-box-arrow-right pr-2"></i>
                    <spring:message code="navbar_session"/>
                </button>
            </a>
        </div>
    </nav>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body>
</html>
