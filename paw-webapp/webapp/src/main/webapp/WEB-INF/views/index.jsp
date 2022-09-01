<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!--QUE LA LISTA NO TENGA FONDO Y QUE CADA ITEM SE HAGA DROPDOWN
SINO - ALTERNATIVA: DEJAR UNA LISTA FIJA PARA EL FILTRADO (CADA TITULO SEA UNA LISTA CON CHECKBOXES COMO MERCADOLIBRE)-->

<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <!--icons-->
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
        <div class="row h-100">
            <div class="col-sm-2 sidebar">
                <div class="dropdown-group">
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            Area de conocimiento
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            <li><a class="dropdown-item" href="#">Area 1</a></li>
                            <li><a class="dropdown-item" href="#">Area 2</a></li>
                            <li><a class="dropdown-item" href="#">Area 3</a></li>
                        </ul>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton2"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            Aptitudes
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            <li><a class="dropdown-item" href="#">Aptitud 1</a></li>
                            <li><a class="dropdown-item" href="#">Aptitud 2</a></li>
                            <li><a class="dropdown-item" href="#">Aptitud 3</a></li>
                        </ul>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton3"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            Experiencia Laboral
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            <li><a class="dropdown-item" href="#">Exp 1</a></li>
                            <li><a class="dropdown-item" href="#">Exp 1</a></li>
                            <li><a class="dropdown-item" href="#">Exp 3</a></li>
                        </ul>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton4"
                                data-bs-toggle="dropdown" aria-expanded="false">
                            Graduado de
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            <li><a class="dropdown-item" href="#">Universidad A</a></li>
                            <li><a class="dropdown-item" href="#">Universidad B</a></li>
                            <li><a class="dropdown-item" href="#">Universidad C</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col mr-2">
                <div class="d-flex justify-content-between mt-2">
                    <h3>Descubrir Perfiles</h3>
                    <!--<button type="button" class="btn btn-outline-secondary waves-effect"><i class="bi bi-star pr-2"></i>Destacados</button>-->
                    <button type="button" class="btn waves-effect" style="background-color: #459F78; color: white"><i class="bi bi-plus-square pr-2"></i>AGREGAR PERFIL</button>
                </div>
                <div class="card w-100 mt-2" style="background: #F2F2F2">
                    <div class="card-deck m-2">
                        <c:forEach begin = "1" end = "3">
                            <div class="card">
                                <img class="card-img-top small" src="<c:url value="/assets/images/naruto.jpg"/>" alt="Profile picture" width="100" height="200">
                                <div class="card-body">
                                    <h5 class="card-title">Card title</h5>
                                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                                </div>
                                <div class="card-footer bg-white text-right">
                                    <!-- <button type="button" class="btn btn-outline-dark"><i class="bi bi-star pr-2" aria-hidden="true"></i></button> -->
                                    <a href="<c:url value="/formenterprise"/>"><button type="button" class="btn btn-outline-dark">Contactar</button></a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
