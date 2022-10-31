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
                    <c:set var="image" value="${enterprise.image.id}"/>
                    <c:choose>
                        <c:when test="${image == null}">
                            <img class="card-img-top small" alt="profile_image" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" width="100" height="200" style="object-fit: cover">
                        </c:when>
                        <c:otherwise>
                            <img class="card-img-top small" alt="profile_image" src="<c:url value="/${enterprise.id}/image/${image}"/>" width="100" height="200" style="object-fit: cover">
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body p-0">
                        <sec:authorize access="hasRole('ENTERPRISE')">
                            <a href="<c:url value="/uploadEnterpriseProfileImage/${enterprise.id}"/>">
                                <button class="btn btn-block waves-effect mb-2" style="white-space:normal; background-color: #459F78; color: white;">
                                    <i class="bi bi-plus-square pr-2"></i><spring:message code="imageFormBtn2"/>
                                </button>
                            </a>
                        </sec:authorize>
                        <div class="d-flex flex-wrap justify-content-between pb-0 pl-4 mt-2 pr-2">
                        <h5 class="card-title"><c:out value="${enterprise.name}"/></h5>
                        <sec:authorize access="hasRole('ENTERPRISE')">
                            <a href="<c:url value="/editEnterprise/${enterprise.id}"/>">
                             <button type="button" class="btn btn-outline-dark"><i class="bi bi-pencil-square"></i></button>
                            </a>
                        </sec:authorize>
                        </div>
                    </div>
                    <div class="card-footer bg-white">
                        <c:set var="categoryName" value="${enterprise.category.name}"/>
                        <p class="card-text"><i class="bi bi-list-ul" style="margin-right: 5px"></i><spring:message code="profileCategory"/>
                            <c:choose>
                                <c:when test="${categoryName.compareTo('No-Especificado') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/home?category=${enterprise.category.id}"/>">
                                        <span class="badge badge-pill badge-success p-2">
                                            <spring:message code="${categoryName}"/>
                                        </span>
                                    </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasRole('ENTERPRISE')">
                                        <span class="badge badge-pill badge-success p-2">
                                            <spring:message code="${categoryName}"/>
                                        </span>
                                    </sec:authorize>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:set var="location" value="${enterprise.location}"/>
                        <p class="card-text"><i class="bi bi-geo-alt-fill" style="margin-right: 5px"></i><spring:message code="profileLocation"/>
                            <c:choose>
                                <c:when test="${location.compareTo('') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${location}"/>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:set var="workers" value="${enterprise.workers}"/>
                        <p class="card-text"><i class="bi bi-people-fill" style="margin-right: 5px"></i><spring:message code="profileWorkers"/>
                            <c:choose>
                                <c:when test="${workers.compareTo('') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${workers}"/>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:set var="year" value="${enterprise.year}"/>
                        <p class="card-text"><i class="bi bi-calendar-event" style="margin-right: 5px"></i><spring:message code="profileYear"/>
                            <c:choose>
                                <c:when test="${year == null}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${year}"/>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <c:set var="link" value="${enterprise.link}"/>
                        <p class="card-text"><i class="bi bi-globe" style="margin-right: 5px"></i><spring:message code="profileLink"/>
                            <c:choose>
                                <c:when test="${link.compareTo('') == 0}">
                                    <spring:message code="profileInfoNotSpecified"/>
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value="${link}"/>" target="_blank" class="text-decoration-none">
                                        <c:out value="${link}"/>
                                    </a>
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
                                                        <p><spring:message code="${joboffer.modality}"/> </p>
                                                    </div>
                                                </div>
                                                <div class="col">
                                                    <div class="row">
                                                        <h6> <spring:message code="jobOfferFormSkills"/></h6>
                                                        <c:if test="${joboffer.skills.size() == 0}">
                                                            <p><spring:message code="profileInfoNotSpecified"/></p>
                                                        </c:if>
                                                        <c:forEach items="${joboffer.skills}" var="skill">
                                                            <p><c:out value="${skill.description}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <p><c:out value="${joboffer.description}"/></p>
                                                    <c:if test="${joboffer.available == 'Activa'}">
                                                        <div class="d-flex flex-column">
                                                            <button class="btn btn-secondary" style="white-space:normal; margin-bottom: 0.75rem; width: 200px" data-bs-toggle="modal" data-bs-target="#closeJobOfferModal${joboffer.id}">
                                                                <spring:message code="profileEnterpriseCloseJobOfferButton"/>
                                                            </button>
                                                        </div>
                                                    </c:if>
                                                <c:if test="${joboffer.available == 'Cerrada'}">
                                                <span class="badge badge-danger p-2 mb-2">
                                                    <spring:message code="profileEnterpriseCloseJobOffer"/>
                                                </span>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Modal -->
                                    <jsp:include page="../components/closeJobOfferModal.jsp">
                                        <jsp:param name="jobOfferId" value="${joboffer.id}"/>
                                    </jsp:include>
                                </c:forEach>
                                <!-- Pagination -->
                                <jsp:include page="../components/pagination.jsp">
                                    <jsp:param name="path" value="/profileEnterprise/${enterpriseId}/?"/>
                                    <jsp:param name="currentPage" value="${currentPage}" />
                                    <jsp:param name="pages" value="${enterprisePages}" />
                                </jsp:include>
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex justify-content-center">
                                    <p class="card-text"><b><spring:message code="profileEnterprise.noJobOffersMsg"/></b></p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </sec:authorize>
                    <sec:authorize access="hasRole('USER')">
                        <c:choose>
                            <c:when test="${activeJobOffers.size() > 0}">
                                <c:forEach items="${activeJobOffers}" var="joboffer">
                                    <div class="card mt-2">
                                        <div class="card-body pb-0">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <h5 class="card-title"><c:out value="${joboffer.position}"/></h5>
                                                <c:set var="jobOfferCategoryName" value="${joboffer.category.name}"/>
                                                <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                                                    <a href="<c:url value="/home?category=${joboffer.category.id}"/>">
                                                        <span class="badge badge-pill badge-success p-2 mb-2">
                                                            <spring:message code="${jobOfferCategoryName}"/>
                                                        </span>
                                                    </a>
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
                                                        <c:if test="${joboffer.skills.size() == 0}">
                                                            <p><spring:message code="profileInfoNotSpecified"/></p>
                                                        </c:if>
                                                        <c:forEach items="${joboffer.skills}" var="skill">
                                                            <p><c:out value="${skill.description}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <p><c:out value="${joboffer.description}"/></p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                                <!-- Pagination -->
                                <jsp:include page="../components/pagination.jsp">
                                    <jsp:param name="path" value="/profileEnterprise/${enterpriseId}/?"/>
                                    <jsp:param name="currentPage" value="${currentPage}" />
                                    <jsp:param name="pages" value="${userPages}" />
                                </jsp:include>
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex justify-content-center">
                                    <p class="card-text"><b><spring:message code="profileEnterprise.noJobOffersMsg"/></b></p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
