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
        <!-- Script -->
        <script src="<c:url value="/assets/js/script.js"/>"></script>
        <title>ClonedIn</title>
        <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp"/>
        <div class="row h-100">
            <div class="col-sm-2 sidebar">
                <h5 class="ml-2 mt-2">Filtrar por:</h5>
                <div class="dropdown-group">
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Rubro</option>
                            <c:forEach var="cs" items="${categories}">
                                <option value="<c:out value="${cs.id}"/>"><c:out value="${cs.name}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Aptitudes</option>
                            <c:forEach var="ss" items="${skills}">
                                <option value="<c:out value="${ss.id}"/>"><c:out value="${ss.description}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Experiencia Laboral</option>
                            <option value="1">Exp 1</option>
                            <option value="2">Exp 2</option>
                            <option value="3">Exp 3</option>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <select class="form-select" aria-label="false">
                            <option selected>Graduado de</option>
                            <option value="1">Universidad A</option>
                            <option value="2">Universidad B</option>
                            <option value="3">Universidad C</option>
                        </select>
                    </div>
                    <div class="dropdown ml-2 mt-2">
                        <button class="btn btn-secondary filterbtn btn-outline-dark" type="button">
                            Filtrar
                        </button>
                    </div>
                </div>
            </div>
            <div class="col mr-2">
                <div class="d-flex justify-content-between mt-2">
                    <h3>Descubrir Perfiles</h3>
                    <a href="<c:url value="/createUser"/>"><button type="button" class="btn waves-effect" style="background-color: #459F78; color: white"><i class="bi bi-plus-square pr-2"></i>Agregar Perfil</button></a>
                </div>
                <div class="container-fluid">
                    <div class="row row-cols-1 row-cols-md-4 g-4 mt-2" style="background: #F2F2F2">
                        <c:forEach var="us" items="${users}">
                            <div class="col mb-4">
                                <div class="card h-100 mt-1">
                                    <a class="text-decoration-none" href="<c:url value="/profile/${us.id}"/>" style="color: inherit">
                                        <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Profile picture" width="100" height="200">
                                        <div class="card-body">
                                            <h5 class="card-title"><c:out value="${us.name}"/></h5>
                                            <p class="card-text"><c:out value="${us.description}"/></p>
                                        </div>
                                    </a>
                                    <div class="card-footer second bg-white text-right">
                                        <a href="<c:url value="/contact/${us.id}"/>"><button type="button" class="btn btn-outline-dark">Contactar</button></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
