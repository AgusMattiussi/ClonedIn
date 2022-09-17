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
    <title>Agrega tu perfil | ClonedIn</title>
  </head>
  <body>
    <jsp:include page="../components/navbar.jsp"/>
      <div class="d-flex justify-content-between mt-2">
          <!-- MultiStep Form -->
        <div class="container-fluid">
          <div class="row justify-content-center mt-0">
            <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
              <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                <h2><strong>Completa tu perfil</strong></h2>
                <p>Asegurate de llenar todos los campos antes de avanzar</p>
                <div class="row">
                  <div class="col-md-12 mx-0">
                    <div id="msform">
                      <!-- progressbar
                      <ul id="progressbar">
                        <li class="active" id="personal"><strong>Personal</strong></li>
                        <li id="education"><strong>Educacion</strong></li>
                        <li id="experience"><strong>Experiencia</strong></li>
                        <li id="skills"><strong>Aptitudes</strong></li>
                      </ul>
                      fieldsets -->
                      <c:url value="/createUser" var="postPath"/>
                      <form:form modelAttribute="userForm" action="${postPath}" method="post">
                      <fieldset>
                        <div class="form-card">
                          <h2 class="fs-title">Informacion Personal</h2>
                          <form:input type="email" path="email" placeholder="Email *"/>
                          <form:errors path="email" cssClass="formError" element="p"/>
                          <form:input type="text" path="name" placeholder="Nombre *"/>
                          <form:errors path="name" cssClass="formError" element="p"/>
                          <!--<div class="row">
                            <div class="col-6">
                              agregar form:
                              <label for="ControlFile">Elegir foto de perfil</label>
                            </div>
                            <div class="col-6">
                              <input type="file" class="form-control-file" id="ControlFile">
                            </div>
                          </div>-->
                          <form:input type="text" path="city" placeholder="Ubicacion"/>
                          <form:input type="text" path="position" placeholder="Puesto Actual"/>
                          <form:input type="text" path="desc" placeholder="Descripcion *"/>
                          <form:errors path="name" cssClass="formError" element="p"/>
                          <!--<div class="row">
                            <div class="col-6">
                              agregar form:
                              <label class="Rubro">Rubro Laboral</label>
                            </div>
                            <div class="col-6">
                              <select class="list-dt" id="area1" name="area1">
                                <option selected>Ninguno</option>
                                <option>IT</option>
                                <option>RRHH</option>
                                <option>Secretaria</option>
                                <option>Investigacion</option>
                                <option>Otro</option>
                              </select>
                            </div>
                          </div>
                        </div>
                        <button type="button" name="next" class="btn next action-button">Continuar</button>
                      </fieldset>
                      <fieldset>
                        <div class="form-card">-->
                          <h2 class="fs-title">Educacion</h2>
                          <form:input type="text" path="college" placeholder="Institución"/>
                          <form:input type="text" path="degree"   placeholder="Titulo"/>
                          <!--<div class="row">
                            <div class="col-3">
                              <label class="startDate">Desde</label>
                            </div>
                            <div class="col-9">
                              <//form:input type="text" path="datedesde"  placeholder="YY/MM/DD"/>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-3">
                              <label class="endDate">Hasta</label>
                            </div>
                            <div class="col-9">
                              <//form:input type="text" path="datehasta" name="datahasta" placeholder="YY/MM/DD"/>
                            </div>
                          </div>
                        </div>
                        <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                        <button type="button" name="next" class="btn next action-button">Continuar</button>
                      </fieldset>
                      <fieldset>
                      <//c:forEach begin = "1" end = "3">
                      <div class="form-card">-->
                        <h2 class="fs-title">Experiencia</h2>
                        <form:input type="text" path="company" placeholder="Empresa"/>
                        <form:input type="text" path="job" placeholder="Puesto"/>
                        <form:input type="text" path="jobdesc" placeholder="Descripcion"/>
                        <!--<div class="row">
                          <div class="col-3">
                            <label class="startDate">Desde</label>
                          </div>
                          <div class="col-9">
                            <//form:input type="text" path="dated" placeholder="YY/MM/DD"/>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-3">
                            <label class="endDate">Hasta</label>
                          </div>
                          <div class="col-9">
                            <//form:input type="text" path="dateh" placeholder="YY/MM/DD"/>
                          </div>
                        </div>
                      </div>
                      <///c:forEach>
                      <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                      <button type="button" name="next" class="btn next action-button">Continuar</button>
                    </fieldset>
                    <fieldset>
                      <div class="form-card">
                        <h2 class="fs-title">Aptitudes</h2>
                        <//form:input type="text" path="lang"  placeholder="Idiomas"/>
                        <//form:input type="text" path="hability"  placeholder="Habilidades"/>
                        <//form:input type="text" path="more"  placeholder="Comentarios Adicionales"/>
                      </div>
                      <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>-->
                          <p>(*) Los campos son requeridos</p>
                          <button type="submit" name="end" class="btn action-button">Finalizar</button>
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