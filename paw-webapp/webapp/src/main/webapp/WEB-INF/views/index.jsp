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
        <jsp:include page="../components/navbar.jsp"/>
        <div class="row h-100 w-100">
            <div class="col-sm-2 sidebar">
                <h5 class="ml-2 mt-2">Filtrar por:</h5>
                <div class="dropdown-group">
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Rubro</option>
                            <c:forEach var="cs" items="${categories}">
                                <option value="<c:out value="${cs.id}"/>"><c:out value="${cs.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Aptitudes</option>
                            <c:forEach var="ss" items="${skills}">
                                <option value="<c:out value="${ss.id}"/>"><c:out value="${ss.description}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Experiencia Laboral</option>
                            <option value="1">Exp 1</option>
                            <option value="2">Exp 2</option>
                            <option value="3">Exp 3</option>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Graduado de</option>
                            <option value="1">Universidad A</option>
                            <option value="2">Universidad B</option>
                            <option value="3">Universidad C</option>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary filterbtn btn-outline-dark" type="button">
                            Filtrar
                        </button>
                    </div>
                </div>
            </div>
            <div class="col mr-2">
                <div class="d-flex justify-content-between mt-2">
                    <h3>Descubrir Perfiles</h3>
                    <a href="<c:url value="/createUser"/>"><button type="button" class="btn waves-effect" style="background-color: #459F78; color: white"><i class="bi bi-plus-square pr-2"></i>Agregar Perfil</button></a>
                </div>
                <div class="jumbotron jumbotron-fluid" style="padding: 0">
                        <div class="card-deck justify-content-center mt-2 pt-2" >
                            <c:forEach var="us" items="${users}">
                                <div class="col-auto mb-3">
                                    <div class="card mt-1 h-100 mx-0" style="width: 13.5rem;">
                                        <a class="text-decoration-none" href="<c:url value="/profile/${us.id}"/>" style="color: inherit">
                                            <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Profile picture" width="100" height="200">
                                            <div class="card-body">
                                                <h5 class="card-title"><c:out value="${us.name}"/></h5>
                                                <p class="card-text"><c:out value="${us.description}"/></p>
                                            </div>
                                        </a>
                                        <div class="card-footer second bg-white text-right">
                                            <a href="<c:url value="/contact/${us.id}"/>"><button type="button" class="btn btn-outline-dark">Contactar</button></a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
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
    </body>
</html>
