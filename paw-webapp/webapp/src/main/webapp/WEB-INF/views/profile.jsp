<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
    <head>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
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
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 mb-2 h-70">
                    <img class="card-img-top small" src="<c:url value="/assets/images/naruto.jpg"/>" alt="Card image cap">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Descripcion de <c:out value="${user.name}" /></h5>
                    </div>
                    <div class="card-footer bg-white text-center">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Experiencia</h5>
                    </div>
                    <div class="card-footer bg-white text-left">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Educacion</h5>
                    </div>
                    <div class="card-footer bg-white text-left">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Aptitudes</h5>
                    </div>
                    <div class="card-footer bg-white text-left">
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.</p>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
    </div>
    </body>
</html>
