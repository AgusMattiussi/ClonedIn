<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <%@include file="../components/imports.jsp"%>
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
  <!-- Script -->
  <script src="<c:url value="/assets/js/steps.js"/>"></script>
  <title><spring:message code="expform_pagetitle"/></title>
  <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar0.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong><spring:message code="expform_title"/></strong></h2>
          <p><spring:message code="expform_warning"/></p>
          <spring:message code="expform_company" var="companyPlaceholder"/>
          <spring:message code="expform_position" var="positionPlaceholder"/>
          <spring:message code="expform_description" var="descriptionPlaceholder"/>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createEx/${user.id}" var="postPath"/>
                <form:form modelAttribute="experienceForm" action="${postPath}" method="post">
                    <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title"><spring:message code="exp_subtitle"/></h2>
                      <form:input type="text" path="company" placeholder="${companyPlaceholder}"/>
                      <form:errors path="company" cssClass="formError" element="p"/>
                      <form:input type="text" path="job" placeholder="${positionPlaceholder}"/>
                      <form:errors path="job" cssClass="formError" element="p"/>
                      <form:input type="text" path="jobDesc" placeholder="${descriptionPlaceholder}"/>
                      <form:errors path="jobDesc" cssClass="formError" element="p"/>
                      <div class="d-flex">
                        <label class="startDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="expform_startdate"/></label>
                        <div style="margin-left: 10px">
                          <form:input type="text" path="dateFrom" placeholder="YYYY-MM-DD"/>
                          <form:errors path="dateFrom" cssClass="formError" element="p"/>
                        </div>
                      </div>
                      <div class="d-flex">
                        <label class="endDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="expform_enddate"/></label>
                        <div style="margin-left: 10px">
                          <form:input type="text" path="dateTo" placeholder="YYYY-MM-DD"/>
                          <form:errors path="dateTo" cssClass="formError" element="p"/>
                        </div>
                      </div>
                    </div>
                      <p><spring:message code="expform_requiredmsg"/></p>
                    <button type="submit" name="end" class="btn action-button"><spring:message code="expform_button"/></button>
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