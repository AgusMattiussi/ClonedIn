<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>"/>
    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Popper -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <!-- BootStrap JS -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
    <body>
        <jsp:include page="../components/navbar.jsp"/>
        <div class="d-flex justify-content-between mt-2">
            <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
                <form class="mr-2 ml-2 mt-2">
                    <h3>Contactarse con NOMBRE USUARIO</h3>
                    <div class="row">
                        <div class="col-sm-6">
                            <h5>Infomacion basica</h5>
                            <div class="mb-3">
                                <label for="inputEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="inputEmail" aria-describedby="emailHelp">
                                <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                            </div>
                            <div class="mb-3">
                                <label for="inputname" class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="inputname">
                            </div>
                            <div class="mb-3">
                                <label for="textarea">Descripcion de la empresa</label>
                                <textarea class="form-control" id="textarea" rows="2"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="inputzone" class="form-label">Ubicacion</label>
                                <input type="text" class="form-control" id="inputzone">
                            </div>
<%--                            <div class="mb-3">--%>
<%--                                <label for="controlSelect">Rubro</label>--%>
<%--                                <select class="form-control" id="controlSelect">--%>
<%--                                    <option>1</option>--%>
<%--                                    <option>2</option>--%>
<%--                                    <option>3</option>--%>
<%--                                    <option>4</option>--%>
<%--                                    <option>5</option>--%>
<%--                                </select>--%>
<%--                            </div>--%>
                        </div>
                        <div class="col-sm-6">
                            <h5>Oferta de trabajo</h5>
                            <div class="mb-3">
                                <label for="inputposition" class="form-label">Puesto</label>
                                <input type="text" class="form-control" id="inputposition">
                            </div>
                            <div class="mb-3">
                                <label for="textarea1">Descripcion de la oferta</label>
                                <textarea class="form-control" id="textarea1" rows="4"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="inputsal" class="form-label">Salario</label>
                                <input type="text" class="form-control" id="inputsal">
                            </div>
                            <div class="mb-3">
                                <label for="controlSelect1">Rubro</label>
                                <select class="form-control" id="controlSelect1">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                    <option>5</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <button type="button" class="btn btn-outline-dark">Contactar</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
