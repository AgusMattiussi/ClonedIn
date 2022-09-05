<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
  <head>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css">
    <!-- JQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <!-- Popper -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <!-- BootStrap JS -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
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
                    <form id="msform">
                      <!-- progressbar -->
                      <ul id="progressbar">
                        <li class="active" id="personal"><strong>Personal</strong></li>
                        <li id="education"><strong>Educacion</strong></li>
                        <li id="experience"><strong>Experiencia</strong></li>
                        <li id="skills"><strong>Aptitudes</strong></li>
                      </ul>
                      <!-- fieldsets -->
                      <c:url value="/create" var="postPath"/>
                      <form:form modelAttribute="userForm" action="${postPath}" method="post">
                      <fieldset>
                        <div class="form-card">
                          <h2 class="fs-title">Informacion Personal</h2>
                          <form:input type="email" path="email" name="email" placeholder="Email"/>
                          <!-- para customizar los errores -->
                          <form:errors path="email" cssClass="formError" element="p"/>
                          <form:input type="text" path="name" name="name" placeholder="Nombre"/>
                          <div class="row">
                            <div class="col-6">
                              <!--agregar form:-->
                              <label for="ControlFile">Elegir foto de perfil</label>
                            </div>
                            <div class="col-6">
                              <input type="file" class="form-control-file" id="ControlFile">
                            </div>
                          </div>
                          <form:input type="text" path="city" name="location" placeholder="Ubicacion"/>
                          <form:input type="text" path="desc" name="description" placeholder="Descripcion"/>
                          <div class="row">
                            <div class="col-6">
                              <!--agregar form:-->
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
                        <div class="form-card">
                          <h2 class="fs-title">Educacion</h2>
                          <form:input type="text" path="college" name="school" placeholder="Institución"/>
                          <form:input type="text" path="degree" name="degree" placeholder="Titulo"/>
                          <form:input type="text" path="years" name="years" placeholder="Cantidad de anios"/>
                        </div>
                        <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                        <button type="button" name="next" class="btn next action-button">Continuar</button>
                      </fieldset>
                      <fieldset>
                        <c:forEach begin = "1" end = "3">
                        <div class="form-card">
                          <h2 class="fs-title">Experiencia</h2>
                          <form:input type="text" path="company" name="company" placeholder="Empresa"/>
                          <form:input type="text" path="job" name="position" placeholder="Puesto"/>
                          <form:input type="text" path="jobdesc" name="description" placeholder="Descripcion"/>
                          <div class="row">
                            <div class="col-3">
                              <label class="startDate">Desde</label>
                            </div>
                            <div class="col-9">
                              <select class="list-dt" id="startMonth" name="startMonth">
                                <option selected>Mes</option>
                                <option>Enero</option>
                                <option>Febrero</option>
                                <option>Marzo</option>
                                <option>Abril</option>
                                <option>Mayo</option>
                                <option>Junio</option>
                                <option>Julio</option>
                                <option>Agosto</option>
                                <option>Septiembre</option>
                                <option>Octubre</option>
                                <option>Noviembre</option>
                                <option>Diciembre</option>
                              </select>
                              <select class="list-dt" id="startYear" name="startYear">
                                <option selected>Anio</option>
                              </select>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col-3">
                              <label class="endDate">Hasta</label>
                            </div>
                            <div class="col-9">
                              <select class="list-dt" id="endMonth" name="endMonth">
                                <option selected>Mes</option>
                                <option>Enero</option>
                                <option>Febrero</option>
                                <option>Marzo</option>
                                <option>Abril</option>
                                <option>Mayo</option>
                                <option>Junio</option>
                                <option>Julio</option>
                                <option>Agosto</option>
                                <option>Septiembre</option>
                                <option>Octubre</option>
                                <option>Noviembre</option>
                                <option>Diciembre</option>
                              </select>
                              <select class="list-dt" id="endyear" name="endyear">
                                <option selected>Anio</option>
                              </select>
                            </div>
                          </div>
                        </div>
                        </c:forEach>
                        <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                        <button type="button" name="next" class="btn next action-button">Continuar</button>
                      </fieldset>
                      <fieldset>
                        <div class="form-card">
                          <h2 class="fs-title">Aptitudes</h2>
                          <form:input type="text" path="lang" name="languages" placeholder="Idiomas"/>
                          <form:input type="text" path="hability" name="skills" placeholder="Habilidades"/>
                          <form:input type="text" path="more" name="otherSkills" placeholder="Comentarios Adicionales"/>
                        </div>
                        <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                        <button type="submit" name="end" class="btn action-button">Finalizar</button>
                      </fieldset>
                    </form>
                  </div>
                </div>
                </form:form>
              </div>
            </div>
          </div>
        </div>
    </div>
  </body>
</html>