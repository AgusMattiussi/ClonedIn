<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
                <sec:authorize access="isAnonymous()">
                    <a class="navbar-brand" href="<c:url value="/login"/>">
                        <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
                    </a>
                </sec:authorize>
            <sec:authorize access="hasRole('ENTERPRISE')">
                <a class="navbar-brand" href="<c:url value="/"/>">
                    <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
                </a>
                <div class="d-flex">
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/"/>">
                            <spring:message code="navbarProfiles"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/profileEnterprise/${param.id}"/>">
                            <spring:message code="navbarMyJobOffers"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/contactsEnterprise/${param.id}"/>">
                            <spring:message code="navbarMyContacts"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/interestedEnterprise/${param.id}"/>">
                            <spring:message code="navbarCandidates"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                </div>
            </sec:authorize>
            <sec:authorize access="hasRole('USER')">
                <a class="navbar-brand" href="<c:url value="/home"/>">
                    <img src="<c:url value="/assets/images/logo.png"/>" height="40" class="d-inline-block align-top" alt="">
                </a>
                <div class="d-flex">
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/home"/>">
                            <spring:message code="navbarMyNetwork"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/profileUser/${param.id}"/>">
                            <spring:message code="navbarMyProfile"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/notificationsUser/${param.id}"/>">
                            <spring:message code="navbarNotifications"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                    <div class="nav-item" style="color: #F2F2F2">
                        <a class="nav-link" style="padding-top: 0.8rem; padding-left: 0.8rem" href="<c:url value="/applicationsUser/${param.id}"/>">
                            <spring:message code="navbarApplication"/>
                            <span class="sr-only">(current)</span>
                        </a>
                    </div>
                </div>
            </sec:authorize>
            </div>
            <sec:authorize access="isAuthenticated()">
            <a href="<c:url value="/logout"/>">
                <button type="button" class="btn btn-outline-success waves-effect" style="color: white">
                    <i class="bi bi-box-arrow-right pr-2"></i>
                    <spring:message code="navbarSession"/>
                </button>
            </a>
            </sec:authorize>
        </div>
    </nav>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body> 
</html>
