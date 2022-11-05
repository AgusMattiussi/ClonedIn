<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
            <h5 class="row ml-2 mr-2 mt-2" style="color:white"><spring:message code="notificationsFilter"/></h5>
            <div class="row d-flex ml-2 mr-2 ">
                <c:choose>
                    <c:when test = "${status == 'aceptada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="aceptada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=aceptada&sortBy=${sortById}"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="aceptada"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test = "${status == 'rechazada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="rechazada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=rechazada&sortBy=${sortById}"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="rechazada"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test = "${status == 'pendiente'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="pendiente"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=pendiente&sortBy=${sortById}"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="pendiente"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <!-- FIXME: UNIFICAR BOTON DE CERRADO CON CANCELADO-->
<%--                <c:choose>--%>
<%--                    <c:when test = "${status == 'cerrada'}">--%>
<%--                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>--%>
<%--                            <spring:message code="cerrada"/>--%>
<%--                        </button>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <a href="<c:url value="?status=cerrada&sortBy=${sortById}"/>">--%>
<%--                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">--%>
<%--                                <spring:message code="cerrada"/>--%>
<%--                            </button>--%>
<%--                        </a>--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>
                <c:choose>
                    <c:when test = "${status == 'cancelada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="cancelada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=cancelada&sortBy=${sortById}"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="cancelada"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            <a href="<c:url value="?"/>">
                <button class="btn btn-secondary filterbtn btn-outline-light mt-3">
                    <spring:message code="notificationsNOFilter"/>
                </button>
            </a>
        </div>
    </div>
    <div class="col mr-2">
        <div class="d-flex justify-content-between mt-2 ml-4">
            <h3><spring:message code="navbarNotifications"/></h3>
            <div style="width: 200px">
                <c:url value="${path}" var="getPath"/>
                <form:form modelAttribute="contactOrderForm" action="${getPath}" method="get">
                    <form:select path="sortBy" cssClass="form-select" onchange="this.form.submit()">
                        <form:option value="0"><spring:message code="contactOrderFormSortByTitle"/></form:option>
                        <form:option value="4"><spring:message code="contactOrderFormSortByDateAsc"/></form:option>
                        <form:option value="5"><spring:message code="contactOrderFormSortByDateDesc"/></form:option>
                    </form:select>
                </form:form>
            </div>
        </div>
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
    <div class="container">
        <c:choose>
            <c:when test = "${contactList.size() == 0}">
                <h5 class="mt-5 mb-5"><spring:message code="noNotificationsMsg"/></h5>
            </c:when>
            <c:otherwise>
                <c:forEach var="contact" items="${contactList}">
                    <div class="card justify-content-center mt-2 pt-2" >
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5>
                                <a href="<c:url value="/profileEnterprise/${contact.enterprise.id}"/>" class="text-decoration-none">
                                    <c:out value="${contact.enterprise.name}"/>
                                </a>
                                |
                                <a href="<c:url value="/jobOffer/${contact.jobOffer.id}"/>" class="text-decoration-none">
                                    <c:out value="${contact.jobOffer.position}"/>
                                </a>
                            </h5>
                            <c:set var="jobOfferCategoryName" value="${contact.jobOffer.category.name}"/>
                            <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                                <a href="<c:url value="/home?category=${contact.jobOffer.category.id}"/>">
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
                                        <p class="card-text"><c:out value="${contact.jobOffer.modality}"/></p>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <h5 class="card-title"><spring:message code="notificationsSalary"/></h5>
                                        <c:set var="salary" value="${contact.jobOffer.salary}"/>
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
                                        <c:if test="${contact.jobOffer.skills.size() == 0}">
                                            <p><spring:message code="profileInfoNotSpecified"/></p>
                                        </c:if>
                                        <c:forEach items="${contact.jobOffer.skills}" var="skill">
                                            <a href="<c:url value="/home?skill=${skill.description}"/>">
                                                <h5><span class="badge badge-success"><c:out value="${skill.description}"/></span></h5>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row">
                                        <h5 class="card-title">
                                            <spring:message code="notificationsDate"/>
                                        </h5>
                                        <c:choose>
                                            <c:when test="${contact.date == null}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="card-text"><c:out value="${contact.date}"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="d-flex flex-column align-items-center">
                                        <h5 class="card-title">
                                            <spring:message code="notificationsStatus"/> <spring:message code="${contact.status}"/>
                                        </h5>
                                        <c:if test="${contact.status == 'pendiente'}">
                                            <a>
                                                <button class="btn btn-success" style="margin-bottom: 5px; min-width: 90px;" data-bs-toggle="modal" data-bs-target="#acceptJobOfferModal${contact.jobOffer.id}">
                                                    <spring:message code="notificationsAccept"/>
                                                </button>
                                            </a>
                                            <a>
                                                <button class="btn btn-danger" style="min-width: 90px" data-bs-toggle="modal" data-bs-target="#rejectJobOfferModal${contact.jobOffer.id}">
                                                    <spring:message code="notificationsReject"/>
                                                </button>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="row mt-2">
                                <c:set var="desc" value="${fn:substring(contact.jobOffer.description,0,200)}"/>
                                <c:if test="${desc.compareTo('') != 0}">
                                    <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                                    <p class="card-text"><c:out value="${desc}"/>...</p>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <!-- Modal -->
                    <jsp:include page="../components/acceptJobOfferModal.jsp">
                        <jsp:param name="userId" value="${user.id}"/>
                        <jsp:param name="jobOfferId" value="${contact.jobOffer.id}"/>
                    </jsp:include>
                    <!-- Modal -->
                    <jsp:include page="../components/rejectJobOfferModal.jsp">
                        <jsp:param name="userId" value="${user.id}"/>
                        <jsp:param name="jobOfferId" value="${contact.jobOffer.id}"/>
                    </jsp:include>
                </c:forEach>
                <!-- Pagination -->
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="path" value="${path}&"/>
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="pages" value="${pages}" />
                </jsp:include>
            </c:otherwise>
        </c:choose>
    </div>
        </div>
    </div>
</div>
</body>
</html>
