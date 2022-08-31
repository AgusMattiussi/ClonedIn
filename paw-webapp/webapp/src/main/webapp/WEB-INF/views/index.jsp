<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!--QUE LA LISTA NO TENGA FONDO Y QUE CADA ITEM SE HAGA DROPDOWN
SINO - ALTERNATIVA: DEJAR UNA LISTA FIJA PARA EL FILTRADO (CADA TITULO SEA UNA LISTA CON CHECKBOXES COMO MERCADOLIBRE)-->

<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <!--icons-->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp"/>
        <div class="row h-100">
            <div class="col-sm-2" style="background-color: #459F78;">
                <div class="list-group list-group-flush ml-2 mt-2">
                    <p style="color: white">FILTRAR POR</p>
                    <a href="#" class="list-group-item list-group-item-action">Area de conocimiento</a>
                    <a href="#" class="list-group-item list-group-item-action">Aptitudes</a>
                    <a href="#" class="list-group-item list-group-item-action">Experiencia Laboral</a>
                    <a href="#" class="list-group-item list-group-item-action disabled">Graduado</a>
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
                                    <button type="button" class="btn btn-outline-dark">Contactar</button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>


        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body>
</html>
