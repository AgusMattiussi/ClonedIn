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
    <div class="row h-100 w-100">
        <div class="col-sm-2 sidebar">
            <h5 class="ml-2 mt-2"><spring:message code="search_filter"/></h5>
        </div>
        <div class="col mr-2">
        <div class="d-flex justify-content-between mt-2 ml-4">
            <h3><spring:message code="navbar_mycontacts"/></h3>
        </div>
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
            <div class="container">
                <c:choose>
                    <c:when test = "${false}">
                        <h4 class="mt-5 mb-5"><spring:message code="NOnotifications"/></h4>
                    </c:when>
                    <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Job Offer</th>
                            <th scope="col">Name</th>
                            <th scope="col">Status</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="job" items="${jobOffers}">
                                <th scope="row">1</th>
                                <td>${job.position}</td>
                                <!-- como referenciar empleado y estado -->
                                <td>name</td>
                                <td>status</td>
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
