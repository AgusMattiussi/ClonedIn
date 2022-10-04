<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <%@include file="../components/imports.jsp"%>
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
  <title><spring:message code="skillsFormPageTitle"/></title>
</head>
<body>
<jsp:include page="../components/navbar.jsp">
  <jsp:param name="id" value="${user.id}"/>
</jsp:include>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong><spring:message code="skillsFormTitle"/></strong></h2>
          <p><spring:message code="skillsFormWarning"/></p>
          <spring:message code="skillsFormSkills" var="skillsPlaceholder"/>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createSkill/${user.id}" var="postPath"/>
                <form:form modelAttribute="skillForm" action="${postPath}" method="post">
                  <fieldset>
                  <div class="form-card">
                  <h2 class="fs-title"><spring:message code="skillsFormSubtitle"/></h2>
                    <form:input type="text" path="skill" placeholder="${skillsPlaceholder}"/>
                    <form:errors path="skill" cssClass="formError" element="p"/>
                  </div>
                    <p><spring:message code="experienceFormRequiredMsg"/></p>
                    <a href="<c:url value="/profileUser/${user.id}"/>">
                      <button type="button" name="end" class="btn next action-button"><spring:message code="returnButtonMsg"/></button>
                    </a>
                    <button type="submit" name="end" class="btn action-button"><spring:message code="skillsFormButtonMsg"/></button>
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