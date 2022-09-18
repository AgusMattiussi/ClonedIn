<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
    <head>
        <%@include file="../components/imports.jsp"%>
        <!-- CSS -->
        <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>">
        <title><c:out value="${user.name}"/> | ClonedIn</title>
    </head>
    <body>
    <jsp:include page="../components/navbar.jsp"/>
    <div class="d-flex justify-content-between mt-2">
    <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
        <div class="row">
            <div class="col-sm-3">
                <div class="card ml-2 mt-2 mb-2 h-70">
                    <img class="card-img-top small" src="<c:url value="/assets/images/default_profile_picture.png"/>" alt="Card image cap">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Descripcion de <c:out value="${user.name}"/></h5>
                    </div>
                    <div class="card-footer bg-white text-center">
                        <p class="card-text">Actualmente: <c:out value="${user.currentPosition}"/></p>
                        <p class="card-text"><c:out value="${user.description}"/></p>
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
                        <!-- cambiar a un forEach de experienceS -->
                        <%--<p class="card-text">Trabaje en <c:out value="${experience.enterpriseName}"/>,
                            como <c:out value="${experience.position}"/>. <c:out value="${experience.description}"/>
                        </p>--%>
                    </div>
                </div>
                </div>
                <div class="row mr-2">
                <div class="card mt-2">
                    <div class="card-body pb-0">
                        <h5 class="card-title">Educacion</h5>
                    </div>
                    <div class="card-footer bg-white text-left">
                        <p class="card-text"><c:out value="${user.education}"/></p>
                    </div>
                </div>
<%--                </div>--%>
<%--                <div class="row mr-2">--%>
<%--                <div class="card mt-2">--%>
<%--                    <div class="card-body pb-0">--%>
<%--                        <h5 class="card-title">Aptitudes</h5>--%>
<%--                    </div>--%>
<%--                    <div class="card-footer bg-white text-left">--%>
<%--                        <p class="card-text">Aptitudes del usuario</p>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                </div>--%>
<%--            </div>--%>
        </div>
    </div>
    </div>
    </body>
</html>
