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
            <!-- TODO: AGREGAR FILTROS -->
            <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
                <h5 class="ml-2 mt-2"><spring:message code="indexFilter"/></h5>
                <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit">Accepted</button>
                <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit">Pending</button>
                <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit">Declined</button>
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
                        <h4 class="mt-5 mb-5"><spring:message code="noNotificationsMsg"/></h4>
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
                            <c:forEach var="job" items="${jobOffers}">
                                <tr>
                                    <td><c:out value="${job.position}"/></td>
                                    <c:set var="categoryName" value="${job.category.name}"/>
                                    <td><spring:message code="${categoryName}"/></td>
                                    <td><c:out value="${job.userName}"/></td>
                                    <c:set var="statusName" value="${job.status}"/>
                                    <td><spring:message code="${statusName}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        </div>
    </div>
</body>
</html>
