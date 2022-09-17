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
  <title>Contactate | ClonedIn</title>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong>Contactate con <c:url value="${user.name}"/></strong></h2>
          <p>Asegurate de llenar todos los campos antes de avanzar</p>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/contact/${user.id}" var="postPath"/>
                <form:form modelAttribute="simpleContactForm" action="${postPath}" method="post">
                  <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title">Mensaje</h2>
                      <form:input type="text" path="subject" placeholder="Asunto *"/>
                      <form:errors path="subject" cssClass="formError" element="p"/>
                      <form:input type="text" path="message" placeholder="Mensaje *"/>
                      <form:errors path="message" cssClass="formError" element="p"/>
                      <form:input type="text" path="contactInfo" placeholder="Informacion de contacto *"/>
                      <form:errors path="contactInfo" cssClass="formError" element="p"/>
                      <p>(*) Los campos son requeridos</p>
                      <div>
                        <button type="submit" class="btn action-button">Enviar</button>
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
</body>
</html>
