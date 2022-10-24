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
        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
            <h5 class="row ml-2 mt-2" style="color:white"><spring:message code="notificationsFilter"/></h5>
            <div class="row d-flex ml-2 mr-2 ">
                <a href="<c:url value="?status=aceptada"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="aceptada"/>
                    </button>
                </a>
                <a href="<c:url value="?status=rechazada"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="rechazada"/>
                    </button>
                </a>
                <a href="<c:url value="?status=pendiente"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="pendiente"/>
                    </button>
                </a>
                <a href="<c:url value="?status=cerrada"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="cerrada"/>
                    </button>
                </a>
                <a href="<c:url value="?status=cancelada"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="cancelada"/>
                    </button>
                </a>
                <a href="<c:url value="?"/>">
                    <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                        <spring:message code="notificationsNOFilter"/>
                    </button>
                </a>
            </div>
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
                            <c:set var="jobCategoryName" value="${job.category.name}"/>
                            <c:if test="${jobCategoryName.compareTo('No-Especificado') != 0}">
                            <span class="badge badge-pill badge-success p-2"><spring:message code="${jobCategoryName}"/></span>
                            </c:if>
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
                                            <c:if test="${jobOffersSkillMap[job.id].size() == 0}">
                                                <p><spring:message code="profileInfoNotSpecified"/></p>
                                            </c:if>
                                            <c:forEach items="${jobOffersSkillMap[job.id]}" var="skill">
                                                <p><c:out value="${skill.description}"/></p>
                                            </c:forEach>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="d-flex flex-column align-items-center">
                                        <h5 class="card-title">
                                            <spring:message code="notificationsStatus"/><spring:message code="${job.status}"/>
                                        </h5>
                                        <c:if test="${job.status == 'pendiente'}">
                                            <c:set var="jobOfferId" value="${job.id}"/>
<%--                                            <a href="<c:url value="/answerJobOffer/${user.id}/${job.id}/1"/>" >--%>
                                                <button class="btn btn-success" style="margin-bottom: 5px; min-width: 90px;" data-bs-toggle="modal" data-bs-target="#acceptJobOfferModal">
                                                    <spring:message code="notificationsAccept"/>
                                                </button>
<%--                                            </a>--%>
<%--                                            <a href="<c:url value="/answerJobOffer/${user.id}/${job.id}/0"/>" >--%>
                                                <button class="btn btn-danger" style="min-width: 90px" data-bs-toggle="modal" data-bs-target="#rejectJobOfferModal">
                                                    <spring:message code="notificationsReject"/>
                                                </button>
<%--                                            </a>--%>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="row mt-2">
                                <c:set var="desc" value="${job.description}"/>
                                <c:if test="${desc.compareTo('') != 0}">
                                    <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                                    <p class="card-text">${desc}</p>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <!-- Pagination -->
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="path" value="notificationsUser/${user.id}/"/>
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="pages" value="${pages}" />
                </jsp:include>
            </c:otherwise>
        </c:choose>
    </div>
        </div>
    </div>
</div>
<!-- Modal -->
<jsp:include page="../components/acceptJobOfferModal.jsp">
    <jsp:param name="userId" value="${user.id}"/>
    <jsp:param name="jobOfferId" value="${jobOfferId}"/>
</jsp:include>
<!-- Modal -->
<jsp:include page="../components/rejectJobOfferModal.jsp">
    <jsp:param name="userId" value="${user.id}"/>
    <jsp:param name="jobOfferId" value="${jobOfferId}"/>
</jsp:include>
</body>
</html>
