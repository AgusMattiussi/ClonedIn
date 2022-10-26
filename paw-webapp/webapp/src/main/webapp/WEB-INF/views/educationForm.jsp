<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <%@include file="../components/imports.jsp"%>
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
  <title><spring:message code="educationFormPageTitle"/></title>
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
          <h2><strong><spring:message code="educationFormTitle"/></strong></h2>
          <p><spring:message code="educationFormWarning"/></p>
          <spring:message code="educationFormInstitution" var="insitutionPlaceholder"/>
          <spring:message code="educationFormDegree" var="degreePlaceholder"/>
          <spring:message code="educationFormComment" var="commentPlaceholder"/>
          <spring:message code="formDateFormat" var="datePlaceholder"/>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createEducation/${user.id}" var="postPath"/>
                <form:form modelAttribute="educationForm" action="${postPath}" method="post" accept-charset="utf-8">
                  <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title"><spring:message code="educationFormSubtitle"/></h2>
                      <form:input type="text" path="college" placeholder="${insitutionPlaceholder}"/>
                      <form:errors path="college" cssClass="formError" element="p"/>
                      <form:input type="text" path="degree" placeholder="${degreePlaceholder}"/>
                      <form:errors path="degree" cssClass="formError" element="p"/>
                      <div class="d-flex">
                        <label class="startDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="educationFormStartDate"/></label>
                        <div class="row" style="margin-left: 10px">
                          <div class="col-sm-6" style="margin-top: 1.2rem;">
                            <form:select path="monthFrom" cssClass="list-dt ml-auto">
                                <form:option value="Enero"><spring:message code="selectMonth1"/></form:option>
                                <form:option value="Febrero"><spring:message code="selectMonth2"/></form:option>
                                <form:option value="Marzo"><spring:message code="selectMonth3"/></form:option>
                                <form:option value="Abril"><spring:message code="selectMonth4"/></form:option>
                                <form:option value="Mayo"><spring:message code="selectMonth5"/></form:option>
                                <form:option value="Junio"><spring:message code="selectMonth6"/></form:option>
                                <form:option value="Julio"><spring:message code="selectMonth7"/></form:option>
                                <form:option value="Agosto"><spring:message code="selectMonth8"/></form:option>
                                <form:option value="Septiembre"><spring:message code="selectMonth9"/></form:option>
                                <form:option value="Octubre"><spring:message code="selectMonth10"/></form:option>
                                <form:option value="Noviembre"><spring:message code="selectMonth11"/></form:option>
                                <form:option value="Diciembre"><spring:message code="selectMonth12"/></form:option>
                            </form:select>
                          </div>
                          <div class="col-sm-6">
                          <form:input type="text" path="yearFrom" placeholder="${datePlaceholder}"/>
                          </div>
                        </div>
                      </div>
                      <form:errors path="monthFrom" cssClass="formError" element="p"/>
                      <form:errors path="yearFrom" cssClass="formError" element="p"/>
                      <div class="d-flex">
                        <label class="endDate" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="educationFormEndDate"/></label>
                        <div class="row" style="margin-left: 10px">
                          <div class="col-sm-6" style="margin-top: 1.2rem;">
                            <form:select path="monthTo" cssClass="list-dt ml-auto">
                              <form:option value="Enero"><spring:message code="selectMonth1"/></form:option>
                              <form:option value="Febrero"><spring:message code="selectMonth2"/></form:option>
                              <form:option value="Marzo"><spring:message code="selectMonth3"/></form:option>
                              <form:option value="Abril"><spring:message code="selectMonth4"/></form:option>
                              <form:option value="Mayo"><spring:message code="selectMonth5"/></form:option>
                              <form:option value="Junio"><spring:message code="selectMonth6"/></form:option>
                              <form:option value="Julio"><spring:message code="selectMonth7"/></form:option>
                              <form:option value="Agosto"><spring:message code="selectMonth8"/></form:option>
                              <form:option value="Septiembre"><spring:message code="selectMonth9"/></form:option>
                              <form:option value="Octubre"><spring:message code="selectMonth10"/></form:option>
                              <form:option value="Noviembre"><spring:message code="selectMonth11"/></form:option>
                              <form:option value="Diciembre"><spring:message code="selectMonth12"/></form:option>
                            </form:select>
                          </div>
                          <div class="col-sm-6">
                            <form:input type="text" path="yearTo" placeholder="${datePlaceholder}"/>
                          </div>
                        </div>
                      </div>
                      <form:errors path="monthTo" cssClass="formError" element="p"/>
                      <form:errors path="yearTo" cssClass="formError" element="p"/>
                      <form:input type="text" path="comment" placeholder="${commentPlaceholder}"/>
                      <form:errors path="comment" cssClass="formError" element="p"/>
                    </div>
                    <p><spring:message code="educationFormRequiredMsg"/></p>
                    <button type="submit" class="btn action-button"><spring:message code="educationFormButtonMsg"/></button>
                    <div class="row">
                      <a href="<c:url value="/profileUser/${user.id}"/>">
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