<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${enterprise.name}"/> | ClonedIn</title>
    </head>
    <body style="background: #F2F2F2">
    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="id" value="${loggedUserID}"/>
    </jsp:include>
    <div class="d-flex justify-content-between mt-2">
<%--    <div class="card w-100 mt-2 mr-2 ml-2" >--%>
    <div class="container">
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
                    <div class="card-footer bg-white">
                            <p class="card-text"><spring:message code="profile_category"/>: <span class="badge badge-pill badge-success"><c:out value="${category.get().name}"/></span></p>
                        <p class="card-text"><spring:message code="profile_location"/>: <c:out value="${enterprise.location}"/></p>
                            <p class="card-text"><b><spring:message code="register_description2"/></b></p>
                            <p class="card-text"><c:out value="${enterprise.description}"/></p>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row mr-2">
                    <sec:authorize access="hasRole('ENTERPRISE')">
                        <div class="d-flex justify-content-center mt-3">
                            <a href="<c:url value="/createJobOffer/${enterprise.id}"/>">
                                <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: fit-content">
                                    <i class="bi bi-plus-square pr-2"></i><spring:message code="addJobOffer_button"/>
                                </button>
                            </a>
                        </div>
                    </sec:authorize>
                    <c:choose>
                        <c:when test="${joboffers.size() > 0}">
                            <c:forEach items="${joboffers}" var="joboffer">
                                <div class="card mt-2">
                                    <div class="card-body pb-0">
                                        <div class="d-flex justify-content-between">
                                            <div class="row">
                                                <div class="col-sm-8">
                                            <h5 class="card-title"><c:out value="${joboffer.position}"/></h5>
                                                </div>
                                                    <div class="col-sm-4">
                                            <span class="badge badge-pill badge-success"><c:out value="${joboffer.categoryID}"/></span>
                                                    </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-footer bg-white text-left">
                                        <div class="row">
                                            <div class="col">
                                                <div class="row">
                                                    <h6><spring:message code="jobOfferForm_salary"/></h6>
                                                    <p><c:out value="${joboffer.salary}"/> </p>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="row">
                                                    <h6> <spring:message code="jobOfferForm_mode2"/></h6>
                                                    <p> <c:out value="${joboffer.modality}"/> </p>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="row">
                                                    <h6> <spring:message code="jobOfferForm_skills"/></h6>
<%--                                                    <p><c:out value="${joboffer.skill1}"/> </p>--%>
<%--                                                    <p><c:out value="${joboffer.skill2}"/> </p>--%>
                                                </div>
                                            </div>
                                        </div>
                                        <p><c:out value="${joboffer.description}"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex justify-content-center">
                                <p class="card-text"><b><spring:message code="profileEnterprise_noJobOffers"/></b></p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
<%--    </div>--%>
    </div>
    </body>
</html>
