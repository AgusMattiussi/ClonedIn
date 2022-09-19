<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${user.name}"/> | ClonedIn</title>
    </head>
    <body>
    <jsp:include page="../components/navbar.jsp">
        <jsp:param name="id" value="${user.id}" />
    </jsp:include>
    <div class="d-flex justify-content-between mt-2">
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
           <div class="container">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="card ml-2 mt-2 mb-2 h-70">
                            <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Card image cap"/>
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title" style="padding-top: 5px">
                                        <c:out value="${user.name}"/>
                                    </h5>
                                    <sec:authorize access="hasRole('ENTERPRISE')">
                                        <a href="<c:url value="/contact/${user.id}"/>">
                                            <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem">
                                                <spring:message code="profile_contactbutton"/>
                                            </button>
                                        </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-center">
                                <p class="card-text"><c:out value="${user.currentPosition}"/></p>
                                <p class="card-text"><c:out value="${user.location}"/></p>
                                <p class="card-text"><c:out value="${user.description}"/></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-9">
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profile_experience"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createEx/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                            <i class="bi bi-plus-square pr-2"></i><spring:message code="profile_experiencebutton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                <p class="card-text">
                                    <c:choose>
                                        <c:when test="${experiences.size() > 0}">
                                            <c:forEach items="${experiences}" var="experience">
                                                <c:out value="${experience.enterpriseName}"/>,
                                                <c:out value="${experience.position}"/>
                                                <c:out value="${experience.from}"/>
                                                <c:out value="${experience.to}"/>
                                                <c:out value="${experience.description}"/>
                                                <br>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                             <p class="card-text"><b><spring:message code="profile_noexperience"/></b></p>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profile_education"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createEd/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                        <i class="bi bi-plus-square pr-2"></i><spring:message code="profile_educationbutton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                   <c:choose>
                                       <c:when test="${educations.size() > 0}">
                                           <c:forEach items="${educations}" var="education">
                                               <c:out value="${education.title}"/>,
                                               <c:out value="${education.institutionName}"/>
                                               <c:out value="${education.dateFrom}"/>
                                               <c:out value="${education.dateTo}"/>
                                               <c:out value="${education.description}"/>
                                               <br>
                                           </c:forEach>
                                       </c:when>
                                       <c:otherwise>
                                           <p class="card-text"><b><spring:message code="profile_noeducation"/></b></p>
                                       </c:otherwise>
                                   </c:choose>
                            </div>
                        </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex justify-content-between">
                                    <h5 class="card-title"><spring:message code="profile_skills"/></h5>
                                    <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/createSkill/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: 200px">
                                            <i class="bi bi-plus-square pr-2"></i><spring:message code="profile_skillsbutton"/>
                                        </button>
                                    </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white text-left">
                                <c:choose>
                                    <c:when test="${skills.size() > 0}">
                                        <c:forEach items="${skills}" var="skill">
                                            <c:out value="${skill.description}"/>
                                            <br>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="card-text"><b><spring:message code="profile_noskills"/></b></p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>
