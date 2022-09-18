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
  <title>Agrega tus aptitudes | ClonedIn</title>
  <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar0.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong>Agrega una experiencia</strong></h2>
          <p>Asegurate de llenar todos los campos antes de avanzar</p>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createSkill/${user.id}" var="postPath"/>
                <form:form modelAttribute="skillForm" action="${postPath}" method="post">
                  <fieldset>
                  <div class="form-card">
                  <h2 class="fs-title">Aptitudes</h2>
                    <form:input type="text" path="lang"  placeholder="Idiomas"/>
                    <form:errors path="lang" cssClass="formError" element="p"/>
                    <form:input type="text" path="skill" placeholder="Habilidades"/>
                    <form:errors path="skill" cssClass="formError" element="p"/>
                    <form:input type="text" path="more"  placeholder="Comentarios Adicionales"/>
                    <form:errors path="more" cssClass="formError" element="p"/>
                  </div>
                    <button type="submit" name="end" class="btn action-button">Finalizar</button>
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