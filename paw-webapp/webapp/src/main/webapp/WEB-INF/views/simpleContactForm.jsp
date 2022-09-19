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
  <title><spring:message code="contactform_pagetitle"/></title>
</head>
<body>
  <jsp:include page="../components/navbar.jsp"/>
  <div class="d-flex justify-content-between mt-2">
    <div class="container-fluid">
      <div class="row justify-content-center mt-0">
        <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
          <div class="card px-0 pt-4 pb-0 mt-3 mb-3" style="background: #F2F2F2">
            <h2><strong><spring:message code="contactform_title"/> <c:url value="${user.name}"/></strong></h2>
            <p><spring:message code="contactform_warning"/></p>
            <spring:message code="contactform_subject" var="subjectPlaceholder"/>
            <spring:message code="contactform_message" var="messagePlaceholder"/>
            <spring:message code="contactform_information" var="informationPlaceholder"/>
            <div class="row">
              <div class="col-md-12 mx-0">
                <div id="msform">
                  <c:url value="/contact/${user.id}" var="postPath"/>
                  <form:form modelAttribute="simpleContactForm" action="${postPath}" method="post">
                    <fieldset>
                      <div class="form-card">
                        <h2 class="fs-title"><spring:message code="contactform_subtitle"/></h2>
                        <form:input type="text" path="subject" placeholder="${subjectPlaceholder}"/>
                        <form:errors path="subject" cssClass="formError" element="p"/>
                        <form:input type="text" path="message" placeholder="${messagePlaceholder}"/>
                        <form:errors path="message" cssClass="formError" element="p"/>
                        <form:input type="text" path="contactInfo" placeholder="${informationPlaceholder}"/>
                        <form:errors path="contactInfo" cssClass="formError" element="p"/>
                        <p><spring:message code="contactform_requiredmsg"/></p>
                        <div>
                          <button type="submit" class="btn action-button" data-bs-toggle="modal" data-bs-target="#exampleModal">
                            <spring:message code="contactform_buttonmsg"/>
                          </button>
                        </div>
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
  <!-- Modal -->
  <jsp:include page="../components/mailConfirmationModal.jsp"/>
</body>
</html>