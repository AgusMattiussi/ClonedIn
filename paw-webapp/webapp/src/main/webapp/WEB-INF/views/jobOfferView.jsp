<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
            <sec:authorize access="hasRole('USER')">
                <h5>
                   <a href="<c:url value="/profileEnterprise/${job.enterprise.id}"/>" class="text-decoration-none">
                      <c:out value="${job.enterprise.name}"/>
                   </a> | <c:out value=" ${job.position}"/>
                </h5>
            </sec:authorize>
            <sec:authorize access="hasRole('ENTERPRISE')">
                <h5>
                    <c:out value=" ${job.position}"/>
                </h5>
            </sec:authorize>
            <c:set var="jobOfferCategoryName" value="${job.category.name}"/>
            <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                <sec:authorize access="hasRole('ENTERPRISE')">
                    <a href="<c:url value="/?category=${job.category.id}"/>">
                         <span class="badge badge-pill badge-success p-2 mb-2">
                             <spring:message code="${jobOfferCategoryName}"/>
                         </span>
                    </a>
                </sec:authorize>
                <sec:authorize access="hasRole('USER')">
                    <a href="<c:url value="/home?category=${job.category.id}"/>">
                         <span class="badge badge-pill badge-success p-2 mb-2">
                               <spring:message code="${jobOfferCategoryName}"/>
                         </span>
                    </a>
                </sec:authorize>
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
                            <sec:authorize access="hasRole('ENTERPRISE')">
                            <a href="<c:url value="/?skill=${skill.description}"/>">
                                <span class="badge badge-success" style="margin-bottom: 1rem">
                                    <c:out value="${skill.description}"/></span>
                            </a>
                            </sec:authorize>
                            <sec:authorize access="hasRole('USER')">
                                <a href="<c:url value="/home?skill=${skill.description}"/>">
                                    <span class="badge badge-pill badge-success" style="margin-bottom: 0.5rem">
                                        <c:out value="${skill.description}"/></span>
                                </a>
                            </sec:authorize>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="d-flex justify-content-between">
                <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                <sec:authorize access="hasRole('USER')">
                    <a>
                        <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem;" data-bs-toggle="modal" data-bs-target="#applicationModal${job.id}">
                            <spring:message code="userHomeApplicationButton"/>
                        </button>
                    </a>
                </sec:authorize>
            </div>
            <c:set var="desc" value="${job.description}"/>
            <c:choose>
                <c:when test="${desc.compareTo('') == 0}">
                    <spring:message code="profileInfoNotSpecified"/>
                </c:when>
                <c:otherwise>
                    <p style="white-space:pre-line"><c:out value="${desc}"/></p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
        <!-- Modal -->
        <jsp:include page="../components/applicationModal.jsp">
            <jsp:param name="jobOfferId" value="${job.id}"/>
        </jsp:include>
    </div>
    </body>
</html>
