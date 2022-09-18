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
  <title>Agrega tu perfil | ClonedIn</title>
  <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar0.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong>Completa tu perfil</strong></h2>
          <p>Asegurate de llenar todos los campos antes de avanzar</p>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/createEd/${user.id}" var="postPath"/>
                <form:form modelAttribute="educationForm" action="${postPath}" method="post">
                  <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title">Educacion</h2>
                      <form:input type="text" path="college" placeholder="Institución"/>
                      <form:input type="text" path="degree" placeholder="Titulo"/>
                      <div class="row">
                        <div class="col-4">
                          <label class="startDate">Desde</label>
                        </div>
                        <div class="col-8">
                          <form:input type="text" path="dateFrom" placeholder="YYYY-MM-DD"/>
                          <form:errors path="dateTo" cssClass="formError" element="p"/>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-4">
                          <label class="endDate">Hasta</label>
                        </div>
                        <div class="col-8">
                          <form:input type="text" path="dateTo" placeholder="YYYY-MM-DD"/>
                          <form:errors path="dateTo" cssClass="formError" element="p"/>
                        </div>
                      </div>
                      <form:input type="text" path="comment" placeholder="Comentario"/>
                    </div>
                    <p>(*) Los campos son requeridos</p>
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