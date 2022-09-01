<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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
    <div class="card w-100 mt-2" style="background: #F2F2F2">
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 h-100">
                    <img class="card-img-top small" src="<c:url value="/assets/images/naruto.jpg"/>" alt="Card image cap">
                    <div class="card-body">
                        <h5 class="card-title">Aptitudes</h5>
                    </div>
                    <div class="card-footer bg-white text-right">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row">
                <div class="card mr-2 mt-2">
                    <div class="card-body">
                        <h5 class="card-title">Experiencia</h5>
                    </div>
                    <div class="card-footer bg-white text-right">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
                <div class="row">
                <div class="card mr-2 mt-2">
                    <div class="card-body">
                        <h5 class="card-title">Educacion</h5>
                    </div>
                    <div class="card-footer bg-white text-right">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
                <div class="row">
                <div class="card mr-2 mt-2">
                    <div class="card-body">
                        <h5 class="card-title">Aptitudes</h5>
                    </div>
                    <div class="card-footer bg-white text-right">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>


    <!-- JavaScript Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
    </body>
</html>
