<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
    <title>Registrate | ClonedIn</title>
    <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
    <body>
        <jsp:include page="../components/navbar0.jsp"/>
        <div class="d-flex justify-content-between mt-2">
            <div class="container-fluid">
                <div class="row justify-content-center mt-0">
                    <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
                        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                            <h2><strong>Registrarse</strong></h2>
                            <p>Asegurate de llenar todos los campos antes de avanzar</p>
                            <div class="row">
                                <div class="col-md-12 mx-0">
                                    <div id="msform">
                                        <c:url value="/createEnterprise" var="postPath"/>
                                        <form:form modelAttribute="enterpriseForm" action="${postPath}" method="post">
                                            <fieldset>
                                                <div class="form-card">
                                                    <h2 class="fs-title">Informacion Basica</h2>
                                                    <form:input type="email" path="email" placeholder="Email *"/>
                                                    <form:errors path="email" cssClass="formError" element="p"/>
                                                    <form:input type="text" path="name" placeholder="Nombre *"/>
                                                    <form:errors path="name" cssClass="formError" element="p"/>
                                                    <form:input type="password" path="password" placeholder="Password *"/>
                                                    <form:errors path="password" cssClass="formError" element="p"/>
                                                    <form:input type="password" path="repeatPassword" placeholder="Repeat Password *"/>
                                                    <form:errors path="repeatPassword" cssClass="formError" element="p"/>
    <%--                                                <div class="row">--%>
    <%--                                                    <div class="col-4">--%>
    <%--                                                        <label for="ControlFile">Elegir foto de perfil</label>--%>
    <%--                                                    </div>--%>
    <%--                                                    <div class="col-8">--%>
    <%--                                                        <input type="file" class="form-control-file" id="ControlFile">--%>
    <%--                                                    </div>--%>
    <%--                                                </div>--%>
                                                    <form:input type="text" path="city" placeholder="Ubicacion"/>
                                                    <form:input type="text" path="description" placeholder="Descripcion *"/>
                                                    <form:errors path="description" cssClass="formError" element="p"/>
                                                    <div class="d-flex">
                                                        <label class="area" style="margin-top: 1.2rem; margin-left: 10px">Rubro</label>
                                                        <div style="margin-left: 15px">
                                                            <select class="list-dt ml-auto" id="area" name="area">
                                                                <option selected>Ninguno</option>
                                                                <option>Otro</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <p>(*) Los campos son requeridos</p>
                                                <button type="submit" name="end" class="btn next action-button">REGISTRAR</button>
                                            </fieldset>
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
