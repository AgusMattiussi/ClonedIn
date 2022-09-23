<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title>ClonedIn</title>
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="id" value="${loggedUserID}" />
        </jsp:include>
            <div class="row h-100 w-100">
                <div class="col-sm-2 sidebar">
                    <h5 class="ml-2 mt-2"><spring:message code="index_filter"/></h5>
                    <div class="d-flex flex-wrap justify-content-center">
                        <c:forEach items="${categories}" var="category">
                            <a href="<c:url value="/?page=1&category=${category.id}"/>" class="btn btn-secondary" style="margin-left: 2px; margin-top: 2px">
<%--                                <spring:message code="${category.name}"/>--%>
                                ${category.name}
                            </a>
                        </c:forEach>
<%--                        <br>--%>
<%--                        <h6>Aptitud</h6>--%>
<%--                        <c:forEach items="${skills}" var="skill">--%>
<%--                            <a href="<c:url value="/?query=${skill.description}"/>" class="btn btn-secondary" style="margin-left: 2px; margin-top: 2px">--%>
<%--                                    ${skill.description}--%>
<%--                            </a>--%>
<%--                        </c:forEach>--%>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <a href="<c:url value="/?page=1"/>">
                            <button class="btn btn-secondary filterbtn btn-outline-dark" type="button">
                                <spring:message code="index_clearfilter"/>
                            </button>
                        </a>
                    </div>
    <%--                <div class="dropdown-group">--%>
    <%--                    <div class="dropdown ml-2 mt-2">--%>
    <%--                        <select class="form-select" aria-label="false">--%>
    <%--                            <option selected>Rubro</option>--%>
    <%--                            <c:forEach var="cs" items="${categories}">--%>
    <%--                                <option value="<c:out value="${cs.id}"/>"><c:out value="${cs.name}"/></option>--%>
    <%--                            </c:forEach>--%>
    <%--                        </select>--%>
    <%--                    </div>--%>
    <%--                    <div class="dropdown ml-2 mt-2">--%>
    <%--                        <select class="form-select" aria-label="false">--%>
    <%--                            <option selected>Aptitudes</option>--%>
    <%--                            <c:forEach var="ss" items="${skills}">--%>
    <%--                                <option value="<c:out value="${ss.id}"/>"><c:out value="${ss.description}"/></option>--%>
    <%--                            </c:forEach>--%>
    <%--                        </select>--%>
    <%--                    </div>--%>
    <%--                    <div class="dropdown ml-2 mt-2">--%>
    <%--                        <select class="form-select" aria-label="false">--%>
    <%--                            <option selected>Experiencia Laboral</option>--%>
    <%--                            <option value="1">Exp 1</option>--%>
    <%--                            <option value="2">Exp 2</option>--%>
    <%--                            <option value="3">Exp 3</option>--%>
    <%--                        </select>--%>
    <%--                    </div>--%>
    <%--                    <div class="dropdown ml-2 mt-2">--%>
    <%--                        <select class="form-select" aria-label="false">--%>
    <%--                            <option selected>Graduado de</option>--%>
    <%--                            <option value="1">Universidad A</option>--%>
    <%--                            <option value="2">Universidad B</option>--%>
    <%--                            <option value="3">Universidad C</option>--%>
    <%--                        </select>--%>
    <%--                    </div>--%>
    <%--                    <div class="dropdown ml-2 mt-2">--%>
    <%--                        <button class="btn btn-secondary filterbtn btn-outline-dark" type="button">--%>
    <%--                            Filtrar--%>
    <%--                        </button>--%>
    <%--                    </div>--%>
                    </div>

                <div class="col mr-2">
                    <div class="d-flex justify-content-between mt-2">
                        <h3><spring:message code="navbar_profiles"/></h3>
                    </div>
                    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
                        <div class="container">
                            <div class="card-deck justify-content-center mt-2 pt-2" >
                                <c:choose>
                                    <c:when test = "${currentPage > pages}">
                                        <h4 class="mt-5 mb-5"><spring:message code="index.noProfilesToShowMsg"/></h4>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="us" items="${users}">
                                            <div class="col-auto mb-3">
                                                <div class="card mt-1 h-100 mx-0" style="width: 13.5rem;">
                                                    <a class="text-decoration-none" href="<c:url value="/profileUser/${us.id}"/>" style="color: inherit">
                                                        <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Profile picture" width="100" height="200">
                                                        <div class="card-body">
                                                            <h5 class="card-title"><c:out value="${us.name}"/></h5>
<%--                                                            <div style="display: inline-block; padding: 0 5px; height: 20px; width: fit-content; font-size: 14px; border-radius: 20px; background-color: #459F78; color: white;">--%>
<%--                                                                <p><c:out value="${us.categoryId_fk}"/></p>--%>
<%--                                                            </div>--%>
<%--                                                                <p class="card-text"><c:out value="${us.description}"/></p>--%>
                                                        </div>
                                                    </a>
                                                    <div class="card-footer second bg-white text-right">
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
