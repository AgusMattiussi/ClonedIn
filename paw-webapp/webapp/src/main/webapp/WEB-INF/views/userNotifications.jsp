<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
    <title><spring:message code="navbarNotifications"/> | ClonedIn</title>
</head>
<body>
<jsp:include page="../components/navbar.jsp">
    <jsp:param name="id" value="${loggedUserID}" />
</jsp:include>
<div class="row h-100 w-100">
    <div class="col-sm-2 sidebar">
        <!-- TODO: AGREGAR FILTROS -->
        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
            <h5 class="ml-2 mt-2"><spring:message code="notificationsFilter"/></h5>
            <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit"><spring:message code="aceptada"/></button>
            <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit"><spring:message code="rechazada"/></button>
            <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit"><spring:message code="pendiente"/></button>
        </div>
    </div>
    <div class="col mr-2">
        <div class="d-flex justify-content-between mt-2 ml-4">
            <h3><spring:message code="navbarNotifications"/></h3>
        </div>
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
    <div class="container">
        <c:choose>
            <c:when test = "${jobOffers.size() == 0}">
                <h5 class="mt-5 mb-5"><spring:message code="noNotificationsMsg"/></h5>
            </c:when>
            <c:otherwise>
                <c:forEach var="job" items="${jobOffers}">
                    <div class="card justify-content-center mt-2 pt-2" >
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5><c:out value="${job.enterpriseName} | ${job.position}"/></h5>
                            <span class="badge badge-pill badge-success p-2"><c:out value="${job.category.name}"/></span>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col">
                                    <div class="row">
                                        <h5 class="card-title"><spring:message code="notificationsMode"/></h5>
                                        <p class="card-text"><c:out value="${job.modality}"/></p>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <h5 class="card-title"><spring:message code="notificationsSalary"/></h5>
                                        <p class="card-text"><c:out value="${job.salary}"/></p>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <h5 class="card-title"><spring:message code="notificationsSkills"/></h5>
<%--                                            <c:forEach items="${skillsMap[job.id]}" var="skill">--%>
<%--                                                <p class="card-text"><c:out value="${skill.value}"/></p>--%>
<%--                                            </c:forEach>--%>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="d-flex flex-column align-items-center">
                                        <h5 class="card-title">
                                            <spring:message code="notificationsStatus"/><spring:message code="${job.status}"/>
                                        </h5>
                                        <c:if test="${job.status == 'pendiente'}">
                                            <a href="<c:url value="/acceptJobOffer/${job.id}/1"/>" >
                                                <button class="btn btn-success" style="margin-bottom: 5px; min-width: 90px;" data-bs-toggle="modal" data-bs-target="#answerModal">
                                                    <spring:message code="notificationsAccept"/>
                                                </button>
                                            </a>
                                            <a href="<c:url value="/acceptJobOffer/${job.id}/0"/>" >
                                                <button class="btn btn-danger" style="min-width: 90px" data-bs-toggle="modal" data-bs-target="#answerModal">
                                                    <spring:message code="notificationsReject"/>
                                                </button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                                <p class="card-text">${job.description}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
        </div>
    </div>
</div>
<!-- Modal -->
<jsp:include page="../components/answerModal.jsp"/>
</body>
</html>
