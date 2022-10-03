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
    <div class="container">
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 mb-2 h-70">
                    <img class="card-img-top small" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" alt="Card image cap"/>
                    <div class="card-body pb-0">
                        <div class="d-flex justify-content-between">
                        <h5 class="card-title"><c:out value="${enterprise.name}"/></h5>
                        <sec:authorize access="hasRole('ENTERPRISE')">
                            <a href="<c:url value="/editEnterprise/${enterprise.id}"/>">
                             <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem"><i class="bi bi-pencil-square"></i></button>
                            </a>
                        </sec:authorize>
                        </div>
                    </div>
                    <div class="card-footer bg-white">
                        <c:set var="categoryName" value="${enterprise.category.name}"/>
                        <p class="card-text"><spring:message code="profileCategory"/>:
                            <c:choose>
                                <c:when test="${categoryName.compareTo('No-Especificado') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-pill badge-success"><spring:message code="${categoryName}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <c:set var="location" value="${enterprise.location}"/>
                        <p class="card-text"><spring:message code="profileLocation"/>:
                            <c:choose>
                                <c:when test="${location.compareTo('') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${location}"/>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p class="card-text"><b><spring:message code="registerDescriptionEnterprise"/></b></p>
                        <c:set var="desc" value="${enterprise.description}"/>
                        <c:choose>
                            <c:when test="${desc.compareTo('') == 0}">
                                <spring:message code="profileInfoNotSpecified"/>
                            </c:when>
                            <c:otherwise>
                                <p class="card-text"><c:out value="${desc}"/></p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row mr-2">
                    <sec:authorize access="hasRole('ENTERPRISE')">
                        <div class="d-flex justify-content-center mt-3">
                            <a href="<c:url value="/createJobOffer/${enterprise.id}"/>">
                                <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: fit-content">
                                    <i class="bi bi-plus-square pr-2"></i><spring:message code="addJobOfferButton"/>
                                </button>
                            </a>
                        </div>
                    </sec:authorize>
                    <c:choose>
                        <c:when test="${jobOffers.size() > 0}">
                            <c:forEach items="${jobOffers}" var="joboffer">
                                <div class="card mt-2">
                                    <div class="card-body pb-0">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <h5 class="card-title"><c:out value="${joboffer.position}"/></h5>
                                            <c:set var="jobOfferCategoryName" value="${joboffer.category.name}"/>
                                            <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                                            <span class="badge badge-pill badge-success p-2 mb-2">
                                                <spring:message code="${jobOfferCategoryName}"/>
                                            </span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="card-footer bg-white text-left">
                                        <div class="row">
                                            <div class="col">
                                                <div class="row">
                                                    <h6><spring:message code="jobOfferFormSalary"/></h6>
                                                    <c:set var="salary" value="${joboffer.salary}"/>
                                                    <c:choose>
                                                        <c:when test="${salary == null}">
                                                            <spring:message code="profileInfoNotSpecified"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <p class="card-text">$<c:out value="${salary}"/></p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="row">
                                                    <h6> <spring:message code="jobOfferFormMode"/></h6>
                                                    <p> <c:out value="${joboffer.modality}"/> </p>
                                                </div>
                                            </div>
                                            <div class="col">
                                                <div class="row">
                                                    <h6> <spring:message code="jobOfferFormSkills"/></h6>
                                                    <c:if test="${jobOffersSkillMap[joboffer.id].size() == 0}">
                                                        <p><spring:message code="profileInfoNotSpecified"/></p>
                                                    </c:if>
                                                    <c:forEach items="${jobOffersSkillMap[joboffer.id]}" var="skill">
                                                        <p><c:out value="${skill.description}"/></p>
                                                    </c:forEach>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <p><c:out value="${joboffer.description}"/></p>
<%--                                            <c:if test="${joboffer.status != 'cancelada'}">--%>
<%--                                                <a href="<c:url value="/cancelJobOffer/${joboffer.id}"/>">--%>
<%--                                                    <button class="btn btn-danger" style="margin-bottom: 0.75rem; width: 200px">--%>
<%--                                                        <spring:message code="profileEnterpriseNOJOB"/>--%>
<%--                                                    </button>--%>
<%--                                                </a>--%>
<%--                                            </c:if>--%>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <!-- Pagination -->
                            <jsp:include page="../components/pagination.jsp">
                                <jsp:param name="path" value="profileEnterprise/${enterpriseId}/"/>
                                <jsp:param name="currentPage" value="${currentPage}" />
                                <jsp:param name="pages" value="${pages}" />
                            </jsp:include>
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex justify-content-center">
                                <p class="card-text"><b><spring:message code="profileEnterprise.noJobOffersMsg"/></b></p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
