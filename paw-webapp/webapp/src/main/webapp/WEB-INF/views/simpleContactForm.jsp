<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <!-- Bootstrap -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
  <!-- Icons -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css">
  <!-- JQuery -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <!-- Popper -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
  <!-- BootStrap JS -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
  <!-- Script -->
  <%--    <script src="<c:url value="/assets/js/steps.js"/>"></script>--%>
  <title>Agrega tu perfil | ClonedIn</title>
  <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<div class="d-flex justify-content-between mt-2">
  <div class="container-fluid">
    <div class="row justify-content-center mt-0">
      <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
        <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
          <h2><strong>Contactate</strong></h2>
          <p>Asegurate de llenar todos los campos antes de avanzar</p>
          <div class="row">
            <div class="col-md-12 mx-0">
              <div id="msform">
                <c:url value="/contact" var="postPath"/>
                <form:form modelAttribute="simpleContactForm" action="${postPath}" method="post">
                  <fieldset>
                    <div class="form-card">
                      <h2 class="fs-title">Informacion Personal</h2>
                      <form:input type="text" path="subject" placeholder="Asunto"/>
                      <form:input type="text" path="message" placeholder="Mensaje"/>
                      <div>
                        <input type="submit" value="Enviar"/>
                      </div>
                    </div>
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
