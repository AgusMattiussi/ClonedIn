<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                <h5 class="ml-2 mt-2"><spring:message code="notificationsFilter"/></h5>
                <div class="d-flex flex-column ">
                    <a href="<c:url value="?status=aceptada"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="aceptada"/>
                        </button>
                    </a>
                    <a href="<c:url value="?status=rechazada"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="rechazada"/>
                        </button>
                    </a>
                    <a href="<c:url value="?status=pendiente"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="pendiente"/>
                        </button>
                    </a>
                    <a href="<c:url value="?status=cerrada"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="cerrada"/>
                        </button>
                    </a>
                    <a href="<c:url value="?status=cancelada"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="cancelada"/>
                        </button>
                    </a>
                    <a href="<c:url value="?"/>">
                        <button class="btn btn-secondary filterbtn btn-outline-dark mt-2">
                            <spring:message code="indexClearFilter"/>
                        </button>
                    </a>
                </div>
            </div>
        </div>
        <div class="col mr-2">
        <div class="d-flex justify-content-between mt-2 ml-4">
            <h3><spring:message code="navbarMyContacts"/></h3>
        </div>
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
            <div class="container">
                <c:choose>
                    <c:when test = "${jobOffers.size() == 0}">
                        <h4 class="mt-5 mb-5"><spring:message code="noContactsMsg"/></h4>
                    </c:when>
                    <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col"><spring:message code="contactsEnterpriseJobOffer"/></th>
                            <th scope="col"><spring:message code="contactsEnterpriseCategory"/></th>
                            <th scope="col"><spring:message code="contactsEnterpriseName"/></th>
                            <th scope="col"><spring:message code="contactsEnterpriseStatus"/></th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="contact" items="${jobOffers}">
                                <tr>
                                    <td><c:out value="${contact.position}"/></td>
                                    <c:set var="categoryName" value="${contact.category.name}"/>
                                    <td>
                                        <c:choose>
                                            <c:when test="${categoryName.compareTo('No-Especificado') == 0}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="${categoryName}"/>
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                    <td>
                                        <a href="<c:url value="/profileUser/${contact.userId}"/>" class="text-decoration-none">
                                            <c:out value="${contact.userName}"/>
                                        </a>
                                    </td>
                                    <c:set var="statusName" value="${contact.status}"/>
                                    <td><spring:message code="${statusName}"/></td>
                                    <c:if test="${statusName == 'pendiente'}">
                                        <td>
                                            <a href="<c:url value="/cancelJobOffer/${contact.userId}/${contact.id}"/>" >
                                                <button class="btn btn-danger" style="margin-bottom: 5px; min-width: 90px;">
                                                    <spring:message code="contactsCancelBtn"/>
                                                </button>
                                            </a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                        <!-- Pagination -->
                        <jsp:include page="../components/pagination.jsp">
                            <jsp:param name="path" value="contactsEnterprise/${enterpriseId}/"/>
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
