<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
  <head>
    <%@include file="../components/imports.jsp"%>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/assets/css/editForm.css"/>"/>
    <title><spring:message code="editPageTitle"/></title>
  </head>
  <body>
    <jsp:include page="../components/navbar.jsp"/>
      <div class="d-flex justify-content-between mt-2">
        <div class="container-fluid">
          <div class="row justify-content-center mt-0">
            <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
              <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                <h2><strong><spring:message code="registerTitle"/></strong></h2>
                <p><spring:message code="registerWarning"/></p>
                <spring:message code="registerMail" var="emailPlaceholder"/>
                <spring:message code="contactsEnterpriseName" var="namePlaceholder"/>
                <spring:message code="registerPassword" var="passPlaceholder"/>
                <spring:message code="registerRepeatPassword" var="repeatpassPlaceholder"/>
                <spring:message code="registerLocation" var="locationPlaceholder"/>
                <spring:message code="registerPosition" var="positionPlaceholder"/>
                <spring:message code="registerDescriptionUser" var="descriptionPlaceholder"/>
                <div class="row">
                  <div class="col-md-12 mx-0">
                    <div id="msform">
                      <c:url value="/editUser/${user.id}" var="postPath"/>
                      <form:form modelAttribute="EditUserForm" action="${postPath}" method="post" accept-charset="utf-8">
                      <fieldset>
                        <div class="form-card">
                          <h2 class="fs-title mb-2"><spring:message code="registerSubtitle"/></h2>
                          <form:label path="name">${namePlaceholder}</form:label>
                          <form:input type="text" path="name" placeholder="${user.name}"/>
                          <form:errors path="name" cssClass="formError" element="p"/>
<%--                          <div class="d-flex">--%>
<%--                              <label style="margin-top: 1.2rem; margin-left: 10px" for="ControlFile"><spring:message code="registerPhotoMsg"/></label>--%>
<%--                            <div style="margin-left: 15px;">--%>
<%--                              <form:input type="file" path="image" class="form-control-file" id="ControlFile"/>--%>
<%--                            </div>--%>
<%--                          </div>--%>
                          <form:label path="location">${locationPlaceholder}</form:label>
                          <form:input type="text" path="location" placeholder="${user.location}"/>
                          <form:errors path="location" cssClass="formError" element="p"/>
                          <form:label path="position">${positionPlaceholder}</form:label>
                          <form:input type="text" path="position" placeholder="${user.currentPosition}"/>
                          <form:errors path="position" cssClass="formError" element="p"/>
                          <div class="d-flex">
                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="registerEducationLevel"/></label>
                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                              <form:select path="level" cssClass="list-dt ml-auto">
                                <form:option value="${user.education}"><spring:message code="${user.education}"/></form:option>
                                <form:option value="No-especificado"><spring:message code="No-especificado"/></form:option>
                                <form:option value="Primario"><spring:message code="Primario"/></form:option>
                                <form:option value="Secundario"><spring:message code="Secundario"/></form:option>
                                <form:option value="Terciario"><spring:message code="Terciario"/></form:option>
                                <form:option value="Graduado"><spring:message code="Graduado"/></form:option>
                                <form:option value="Posgrado"><spring:message code="Posgrado"/></form:option>
                              </form:select>
                            </div>
                            <form:errors path="level" cssClass="formError" element="p"/>
                          </div>
                          <div class="d-flex">
                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="registerCategoryRequired"/></label>
                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                              <form:select path="category" cssClass="list-dt ml-auto">
                                <form:option value="${user.category.name}"><spring:message code="${user.category.name}"/></form:option>
                                <c:forEach items="${categories}" var="category">
                                  <form:option value="${category.name}"><spring:message code="${category.name}"/></form:option>
                                </c:forEach>
                              </form:select>
                            </div>
                            <form:errors path="category" cssClass="formError" element="p"/>
                          </div>
                          <form:label cssStyle="margin-left:10px; margin-top:10px;" path="aboutMe">${descriptionPlaceholder}</form:label>
                          <form:textarea path="aboutMe" rows="3" cssStyle="resize: none" placeholder="${user.description}"/>
                          <form:errors path="aboutMe" cssClass="formError" element="p"/>
                        </div>
<%--                        <p><spring:message code="registerRequiredMsg"/></p>--%>
                        <a href="<c:url value="/profileUser/${user.id}"/>">
                          <button type="button" name="end" class="btn next action-button"><spring:message code="returnButtonMsg"/></button>
                        </a>
                        <button type="submit" name="end" class="btn action-button"><spring:message code="educationFormButtonMsg"/></button>
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