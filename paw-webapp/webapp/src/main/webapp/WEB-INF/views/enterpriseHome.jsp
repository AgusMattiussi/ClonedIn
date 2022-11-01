<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title>ClonedIn</title>
    </head>
    <body>
        <c:set var="searchFormModel" value="${searchForm}" scope="request" />
        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="id" value="${loggedUserID}" />
            <jsp:param name="model" value="searchFormModel"/>
        </jsp:include>
            <div class="row h-100 w-100">
                <div class="col-sm-2 sidebar">
                    <h5 class="ml-2 mt-2" style="color:white"><spring:message code="indexSearchFilter"/></h5>
                    <c:url value="/" var="getPath"/>
                    <form:form modelAttribute="searchForm" action="${getPath}" method="get">
                        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
                            <spring:message code="navbarSearch" var="searchBarPlaceholder"/>
                            <form:input type="text" path="term" cssStyle="border-radius: 5px" placeholder="${searchBarPlaceholder}"/>
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2" type="submit"><i class="bi bi-search"></i></button>
                        </div>
                    </form:form>
                    <h5 class="ml-2 mt-2" style="color:white"><spring:message code="indexFilter"/></h5>
                    <c:url value="/" var="getPath"/>
                    <form:form modelAttribute="enterpriseFilterForm" action="${getPath}" method="get">
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <form:select path="category" cssClass="form-select">
                                <form:option value=""><spring:message code="indexCategoryFilter"/></form:option>
                                <c:forEach items="${categories}" var="category">
                                    <c:if test="${category.name.compareTo('No-Especificado') != 0}">
                                        <form:option value="${category.id}"><spring:message code="${category.name}"/></form:option>
                                    </c:if>
                                </c:forEach>
                            </form:select>
                        </div>
                        <br>
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <spring:message code="registerLocation" var="locationFilterPlaceholder"/>
                            <form:input type="text" path="location" cssStyle="border-radius: 5px" placeholder="${locationFilterPlaceholder}"/>
                        </div>
                        <br>
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <form:select path="educationLevel" cssClass="form-select">
                                <form:option value=""><spring:message code="indexEducationLevelFilter"/></form:option>
                                <form:option value="Primario"><spring:message code="Primario"/></form:option>
                                <form:option value="Secundario"><spring:message code="Secundario"/></form:option>
                                <form:option value="Terciario"><spring:message code="Terciario"/></form:option>
                                <form:option value="Graduado"><spring:message code="Graduado"/></form:option>
                                <form:option value="Posgrado"><spring:message code="Posgrado"/></form:option>
                            </form:select>
                        </div>
<%--                        <label class="d-flex flex-wrap justify-content-center ml-2 mt-2" style="color:white"><spring:message code="contactOrderFormSortByYearsOfExperience"/> </label>--%>
<%--                        <div class="d-flex justify-content-center ml-2">--%>
<%--                            <div>--%>
<%--                                <spring:message code="indexMinFilter" var="minFilterPlaceholder"/>--%>
<%--                                <form:input type="text" path="minExperience" cssStyle="border-radius: 5px; width: 80px" placeholder="${minFilterPlaceholder}"/>--%>
<%--                            </div>--%>
<%--                            <div class="ml-2 mr-2 pt-2" style="color: #F2F2F2;">-</div>--%>
<%--                            <div>--%>
<%--                                <spring:message code="indexMaxFilter" var="maxFilterPlaceholder"/>--%>
<%--                                <form:input type="text" path="maxExperience" cssStyle="border-radius: 5px; width: 80px" placeholder="${maxFilterPlaceholder}"/>--%>
<%--                            </div>--%>
<%--                        </div>--%>
                        <br>
                        <div class="d-flex flex-wrap justify-content-center ml-2">
                            <spring:message code="skillsFormSubtitle" var="skillFilterPlaceholder"/>
                            <form:input type="text" path="skill" cssStyle="border-radius: 5px" placeholder="${skillFilterPlaceholder}"/>
                        </div>
                        <div class="dropdown ml-2 mt-2">
                            <a href="<c:url value="/?page=1"/>">
                                <button class="btn btn-secondary filterbtn btn-outline-light" type="button">
                                    <spring:message code="indexClearFilter"/>
                                </button>
                            </a>
                        </div>
                        <div class="dropdown ml-2 mt-2">
                            <button class="btn btn-secondary filterbtn btn-outline-light" type="submit">
                                <spring:message code="indexFilterBtn"/>
                            </button>
                        </div>
                </div>
                <div class="col mr-2">
                    <h3 class="mt-2 mb-2"><spring:message code="navbarProfiles"/></h3>
