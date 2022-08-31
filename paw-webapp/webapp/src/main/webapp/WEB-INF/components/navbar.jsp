<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- FIX: VER COMO ESPACIAR LOS COMPONENTES EN EL NAVBAR (VER justify-content-between Y XQ NO TOMA EL CSS) -->

<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="../assets/style.css">
    </head>
    <body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-auto" style="background-color: #04704C;">
        <a class="navbar-brand" href="#">
            <img src="../images/logo.png" width="30" height="30" class="d-inline-block align-top" alt="">
            NOMBRE
        </a>
        <!--volver a inicio & FALTAN NUESTRO NOMBRE Y NUESTRO LOGO-->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="#">Descubrir Perfiles<span class="sr-only">(current)</span></a>
                </li>
                <!--volver a la pagina de los perfiles-->
            </ul>
        </div>
    </nav>
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body>
</html>
