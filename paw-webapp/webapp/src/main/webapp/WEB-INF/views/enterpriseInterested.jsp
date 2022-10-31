<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<!-- FIXME: PASARLE BIEN LA INFO NECESARIA-->
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
    <title><spring:message code="navbarCandidates"/> | ClonedIn</title>
</head>
<body>
<jsp:include page="../components/navbar.jsp">
    <jsp:param name="id" value="${loggedUserID}" />
</jsp:include>
<div class="row h-100 w-100">
    <div class="col-sm-2 sidebar">
        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
            <h5 class="row ml-2 mr-2 mt-2" style="color:white"><spring:message code="notificationsFilter"/></h5>
            <div class="row d-flex ml-2 mr-2">
                <c:choose>
                    <c:when test = "${status == 'aceptada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="aceptada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=aceptada"/>">
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
                        <a href="<c:url value="?status=rechazada"/>">
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
                        <a href="<c:url value="?status=pendiente"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="pendiente"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test = "${status == 'cerrada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="cerrada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=cerrada"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="cerrada"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test = "${status == 'cancelada'}">
                        <button class="btn btn-secondary filterbtn btn-outline-light mt-2" disabled>
                            <spring:message code="cancelada"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="?status=cancelada"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2">
                                <spring:message code="cancelada"/>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
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
            <h3><spring:message code="navbarCandidates"/></h3>
            <div style="width: 200px">
                <c:url value="${path}" var="getPath"/>
                <form:form modelAttribute="contactOrderForm" action="${getPath}" method="get">
                    <form:select path="sortBy" cssClass="form-select" onchange="this.form.submit()">
                        <form:option value="0"><spring:message code="contactOrderFormSortByTitle"/></form:option>
                        <form:option value="1"><spring:message code="contactOrderFormSortByJobOfferPosition"/></form:option>
                        <form:option value="2"><spring:message code="contactOrderFormSortByUserName"/></form:option>
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
                        <h4 class="mt-5 mb-5"><spring:message code="noContactsMsg"/></h4>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col"><spring:message code="contactsEnterpriseJobOffer"/></th>
                                <th scope="col"><spring:message code="contactsEnterpriseName"/></th>
                                <th scope="col"><spring:message code="contactsEnterpriseCategory"/></th>
                                <th scope="col"><spring:message code="profileSkills"/></th>
                                <th scope="col"><spring:message code="profileYearEx"/></th>
                                <th scope="col"><spring:message code="notificationsDate"/></th>
                                <th scope="col"><spring:message code="contactsEnterpriseStatus"/></th>
                                <th/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="contact" items="${contactList}">
                                <tr>
                                    <td><c:out value="${contact.jobOffer.position}"/></td>
                                    <c:set var="categoryName2" value="${contact.jobOffer.category.name}"/>
                                    <td>
                                        <a href="<c:url value="/profileUser/${contact.user.id}"/>" class="text-decoration-none">
                                            <c:out value="${contact.user.name}"/>
                                        </a>
                                    </td>
                                    <c:set var="categoryName" value="${contact.user.category.name}"/>
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
                                        <c:choose>
                                            <c:when test="${contact.user.skills.size() == 0}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="skill" items="${contact.user.skills}" begin="0" end="2">
                                                    <c:out value="${skill.description}"/>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><%--<c:out value="${contact.user.yearsOfExperience}"/>--%></td>
                                    <td><c:out value="${contact.date}"/></td>
                                    <c:set var="statusName" value="${contact.status}"/>
                                    <td><spring:message code="${statusName}"/></td>
                                    <td>
                                    <c:if test="${statusName == 'pendiente'}">
                                        <c:set var="contactId" value="${contact.jobOffer.id}"/>
                                        <c:set var="contactUserId" value="${contact.user.id}"/>
                                        <td>
                                            <div class="d-flex">
                                                <button class="btn btn-success" style="margin-bottom: 5px; width: 90px;"> <!-- data-bs-toggle="modal" data-bs-target="#acceptJobOfferModal" -->
                                                    <spring:message code="notificationsAccept"/>
                                                </button>
                                                <button class="btn btn-danger" style="margin-bottom: 5px; margin-left: 5px; width: 90px;">
                                                    <spring:message code="contactsCancelBtn"/>
                                                </button>
                                            </div>
                                        </td>
                                    </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
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
<!-- Modal -->
<jsp:include page="../components/cancelJobOfferModal.jsp">
    <jsp:param name="contactId" value="${contactId}"/>
    <jsp:param name="contactUserId" value="${contactUserId}"/>
</jsp:include>
</body>
</html>

