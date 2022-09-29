<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
  <head>
    <%@include file="../components/imports.jsp"%>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
    <title><spring:message code="register_pagetitle"/></title>
  </head>
  <body>
    <jsp:include page="../components/navbarEmpty.jsp"/>
      <div class="d-flex justify-content-between mt-2">
        <div class="container-fluid">
          <div class="row justify-content-center mt-0">
            <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
              <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                <h2><strong><spring:message code="register_title"/></strong></h2>
                <p><spring:message code="register_warning"/></p>
                <spring:message code="register_mail" var="emailPlaceholder"/>
                <spring:message code="register_name" var="namePlaceholder"/>
                <spring:message code="register_pass" var="passPlaceholder"/>
                <spring:message code="register_repeatpass" var="repeatpassPlaceholder"/>
                <spring:message code="register_location" var="locationPlaceholder"/>
                <spring:message code="register_position" var="positionPlaceholder"/>
                <spring:message code="register_description" var="descriptionPlaceholder"/>
                <div class="row">
                  <div class="col-md-12 mx-0">
                    <div id="msform">
                      <c:url value="/createUser" var="postPath"/>
                      <form:form modelAttribute="userForm" action="${postPath}" method="post" accept-charset="utf-8">
                      <fieldset>
                        <div class="form-card">
                          <h2 class="fs-title"><spring:message code="register_subtitle"/></h2>
                          <form:input type="email" path="email" placeholder="${emailPlaceholder}"/>
                          <form:errors path="email" cssClass="formError" element="p"/>
                          <form:input type="text" path="name" placeholder="${namePlaceholder}"/>
                          <form:errors path="name" cssClass="formError" element="p"/>
                          <form:input type="password" path="password" placeholder="${passPlaceholder}"/>
                          <form:errors path="password" cssClass="formError" element="p"/>
                          <form:errors cssClass="formError" element="p"/>
                          <form:input type="password" path="repeatPassword" placeholder="${repeatpassPlaceholder}"/>
                          <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                          <form:errors cssClass="formError" element="p"/>
<%--                          <div class="d-flex">--%>
<%--                              <label style="margin-top: 1.2rem; margin-left: 10px" for="ControlFile"><spring:message code="register_photomsg"/></label>--%>
<%--                            <div style="margin-left: 15px;">--%>
<%--                              <form:input type="file" path="image" class="form-control-file" id="ControlFile"/>--%>
<%--                            </div>--%>
<%--                          </div>--%>
                          <form:input type="text" path="city" placeholder="${locationPlaceholder}"/>
                          <form:errors path="city" cssClass="formError" element="p"/>
                          <form:input type="text" path="position" placeholder="${positionPlaceholder}"/>
                          <form:errors path="position" cssClass="formError" element="p"/>
                          <div class="d-flex">
                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="edform_level"/></label>
                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                              <form:select path="level" cssClass="list-dt ml-auto">
                                <form:option value="Primario"><spring:message code="select_level1"/></form:option>
                                <form:option value="Secundario"><spring:message code="select_level2"/></form:option>
                                <form:option value="Terciario"><spring:message code="select_level3"/></form:option>
                                <form:option value="Graduado"><spring:message code="select_level4"/></form:option>
                                <form:option value="Postgrado"><spring:message code="select_level5"/></form:option>
                              </form:select>
                            </div>
                            <form:errors path="level" cssClass="formError" element="p"/>
                          </div>
                          <div class="d-flex">
                            <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="register_category"/></label>
                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                              <form:select path="category" cssClass="list-dt ml-auto">
                                <c:forEach items="${categories}" var="category">
                                  <form:option value="${category.name}"><spring:message code="${category.name}"/></form:option>
                                </c:forEach>
                              </form:select>
                            </div>
                            <form:errors path="category" cssClass="formError" element="p"/>
                          </div>
                          <form:input type="text" path="aboutMe" placeholder="${descriptionPlaceholder}"/>
                          <form:errors path="aboutMe" cssClass="formError" element="p"/>
                        </div>
                        <p><spring:message code="register_requiredmsg"/></p>
                        <a href="<c:url value="/login"/>">
                          <button type="button" name="end" class="btn next action-button"><spring:message code="return_buttonmsg"/></button>
                        </a>
                        <button type="submit" name="end" class="btn action-button"><spring:message code="register_buttonmsg"/></button>
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