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
  <title><spring:message code="edform_pagetitle"/></title>
  <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar0.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong><spring:message code="edform_title"/></strong></h2>
          <p><spring:message code="edform_warning"/></p>
          <spring:message code="edform_institution" var="insitutionPlaceholder"/>
          <spring:message code="edform_degree" var="degreePlaceholder"/>
          <spring:message code="edform_comment" var="commentPlaceholder"/>
          <spring:message code="form_dateformat" var="datePlaceholder"/>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createEd/${user.id}" var="postPath"/>
                <form:form modelAttribute="educationForm" action="${postPath}" method="post">
                  <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title"><spring:message code="edform_subtitle"/></h2>
                      <form:input type="text" path="college" placeholder="${insitutionPlaceholder}"/>
                      <form:errors path="college" cssClass="formError" element="p"/>
                      <form:input type="text" path="degree" placeholder="${degreePlaceholder}"/>
                      <form:errors path="degree" cssClass="formError" element="p"/>
                      <div class="d-flex">
                        <label class="startDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="edform_startdate"/></label>
                        <div style="margin-left: 10px">
                          <form:input type="text" path="dateFrom" placeholder="${datePlaceholder}"/>
                          <form:errors path="dateFrom" cssClass="formError" element="p"/>
                        </div>
                      </div>
                      <div class="d-flex">
                        <label class="endDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="edform_enddate"/></label>
                        <div style="margin-left: 10px">
                          <form:input type="text" path="dateTo" placeholder="${datePlaceholder}"/>
                          <form:errors path="dateTo" cssClass="formError" element="p"/>
                        </div>
                      </div>
                      <form:input type="text" path="comment" placeholder="${commentPlaceholder}"/>
                    </div>
                    <p><spring:message code="edform_requiredmsg"/></p>
                    <a href="<c:url value="/profile/${user.id}"/>">
                      <button type="button" name="end" class="btn next action-button"><spring:message code="return_buttonmsg"/></button>
                    </a>
                    <button type="submit" name="end" class="btn action-button"><spring:message code="edform_button"/></button>
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