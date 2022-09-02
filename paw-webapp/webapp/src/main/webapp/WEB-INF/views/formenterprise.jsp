<%--
  Created by IntelliJ IDEA.
  User: Malena Vasquez
  Date: 8/31/2022
  Time: 9:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>
  <!--icons-->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
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
                            <div class="mb-3">
                                <label for="controlSelect">Rubro</label>
                                <select class="form-control" id="controlSelect">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                    <option>5</option>
                                </select>
                            </div>
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
                                <label for="inputsal" class="form-label">salario</label>
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
                    <button type="button" class="btn btn-outline-dark">Contactar</button>
                </form>
            </div>
        </div>
    </div>
    <!-- JavaScript Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>

</body>
</html>
