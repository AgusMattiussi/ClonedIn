<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${user.name}"/> | ClonedIn</title>
    </head>
    <body>
    <jsp:include page="../components/navbar.jsp"/>
    <div class="d-flex justify-content-between mt-2">
    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 mb-2 h-70">
                    <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Card image cap"/>
                    <div class="card-body pb-0">
                        <h6 class="card-title">Descripcion de <c:out value="${user.name}"/></h6>
                    </div>
                    <div class="card-footer bg-white text-center">
                        <p class="card-text"> Actualmente soy: <c:out value="${user.currentPosition}"/></p>
                        <p class="card-text"><c:out value="${user.description}"/></p>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <div class="d-flex justify-content-between">
                            <h5 class="card-title">Experiencia</h5>
                            <sec:authorize access="hasRole('USER')">
                            <a href="<c:url value="/createEx/${user.id}"/>">
                                <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem">
                                    <i class="bi bi-plus-square pr-2"></i>Agregar Experiencia
                                </button>
                            </a>
                            </sec:authorize>
                        </div>
                    </div>
                    <div class="card-footer bg-white text-left">
                        <p class="card-text">
                            <c:if test="${experiences != null}">
                                <c:forEach items="${experiences}" var="experience">
                                    <c:out value="${experience.enterpriseName}"/>,
                                    <c:out value="${experience.position}"/>
                                    <c:out value="${experience.description}"/>
                                </c:forEach>
                            </c:if>
                            <c:if test="${experiences == null}">
                                <p class="card-text"><b>Experiencias no especificadas</b></p>
                            </c:if>
                        </p>
                    </div>
                </div>
                </div>
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <div class="d-flex justify-content-between">
                            <h5 class="card-title">Educacion</h5>
                            <a href="<c:url value="/createEd/${user.id}"/>">
                                <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem">
                                <i class="bi bi-plus-square pr-2"></i>Agregar Educacion
                                </button>
                            </a>
                        </div>
                    </div>
                    <div class="card-footer bg-white text-left">
                       <%-- <c:if test="${education != null}">
                            <c:out value="${education.title}"/>,
                            <c:out value="${education.institutionName}"/>
                            <c:out value="${education.description}"/>
                        </c:if>
                        <c:if test="${education == null}">
                            <p class="card-text"><b>Educacion no especificada</b></p>
                        </c:if>--%>
                    </div>
                </div>
                </div>
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <div class="d-flex justify-content-between">
                            <h5 class="card-title">Aptitudes</h5>
                            <a href="<c:url value="/createSkill/${user.id}"/>">
                                <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem">
                                    <i class="bi bi-plus-square pr-2"></i>Agregar aptitud
                                </button>
                            </a>
                        </div>
                    </div>
                    <div class="card-footer bg-white text-left">
<%--                        <c:if test="${skills != null}">--%>
<%--                            <c:out value="${skills.}"/>,--%>
<%--                        </c:if>--%>
<%--                        <c:if test="${skills == null}">--%>
<%--                            <p class="card-text"><b>Aptitudes no especificadas</b></p>--%>
<%--                        </c:if>--%>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
