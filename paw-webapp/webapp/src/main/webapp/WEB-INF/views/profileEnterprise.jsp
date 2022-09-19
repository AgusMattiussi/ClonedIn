<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${enterprise.name}"/> | ClonedIn</title>
    </head>
    <body>
    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="id" value="${enterprise.id}" />
    </jsp:include>
    <div class="d-flex justify-content-between mt-2">
    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 mb-2 h-70">
                    <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Card image cap"/>
                    <div class="card-body pb-0">
                        <h5 class="card-title"><c:out value="${enterprise.name}"/></h5>
<%--                        <sec:authorize access="hasRole('ENTERPRISE')">--%>
<%--                        <a href="<c:url value="/contact/${us.id}"/>"><button type="button" class="btn btn-outline-dark">Contactar</button></a>--%>
<%--                        </sec:authorize>--%>
                    </div>
                    <div class="card-footer bg-white text-center">
                        <p class="card-text"><c:out value="${enterprise.location}"/></p>
                        <p class="card-text"><c:out value="${enterprise.description}"/></p>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row mr-2">
                    <sec:authorize access="hasRole('ENTERPRISE')">
                        <a href="<c:url value="/createJO/${enterprise.id}"/>">
                            <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                <i class="bi bi-plus-square pr-2"></i>Agregar Oferta de Trabajo
                            </button>
                        </a>
                    </sec:authorize>
                    <c:choose>
                        <c:when test="${joboffers.size() > 0}">
                            <c:forEach items="${joboffers}" var="joboffer">
                                <div class="card mt-2">
                                    <div class="card-body pb-0">
                                        <div class="d-flex justify-content-between">
                                            <h5 class="card-title"><c:out value="${joboffer.position}"/></h5>
                                        </div>
                                    </div>
                                    <div class="card-footer bg-white text-left">
                                        <p class="card-text">
                                            <c:out value="${joboffer.salary}"/>
                                            <c:out value="${joboffer.description}"/>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="card-text"><b>Ofertas laborales no especificadas</b></p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
