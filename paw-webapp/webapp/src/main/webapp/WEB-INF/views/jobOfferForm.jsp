<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
    <title><spring:message code="jobOfferFormPageTitle"/></title>
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
                            <h2><strong></strong></h2>
                            <spring:message code="jobOfferFormPosition" var="positionPlaceholder"/>
                            <spring:message code="jobOfferFormDescription" var="descriptionPlaceholder"/>
                            <spring:message code="jobOfferFormSalary" var="salaryPlaceholder"/>
                            <div class="row">
                                <div class="col-md-12 mx-0">
                                    <div id="msform">
                                        <c:url value="/createJobOffer/${enterprise.id}" var="postPath"/>
                                        <form:form modelAttribute="jobOfferForm" action="${postPath}" method="post" accept-charset="utf-8">
                                            <fieldset>
                                                <div class="form-card">
                                                    <h2 class="fs-title"><spring:message code="jobOfferFormTitle"/></h2>
                                                        <form:input type="text" path="jobPosition" placeholder="${positionPlaceholder}"/>
                                                        <form:errors path="jobPosition" cssClass="formError" element="p"/>
                                                        <div class="d-flex">
                                                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="jobOfferFormModeRequired"/></label>
                                                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                                                            <form:select path="mode" cssClass="list-dt ml-auto">
                                                                <form:option value="Remoto"><spring:message code="selectModeVirtual"/></form:option>
                                                                <form:option value="Presencial"><spring:message code="selectModeOnSite"/></form:option>
                                                                <form:option value="Mixto"><spring:message code="selectModeMixed"/></form:option>
                                                            </form:select>
                                                            </div>
                                                        </div>
                                                        <form:input type="text" path="jobDescription" placeholder="${descriptionPlaceholder}"/>
                                                        <form:errors path="jobDescription" cssClass="formError" element="p"/>
                                                        <form:input type="number" path="salary" placeholder="${salaryPlaceholder}"/>
                                                        <form:errors path="salary" cssClass="formError" element="p"/>
                                                        <div class="d-flex">
                                                            <div class="row">
                                                                <div class="col-sm-4">
                                                                <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="jobOfferFormSkills"/></label>
                                                                </div>
                                                                 <div class="col-sm-4">
                                                                    <form:input type="text" path="skill1"/>
                                                                    <form:errors path="skill1" cssClass="formError" element="p"/>
                                                                 </div>
                                                                <div class="col-sm-4">
                                                                    <form:input type="text" path="skill2"/>
                                                                    <form:errors path="skill2" cssClass="formError" element="p"/>
                                                                 </div>
                                                                <form:errors cssClass="formError" element="p"/>
                                                            </div>
                                                        </div>
                                                        <div class="d-flex">
                                                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="registerCategoryRequired"/></label>
                                                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                                                                <form:select path="category" cssClass="list-dt ml-auto">
                                                                    <c:forEach items="${categories}" var="category">
                                                                        <form:option value="${category.name}"><spring:message code="${category.name}"/></form:option>
                                                                    </c:forEach>
                                                                </form:select>
                                                            </div>
                                                        </div>
                                                </div>
                                                <p><spring:message code="registerRequiredMsg"/></p>
                                                <a href="<c:url value="/profileEnterprise/${enterprise.id}"/>">
                                                    <button type="button" name="end" class="btn next action-button"><spring:message code="returnButtonMsg"/></button>
                                                </a>
                                                <button type="submit" name="end" class="btn next action-button">
                                                    <spring:message code="skillsFormButtonMsg"/>
                                                </button>
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