<%--                    <div class="d-flex justify-content-between mt-2">--%>
<%--                        <h3><spring:message code="navbarProfiles"/></h3>--%>
<%--                        <div style="width: 200px">--%>
<%--                            <form:select path="sortBy" cssClass="form-select" onchange="this.form.submit()">--%>
<%--                                <form:option value="0"><spring:message code="contactOrderFormSortByTitle"/></form:option>--%>
<%--                                <form:option value="6"><spring:message code="contactOrderFormSortByYearsOfExperience"/></form:option>--%>
<%--                            </form:select>--%>
<%--                        </div>--%>
<%--                    </div>--%>
                    </form:form>
                    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
                        <div class="container">
                            <div class="card-deck justify-content-center mt-2 pt-2" >
                                <c:choose>
                                    <c:when test = "${currentPage > pages || users.size() == 0}">
                                        <h5 class="mt-5 mb-5"><spring:message code="indexNoProfilesToShowMsg"/></h5>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="us" items="${users}">
                                            <div class="col-auto mb-3">
                                                <div class="card mt-1 h-100 mx-0" style="width: 15rem;">
                                                    <a class="text-decoration-none" href="<c:url value="/profileUser/${us.id}"/>" style="color: inherit">
                                                        <c:set var="image" value="${us.image.id}"/>
                                                        <c:choose>
                                                            <c:when test="${image == null}">
                                                                <img class="card-img-top small" alt="profile_image" src="<c:url value="/assets/images/defaultProfilePicture.png"/>" width="100" height="200" style="object-fit: cover">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img class="card-img-top small" alt="profile_image" src="<c:url value="/${us.id}/image/${image}"/>" width="100" height="200" style="object-fit: cover">
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <div class="card-body">
                                                            <h5 class="card-title"><c:out value="${us.name}"/></h5>
                                                            <c:set var="categoryName" value="${us.category.name}"/>
                                                                <p><i class="bi bi-list-ul" style="margin-right: 5px"></i><spring:message code="indexCategory"/>
                                                                    <c:choose>
                                                                        <c:when test="${categoryName.compareTo('No-Especificado') == 0}">
                                                                            <spring:message code="profileInfoNotSpecified"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <a href="<c:url value="?category=${us.category.id}"/>">
                                                                                <span class="badge badge-pill badge-success">
                                                                                    <spring:message code="${categoryName}"/>
                                                                                </span>
                                                                            </a>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>
                                                            <c:set var="position" value="${us.currentPosition}"/>
                                                            <p class="card-text"><i class="bi bi-briefcase" style="margin-right: 5px"></i><spring:message code="profilePosition"/>
                                                                <c:choose>
                                                                <c:when test="${position.compareTo('') == 0}">
                                                                    <spring:message code="profileInfoNotSpecified"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:out value="${position}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </p>

                                                            <c:set var="educationLevel" value="${us.education}"/>
                                                            <p class="card-text"><i class="bi bi-book" style="margin-right: 5px"></i><spring:message code="profileEducationLevel"/>
                                                                <c:choose>
                                                                    <c:when test="${educationLevel.compareTo('No-especificado') == 0}">
                                                                        <spring:message code="profileInfoNotSpecified"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <spring:message code="${educationLevel}"/>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </p>

                                                            <c:set var="location" value="${us.location}"/>
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

                                                            <div>
                                                                <c:choose>
                                                                    <c:when test="${us.skills.size() == 0}">
                                                                        <spring:message code="profileNoSkillsMsg"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:forEach var="skill" items="${us.skills}" begin="0" end="3">
                                                                            <span class="badge badge-pill badge-success">
                                                                                <c:out value="${skill.description}"/>
                                                                            </span>
                                                                        </c:forEach>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        </div>
                                        <!-- Pagination -->
                                        <jsp:include page="../components/pagination.jsp">
                                            <jsp:param name="path" value="${path}&"/>
                                            <jsp:param name="currentPage" value="${currentPage}" />
                                            <jsp:param name="pages" value="${pages}" />
                                        </jsp:include>
                                    </c:otherwise>
                                </c:choose>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>
