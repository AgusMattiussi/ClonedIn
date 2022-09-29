<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title>ClonedIn</title>
    </head>
    <body>
        <c:set var="searchFormModel" value="${searchForm}" scope="request" />
        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="id" value="${loggedUserID}" />
            <jsp:param name="model" value="searchFormModel"/>
        </jsp:include>
            <div class="row h-100 w-100">
                <div class="col-sm-2 sidebar">
                    <h5 class="ml-2 mt-2"><spring:message code="search_filter"/></h5>
                    <c:url value="/" var="getPath"/>
                    <form:form modelAttribute="searchForm" action="${getPath}" method="get">
                        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
                            <spring:message code="navbar_search" var="searchBarPlaceholder"/>
                            <form:input type="text" path="term" cssStyle="border-radius: 5px" placeholder="${searchBarPlaceholder}"/>
                            <button class="btn btn-secondary filterbtn btn-outline-dark mt-2" type="submit"><i class="bi bi-search"></i></button>
                        </div>
                    </form:form>
                    <h5 class="ml-2 mt-2"><spring:message code="index_filter"/></h5>
                    <c:url value="/" var="getPath"/>
                    <form:form modelAttribute="filterForm" action="${getPath}" method="get">
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <form:select path="category" cssClass="form-select">
                                <form:option value=""><spring:message code="categoryFilter"/></form:option>
                                <c:forEach items="${categories}" var="category">
                                    <form:option value="${category.id}"><spring:message code="${category.name}"/></form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <br>
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <spring:message code="profile_location" var="locationFilterPlaceholder"/>
                            <form:input type="text" path="location" cssStyle="border-radius: 5px" placeholder="${locationFilterPlaceholder}"/>
                        </div>
                        <br>
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <form:select path="educationLevel" cssClass="form-select">
                                <form:option value=""><spring:message code="educationLevelFilter"/></form:option>
                                <form:option value="Primario"><spring:message code="select_level1"/></form:option>
                                <form:option value="Secundario"><spring:message code="select_level2"/></form:option>
                                <form:option value="Terciario"><spring:message code="select_level3"/></form:option>
                                <form:option value="Graduado"><spring:message code="select_level4"/></form:option>
                                <form:option value="Posgrado"><spring:message code="select_level5"/></form:option>
                            </form:select>
                        </div>
                        <div class="dropdown ml-2 mt-2">
                            <a href="<c:url value="/?page=1"/>">
                                <button class="btn btn-secondary filterbtn btn-outline-dark" type="button">
                                    <spring:message code="index_clearfilter"/>
                                </button>
                            </a>
                        </div>
                        <div class="dropdown ml-2 mt-2">
                            <button class="btn btn-secondary filterbtn btn-outline-dark" type="submit">
                                <spring:message code="filterBtn"/>
                            </button>
                        </div>
                    </form:form>
                </div>
                <div class="col mr-2">
                    <div class="d-flex justify-content-between mt-2">
                        <h3><spring:message code="navbar_profiles"/></h3>
                    </div>
                    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
                        <div class="container">
                            <div class="card-deck justify-content-center mt-2 pt-2" >
                                <c:choose>
                                    <c:when test = "${currentPage > pages || users.size() == 0}">
                                        <h5 class="mt-5 mb-5"><spring:message code="index.noProfilesToShowMsg"/></h5>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="us" items="${users}">
                                            <div class="col-auto mb-3">
                                                <div class="card mt-1 h-100 mx-0" style="width: 15rem;">
                                                    <a class="text-decoration-none" href="<c:url value="/profileUser/${us.id}"/>" style="color: inherit">
                                                        <img class="card-img-top small" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" alt="Profile picture" width="100" height="200">
                                                        <div class="card-body">
                                                            <h5 class="card-title"><c:out value="${us.name}"/></h5>
                                                                <p><spring:message code="index_category"/>:
                                                                    <span class="badge badge-pill badge-success">
                                                                        <spring:message code="${us.category.name}"/>
                                                                    </span>
                                                                </p>
                                                                <p class="card-text"><spring:message code="profile_position"/>: <c:out value="${us.currentPosition}"/></p>
                                                                <p class="card-text"><spring:message code="profile_level"/>: <c:out value="${us.education}"/></p>
                                                                <p class="card-text"><spring:message code="profile_location"/>: <c:out value="${us.location}"/></p>
                                                        </div>
                                                    </a>
                                                    <div class="card-footer second bg-white text-right mt-auto">
                                                        <a href="<c:url value="/contact/${us.id}"/>"><button type="button" class="btn btn-outline-dark"><spring:message code="profile_contactbutton"/></button></a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
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
                </div>
            </div>
        </div>
    </body>
</html>
