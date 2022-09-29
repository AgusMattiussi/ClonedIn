<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${user.name}"/> | ClonedIn</title>
    </head>
    <body style="background: #F2F2F2" >
    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="id" value="${loggedUserID}" />
    </jsp:include>
    <div class="d-flex justify-content-between mt-2">
<%--        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">--%>
           <div class="container">
                <div class="row">
                    <div class="col-3">
                        <div class="card ml-2 mt-2 mb-2 h-70">
                            <img class="card-img-top small" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" alt="Card image cap"/>
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title" style="padding-top: 5px">
                                        <c:out value="${user.name}"/>
                                    </h5>
                                    <sec:authorize access="hasRole('ENTERPRISE')">
                                        <a href="<c:url value="/contact/${user.id}"/>">
                                            <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem">
                                                <spring:message code="profileContactButton"/>
                                            </button>
                                        </a>
                                    </sec:authorize>
<%--                                    <sec:authorize access="hasRole('USER')">--%>
<%--                                            <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem"><i class="bi bi-pencil-square"></i></button>--%>
<%--                                    </sec:authorize>--%>
                                </div>
                            </div>
                            <div class="card-footer bg-white">
                                    <p class="card-text"><spring:message code="profilePosition"/>: <c:out value="${user.currentPosition}"/></p>
                                    <c:set var="categoryName" value="${user.category.name}"/>
                                    <p class="card-text"><spring:message code="profileCategory"/>: <span class="badge badge-pill badge-success">
                                        <spring:message code="${categoryName}"/>
                                    </span></p>
                                    <p class="card-text"><spring:message code="profileEducationLevel"/>: <c:out value="${user.education}"/></p>
                                    <p class="card-text"><spring:message code="profileLocation"/>: <c:out value="${user.location}"/></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-9">
                        <div class="row mr-2">
                            <div class="card mt-2">
                                <div class="card-body pb-0">
                                    <div class="d-flex justify-content-between">
                                        <h5 class="card-title"><spring:message code="registerDescriptionUser"/></h5>
                                    </div>
                                </div>
                                <div class="card-footer bg-white text-left">
                                    <p class="card-text"><c:out value="${user.description}"/></p>
                                </div>
                            </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profileExperience"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createExperience/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                            <i class="bi bi-plus-square pr-2"></i><spring:message code="profileExperienceButton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                    <c:choose>
                                        <c:when test="${experiences.size() > 0}">
                                            <c:forEach items="${experiences}" var="experience">
                                                <h6><b>
                                                    <c:out value="${experience.enterpriseName}"/> - <c:out value="${experience.position}"/>
                                                </b></h6>
                                                <p style="font-size: 9pt">
                                                    <c:set var="monthFromNameEx" value="select_m${experience.monthFrom}"/>
                                                    <c:set var="monthToNameEx" value="select_m${experience.monthTo}"/>
                                                    <spring:message code="${monthFromNameEx}"/> <c:out value="${experience.yearFrom}"/> -
                                                    <spring:message code="${monthToNameEx}"/> <c:out value="${experience.yearTo}"/>
                                                </p>
                                                <p><c:out value="${experience.description}"/></p>
                                                <hr style="border: 1px solid grey">
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                             <p class="card-text"><b><spring:message code="profileNoExperienceMsg"/></b></p>
                                        </c:otherwise>
                                    </c:choose>
                            </div>
                        </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profileEducation"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createEducation/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                        <i class="bi bi-plus-square pr-2"></i><spring:message code="profileEducationButton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                   <c:choose>
                                       <c:when test="${educations.size() > 0}">
                                           <c:forEach items="${educations}" var="education">
                                               <h6><b><c:out value="${education.institutionName}"/> - <c:out value="${education.title}"/></b></h6>
                                               <p style="font-size: 9pt">
                                                       <c:set var="monthFromNameEd" value="select_m${education.monthFrom}"/>
                                                       <c:set var="monthToNameEd" value="select_m${education.monthTo}"/>
                                                       <spring:message code="${monthFromNameEd}"/> <c:out value="${education.yearFrom}"/> -
                                                       <spring:message code="${monthToNameEd}"/> <c:out value="${education.yearTo}"/>
                                               <p><c:out value="${education.description}"/></p>
                                               <hr style="border: 1px solid grey">
                                           </c:forEach>
                                       </c:when>
                                       <c:otherwise>
                                           <p class="card-text"><b><spring:message code="profileNoEducationMsg"/></b></p>
                                       </c:otherwise>
                                   </c:choose>
                            </div>
                        </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profileSkills"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createSkill/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                            <i class="bi bi-plus-square pr-2"></i><spring:message code="profileSkillsButton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                <c:choose>
                                    <c:when test="${skills.size() > 0}">
                                        <c:forEach items="${skills}" var="skill">
                                            <span class="badge badge-pill badge-success"><c:out value="${skill.description}"/></span>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="card-text"><b><spring:message code="profileNoSkillsMsg"/></b></p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
            </div>
<%--        </div>--%>
    </div>
    </body>
</html>
