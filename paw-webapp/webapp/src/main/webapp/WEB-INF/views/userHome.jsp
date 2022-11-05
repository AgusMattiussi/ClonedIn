<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
                <c:url value="/home" var="getPath"/>
                <h5 class="ml-2 mt-2" style="color:white"><spring:message code="indexSearchFilter"/></h5>
                <div>
                    <form:form modelAttribute="userFilterForm" action="${getPath}" method="get">
                        <div class="d-flex flex-wrap justify-content-center ml-2 mt-2">
                            <spring:message code="navbarSearch" var="searchBarPlaceholder"/>
                            <form:input type="text" path="term" cssStyle="border-radius: 5px" placeholder="${searchBarPlaceholder}"/>
                            <button class="btn btn-secondary filterbtn btn-outline-light mt-2" type="submit"><i class="bi bi-search"></i></button>
                        </div>
                </div>
                <h5 class="ml-2 mt-2" style="color:white"><spring:message code="indexFilter"/></h5>
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
                        <form:select path="modality" cssClass="form-select">
                            <form:option value=""><spring:message code="indexModalityFilter"/></form:option>
                            <form:option value="Remoto"><spring:message code="selectModeVirtual"/></form:option>
                            <form:option value="Presencial"><spring:message code="selectModeOnSite"/></form:option>
                            <form:option value="Mixto"><spring:message code="selectModeMixed"/></form:option>
                        </form:select>
                    </div>
                    <br>
                    <div class="d-flex flex-wrap justify-content-center ml-2">
                        <spring:message code="skillsFormSubtitle" var="skillFilterPlaceholder"/>
                        <form:input type="text" path="skill" cssStyle="border-radius: 5px" placeholder="${skillFilterPlaceholder}"/>
                    </div>
                    <br>
                    <div class="d-flex flex-wrap justify-content-center ml-2">
                        <spring:message code="indexPositionFilter" var="locationFilterPlaceholder"/>
                        <form:input type="text" path="position" cssStyle="border-radius: 5px" placeholder="${locationFilterPlaceholder}"/>
                    </div>
                <label class="d-flex flex-wrap justify-content-center ml-2 mt-2" style="color:white"><spring:message code="indexSalaryFilter"/> </label>
                <div class="d-flex justify-content-center ml-2">
                    <div>
                        <spring:message code="indexMinFilter" var="minFilterPlaceholder"/>
                        <form:input type="number" path="minSalary" cssStyle="border-radius: 5px; width: 80px" placeholder="${minFilterPlaceholder}"/>
                    </div>
                    <div class="ml-2 mr-2 pt-2" style="color: #F2F2F2;">-</div>
                    <div>
                        <spring:message code="indexMaxFilter" var="maxFilterPlaceholder"/>
                        <form:input type="number" path="maxSalary" cssStyle="border-radius: 5px; width: 80px" placeholder="${maxFilterPlaceholder}"/>
                    </div>
                </div>
                    <div class="dropdown ml-2 mt-2">
                        <a href="<c:url value="/home?page=1"/>">
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
                <br>
            </div>
            <div class="col mr-2">
                <h3 class="mt-2 mb-2"><spring:message code="navbarMyNetwork"/></h3>
<%--                <div class="d-flex justify-content-between mt-2">--%>
<%--                    <h3><spring:message code="navbarMyNetwork"/></h3>--%>
<%--                    <div style="width: 200px">--%>
<%--                        <form:select path="sortBy" cssClass="form-select" onchange="this.form.submit()">--%>
<%--                            <form:option value="0"><spring:message code="contactOrderFormSortByTitle"/></form:option>--%>
<%--                            <form:option value="6"><spring:message code="contactOrderFormSortByYearsOfExperience"/></form:option>--%>
<%--                        </form:select>--%>
<%--                    </div>--%>
<%--                </div>--%>
                </form:form>
                <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
                    <div class="container">
                        <c:choose>
                            <c:when test = "${jobOffers.size() == 0}">
                                <h5 class="mt-5 mb-5"><spring:message code="profileEnterprise.noJobOffersMsg"/></h5>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="job" items="${jobOffers}">
                                    <div class="card justify-content-center mt-2 pt-2" >
                                        <div class="card-header d-flex justify-content-between align-items-center">
                                            <h5>
                                                <a href="<c:url value="/profileEnterprise/${job.enterprise.id}"/>" class="text-decoration-none">
                                                    <c:out value="${job.enterprise.name}"/>
                                                </a>
                                                <c:out value=" | ${job.position}"/></h5>
                                            <c:set var="jobOfferCategoryName" value="${job.category.name}"/>
                                            <c:if test="${jobOfferCategoryName.compareTo('No-Especificado') != 0}">
                                                <a href="<c:url value="/home?category=${job.category.id}"/>">
                                                    <span class="badge badge-pill badge-success p-2 mb-2">
                                                        <spring:message code="${jobOfferCategoryName}"/>
                                                    </span>
                                                </a>
                                            </c:if>
                                        </div>
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col">
                                                    <div class="row">
                                                        <h5 class="card-title"><spring:message code="notificationsMode"/></h5>
                                                        <p class="card-text"><spring:message code="${job.modality}"/></p>
                                                    </div>
                                                </div>
                                                <div class="col">
                                                    <div class="row">
                                                        <h5 class="card-title"><spring:message code="notificationsSalary"/></h5>
                                                        <c:set var="salary" value="${job.salary}"/>
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
                                                        <h5 class="card-title"><spring:message code="notificationsSkills"/></h5>
                                                        <c:if test="${job.skills.size() == 0}">
                                                            <p><spring:message code="profileInfoNotSpecified"/></p>
                                                        </c:if>
                                                        <c:forEach items="${job.skills}" var="skill">
                                                            <p><c:out value="${skill.description}"/></p>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row mt-2">
                                                <h5 class="card-title"><spring:message code="notificationsDescription"/></h5>
                                                <div class="d-flex justify-content-between">
                                                        <c:set var="desc" value="${fn:substring(job.description,0,200)}"/>
                                                        <c:if test="${desc.compareTo('') == 0}">
                                                            <p><spring:message code="profileInfoNotSpecified"/></p>
                                                        </c:if>
                                                        <p class="card-text"><c:out value="${desc}"/>...</p>
                                                    <a>
                                                        <button type="button" class="btn btn-outline-dark" style="margin-bottom: 1rem;" data-bs-toggle="modal" data-bs-target="#applicationModal${job.id}">
                                                            <spring:message code="userHomeApplicationButton"/>
                                                        </button>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Modal -->
                                    <jsp:include page="../components/applicationModal.jsp">
                                        <jsp:param name="currentPage" value="${currentPage}"/>
                                        <jsp:param name="jobOfferId" value="${job.id}"/>
                                    </jsp:include>
                                </c:forEach>
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