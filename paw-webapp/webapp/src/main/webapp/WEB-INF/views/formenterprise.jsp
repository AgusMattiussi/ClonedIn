<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
                            <h2><strong>Contactarse con el (nombre-usuario)</strong></h2>
                            <p>Asegurate de llenar todos los campos antes de avanzar</p>
                            <div class="row">
                                <div class="col-md-12 mx-0">
                                    <form id="msform">
                                        <!-- progressbar -->
                                        <ul id="progressbar">
                                            <li class="active" id="personal"><strong>Informacion Basica</strong></li>
                                            <li id="experience"><strong>Oferta Laboral</strong></li>
                                        </ul>
                                        <!-- fieldsets -->
                                        <fieldset>
                                            <div class="form-card">
                                                <h2 class="fs-title">Informacion Basica</h2>
                                                <input type="email" name="email" placeholder="Email"/>
                                                <input type="text" name="name" placeholder="Nombre"/>
                                                <div class="row">
                                                    <div class="col-6">
                                                        <label for="ControlFile">Elegir foto de perfil</label>
                                                    </div>
                                                    <div class="col-6">
                                                        <input type="file" class="form-control-file" id="ControlFile">
                                                    </div>
                                                </div>
                                                <input type="text" name="location" placeholder="Ubicacion"/>
                                                <input type="text" name="description" placeholder="Descripcion"/>
                                                <div class="row">
                                                    <div class="col-6">
                                                        <label class="area">Rubro Empresarial</label>
                                                    </div>
                                                    <div class="col-6">
                                                        <select class="list-dt" id="area" name="area">
                                                            <option selected>Ninguno</option>
                                                            <option>Tecnologia</option>
                                                            <option>Medicina</option>
                                                            <option>Computacion</option>
                                                            <option>Empresarial</option>
                                                            <option>Educacion</option>
                                                            <option>Bancario</option>
                                                            <option>Otro</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <button type="button" name="next" class="btn next action-button">Continuar</button>
                                        </fieldset>
                                        <fieldset>
                                            <div class="form-card">
                                                <h2 class="fs-title">Oferta de trabajo</h2>
                                                <input type="text" name="position" placeholder="Puesto"/>
                                                <input type="text" name="description" placeholder="Descripcion"/>
                                                <input type="text" name="salary" placeholder="Salario"/>
                                                <div class="row">
                                                    <div class="col-6">
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
                                            <button type="button" name="previous" class="btn previous action-button-previous">Volver</button>
                                            <button type="button" name="next" class="btn next action-button">Continuar</button>
                                        </fieldset>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
