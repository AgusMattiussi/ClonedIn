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
<div class="d-flex justify-content-between mt-2 ml-4">
    <h3><spring:message code="navbar_notifications"/></h3>
</div>
    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
        <div class="container">
    <c:choose>
        <c:when test = "${currentPage > pages}">
            <h4 class="mt-5 mb-5"><spring:message code="NOnotifications"/></h4>
        </c:when>
        <c:otherwise>
            <c:forEach var="job" items="${jobOffers}">
        <div class="card mt-2 pt-2 ml-2 mr-2 mb-2" >
            <div class="row">
                <div class="col-sm-8">
            <h5 class="card-header"> ${job.enterpriseID}  | ${job.position} </h5>
                </div>
                <div class="col-sm-4">
            <span class="badge badge-pill badge-success"><c:out value="${job.categoryID}"/></span>
                </div>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title"><spring:message code="notofications_mode"/></h5>
                            <p class="card-text">${job.modality}</p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title"><spring:message code="notificacions_salary"/></h5>
                            <p class="card-text">${job.salary}</p>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <h5 class="card-title"><spring:message code="notifications_skills"/></h5>
                                <%-- <p class="card-text"><c:out value="${joboffer.skill1}"/> </p>--%>
                                <%-- <p class="card-text"><c:out value="${joboffer.skill2}"/> </p>--%>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row" style="margin-bottom: 5px">
                            <a href="#" class="btn btn-success"><spring:message code="notifications_YES"/></a>
                        </div>
                        <div class="row">
                            <a href="#" class="btn btn-danger"><spring:message code="notifications_NO"/></a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <h5 class="card-title"><spring:message code="notifications_desc"/></h5>
                    <p class="card-text">${job.description}</p>
                </div>
            </div>
        </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
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
    </div>
</body>
</html>
