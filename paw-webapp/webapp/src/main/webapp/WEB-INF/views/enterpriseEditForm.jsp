<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <link rel="stylesheet" href="<c:url value="/assets/css/editForm.css"/>"/>
    <title><spring:message code="editPageTitle"/></title>
</head>
    <body>
        <jsp:include page="../components/navbar.jsp">
            <jsp:param name="id" value="${enterprise.id}"/>
        </jsp:include>
        <div class="d-flex justify-content-between mt-2">
            <div class="container-fluid">
                <div class="row justify-content-center mt-0">
                    <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
                        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                            <h2><strong><spring:message code="editPageTitle"/></strong></h2>
                            <p><spring:message code="registerWarning"/></p>
                            <spring:message code="registerMail" var="emailPlaceholder"/>
                            <spring:message code="contactsEnterpriseName" var="namePlaceholder"/>
                            <spring:message code="registerPassword" var="passPlaceholder"/>
                            <spring:message code="registerRepeatPassword" var="repeatpassPlaceholder"/>
                            <spring:message code="registerLocation" var="locationPlaceholder"/>
                            <spring:message code="registerDescriptionEnterprise" var="descriptionPlaceholder"/>
                            <div class="row">
                                <div class="col-md-12 mx-0">
                                    <div id="msform">
                                        <c:url value="/editEnterprise/${enterprise.id}" var="postPath"/>
                                        <form:form modelAttribute="editEnterpriseForm" action="${postPath}" method="post" accept-charset="utf-8">
                                            <fieldset>
                                                <div class="form-card">
                                                    <h2 class="fs-title mb-2"><spring:message code="registerSubtitle"/></h2>
                                                    <form:label path="name">${namePlaceholder}</form:label>
                                                    <form:input type="text" path="name" placeholder="${enterprise.name}"/>
                                                    <form:errors path="name" cssClass="formError" element="p"/>
                                                    <form:label path="location">${locationPlaceholder}</form:label>
                                                    <form:input type="text" path="location" placeholder="${enterprise.location}"/>
                                                    <form:errors path="location" cssClass="formError" element="p"/>
                                                    <div class="d-flex">
                                                        <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="registerCategoryRequired"/></label>
                                                        <div style="margin-left: 15px; margin-top: 1.2rem;">
                                                            <form:select path="category" cssClass="list-dt ml-auto">
                                                                <form:option value="${enterprise.category.name}"><spring:message code="${enterprise.category.name}"/></form:option>
                                                                <c:forEach items="${categories}" var="category">
                                                                    <form:option value="${category.name}"><spring:message code="${category.name}"/></form:option>
                                                                </c:forEach>
                                                            </form:select>
                                                        </div>
                                                    </div>
                                                    <form:label cssStyle="margin-left:10px; margin-top:10px;" path="aboutUs">${descriptionPlaceholder}</form:label>
                                                    <form:textarea path="aboutUs" rows="3" cssStyle="resize: none" placeholder="${enterprise.description}"/>
                                                    <form:errors path="aboutUs" cssClass="formError" element="p"/>
                                                </div>
                                                <button type="submit" class="btn next action-button"><spring:message code="educationFormButtonMsg"/></button>
                                                <div class="row">
                                                    <a href="<c:url value="/profileEnterprise/${enterprise.id}"/>">
                                                        <button type="button" class="btn next btn-outline" style="color: #459F78"><spring:message code="returnButtonMsg"/></button>
                                                    </a>
                                                </div>
                                            </fieldset>
                                        </form:form>
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
