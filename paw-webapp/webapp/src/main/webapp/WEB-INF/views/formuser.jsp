<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
  <head>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>"/>
    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Popper -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <!-- BootStrap JS -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
  </head>
  <body>
    <jsp:include page="../components/navbar.jsp"/>
      <div class="d-flex justify-content-between mt-2">
        <div class="card w-100 mt-2 mr-2 ml-2" style="background: #F2F2F2">
            <form id="regForm" action="">

              <h1>Register:</h1>

              <!-- One "tab" for each step in the form: -->
              <div class="tab">Name:
                <p><input placeholder="First name..." oninput="this.className = ''"></p>
                <p><input placeholder="Last name..." oninput="this.className = ''"></p>
              </div>

              <div class="tab">Contact Info:
                <p><input placeholder="E-mail..." oninput="this.className = ''"></p>
                <p><input placeholder="Phone..." oninput="this.className = ''"></p>
              </div>

              <div class="tab">Birthday:
                <p><input placeholder="dd" oninput="this.className = ''"></p>
                <p><input placeholder="mm" oninput="this.className = ''"></p>
                <p><input placeholder="yyyy" oninput="this.className = ''"></p>
              </div>

              <div class="tab">Login Info:
                <p><input placeholder="Username..." oninput="this.className = ''"></p>
                <p><input placeholder="Password..." oninput="this.className = ''"></p>
              </div>

              <div style="overflow:auto;">
                <div style="float:right;">
                  <button type="button" id="prevBtn" onclick="nextPrev(-1)">Previous</button>
                  <button type="button" id="nextBtn" onclick="nextPrev(1)">Next</button>
                </div>
              </div>

          <!-- Circles which indicates the steps of the form: -->
              <div style="text-align:center;margin-top:40px;">
                <span class="step"></span>
                <span class="step"></span>
                <span class="step"></span>
                <span class="step"></span>
              </div>
            </form>
          </div>
        </div>
  </body>
</html>
