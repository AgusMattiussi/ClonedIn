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
           <div class="container">
                <div class="row">
                    <div class="col-3">
                        <sec:authorize access="hasRole('USER')">
                        <div class="d-flex justify-content-center mt-3">
                            <c:choose>
                                <c:when test="${user.visibility == 1}">
                                    <a href="<c:url value="/hideUserProfile/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: fit-content">
                                            <spring:message code="hideProfile"/>
                                        </button>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value="/showUserProfile/${user.id}"/>">
                                        <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; margin-bottom: 0.75rem; width: fit-content">
                                            <spring:message code="showProfile"/>
                                        </button>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        </sec:authorize>
                        <div class="card ml-2 mt-2 mb-2 h-70">
                            <c:set var="image" value="${user.image.id}"/>
                            <c:choose>
                                <c:when test="${image == null}">
                                    <img class="card-img-top small" alt="profile_image" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" width="100" height="200">
                                </c:when>
                                <c:otherwise>
                                    <img class="card-img-top small" alt="profile_image" src="<c:url value="/${user.id}/image/${image}"/>" width="100" height="200">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body p-0">
                                <sec:authorize access="hasRole('USER')">
                                    <a href="<c:url value="/uploadProfileImage/${user.id}"/>">
                                        <button class="btn btn-block waves-effect mb-2" style="white-space:normal; background-color: #459F78; color: white;">
                                            <i class="bi bi-plus-square pr-2"></i><spring:message code="imageFormBtn"/>
                                        </button>
                                    </a>
                                </sec:authorize>
                                <div class="d-flex flex-wrap justify-content-between pb-0 pl-4 mt-2 pr-2">
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
                                    <sec:authorize access="hasRole('USER')">
                                        <a href="<c:url value="/editUser/${user.id}"/>">
                                            <button type="button" class="btn btn-outline-dark mr-2" style="margin-bottom: 1rem"><i class="bi bi-pencil-square"></i></button>
                                        </a>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="card-footer bg-white">
                                <c:set var="position" value="${user.currentPosition}"/>
                                <p class="card-text"><spring:message code="profilePosition"/>
                                    <c:choose>
                                        <c:when test="${position.compareTo('') == 0}">
                                            <spring:message code="profileInfoNotSpecified"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${position}"/>
                                        </c:otherwise>
                                    </c:choose>
                                            </p>
                                    <c:set var="categoryName" value="${user.category.name}"/>
                                    <p class="card-text"><spring:message code="profileCategory"/>
                                        <c:choose>
                                            <c:when test="${categoryName.compareTo('No-Especificado') == 0}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <sec:authorize access="hasRole('USER')">
                                                    <span class="badge badge-pill badge-success"><spring:message code="${categoryName}"/></span>
                                                </sec:authorize>
                                                <sec:authorize access="hasRole('ENTERPRISE')">
                                                    <a href="<c:url value="/?category=${user.category.id}&location=&educationLevel="/>">
                                                        <span class="badge badge-pill badge-success">
                                                            <spring:message code="${categoryName}"/>
                                                        </span>
                                                    </a>
                                                </sec:authorize>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <c:set var="educationLevel" value="${user.education}"/>
                                    <p class="card-text"><spring:message code="profileEducationLevel"/>
                                        <c:choose>
                                            <c:when test="${educationLevel.compareTo('No-especificado') == 0}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <spring:message code="${educationLevel}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                <c:set var="location" value="${user.location}"/>
                                    <p class="card-text"><spring:message code="profileLocation"/>
                                        <c:choose>
                                            <c:when test="${location.compareTo('') == 0}">
                                                <spring:message code="profileInfoNotSpecified"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${location}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
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
                                    <c:set var="desc" value="${user.description}"/>
                                    <c:choose>
                                        <c:when test="${desc.compareTo('') == 0}">
                                            <p><b><spring:message code="profileInfoNotSpecified"/></b></p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="card-text"><c:out value="${desc}"/></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        <div class="row mr-2">
                        <div class="card mt-2">
                            <div class="card-body pb-0">
                                <div class="d-flex flex-wrap justify-content-between">
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
                                                <div class="d-flex justify-content-between">
                                                <h6><b>
                                                    <c:out value="${experience.enterpriseName}"/> - <c:out value="${experience.position}"/>
                                                </b></h6>
                                                    <sec:authorize access="hasRole('USER')">
                                                    <a href="<c:url value="/deleteExperience/${user.id}/${experience.id}"/>">
                                                        <button type="button" class="btn btn-outline-danger"><i class="bi bi-trash"></i></button>
                                                    </a>
                                                    </sec:authorize>
                                                </div>
                                                <p style="font-size: 9pt">
                                                    <c:set var="monthFromNameEx" value="selectMonth${experience.monthFrom}"/>
                                                    <spring:message code="${monthFromNameEx}"/> <c:out value="${experience.yearFrom}"/> -
                                                <c:choose>
                                                    <c:when test="${experience.yearTo != null && experience.monthTo != 0}">
                                                        <c:set var="monthToNameEx" value="selectMonth${experience.monthTo}"/>
                                                        <spring:message code="${monthToNameEx}"/> <c:out value="${experience.yearTo}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <spring:message code="profileNow"/>
                                                    </c:otherwise>
                                                </c:choose>
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
                                <div class="d-flex flex-wrap justify-content-between">
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
                                               <div class="d-flex justify-content-between">
                                                       <h6><b><c:out value="${education.institutionName}"/> - <c:out value="${education.title}"/></b></h6>
                                                   <sec:authorize access="hasRole('USER')">
                                                       <a href="<c:url value="/deleteEducation/${user.id}/${education.id}"/>">
                                                           <button type="button" class="btn btn-outline-danger">
                                                               <i class="bi bi-trash"></i>
                                                           </button>
                                                       </a>
                                                   </sec:authorize>
                                               </div>
                                               <p style="font-size: 9pt">
                                                       <c:set var="monthFromNameEd" value="selectMonth${education.monthFrom}"/>
                                                       <c:set var="monthToNameEd" value="selectMonth${education.monthTo}"/>
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
                        <div class="card mt-2 mb-5">
                            <div class="card-body pb-0">
                                <div class="d-flex flex-wrap justify-content-between">
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
                                            <span class="badge badge-pill badge-success" style="margin-bottom: 1rem"><c:out value="${skill.description}"/>
                                                <sec:authorize access="hasRole('USER')">
                                                    <a href="<c:url value="/deleteSkill/${user.id}/${skill.id}"/>">
                                                    <button type="button" class="btn waves-effect btn-sm" style="color: white">
                                                        <i class="bi bi-x"></i>
                                                    </button>
                                                    </a>
                                                </sec:authorize>
                                            </span>
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
    </div>
    </body>
</html>
