<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
    <title><c:out value="${job.position}"/> | ClonedIn</title>
</head>
    <body style="background: #F2F2F2">
    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="id" value="${loggedUserID}"/>
    </jsp:include>
    <div class="container">
    <div class="card justify-content-center mt-2 pt-2" >
        <div class="card-header d-flex justify-content-between align-items-center">
            <h5>
                <a href="<c:url value="/profileEnterprise/${job.enterprise.id}"/>" class="text-decoration-none">
                    <c:out value="${job.enterprise.name}"/>
                </a>
                <c:out value=" | ${job.position}"/></h5>
            <c:set var="jobOfferCategoryName" value="${job.category.name}"/>
            <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                <a href="<c:url value="/home?category=${job.category.id}"/>">
                     <span class="badge badge-pill badge-success p-2 mb-2">
                           <spring:message code="${jobOfferCategoryName}"/>
                     </span>
                </a>
            </c:if>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col">
                    <div class="row">
                        <h5 class="card-title"><spring:message code="notificationsMode"/></h5>
                        <p class="card-text"><spring:message code="${job.modality}"/></p>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h5 class="card-title"><spring:message code="notificationsSalary"/></h5>
                        <c:set var="salary" value="${job.salary}"/>
                        <c:choose>
                            <c:when test="${salary == null}">
                                <spring:message code="profileInfoNotSpecified"/>
                            </c:when>
                            <c:otherwise>
                                <p class="card-text">$<c:out value="${salary}"/></p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h5 class="card-title"><spring:message code="notificationsSkills"/></h5>
                        <c:if test="${job.skills.size() == 0}">
                            <p><spring:message code="profileInfoNotSpecified"/></p>
                        </c:if>
                        <c:forEach items="${job.skills}" var="skill">
                            <p><c:out value="${skill.description}"/></p>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="row mt-2">
                <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                <div class="d-flex justify-content-between">
                    <c:set var="desc" value="${job.description}"/>
                    <c:if test="${desc.compareTo('') == 0}">
                        <p><spring:message code="profileInfoNotSpecified"/></p>
                    </c:if>
                    <p class="card-text"><c:out value="${desc}"/>...</p>
                    <a>
                        <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem;" data-bs-toggle="modal" data-bs-target="#applicationModal${job.id}">
                            <spring:message code="userHomeApplicationButton"/>
                        </button>
                    </a>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
