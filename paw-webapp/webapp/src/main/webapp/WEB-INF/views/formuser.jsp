<%--
  Created by IntelliJ IDEA.
  User: SolChiSol
  Date: 02/09/22
  Time: 13:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Bootstrap -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/style.css"/>
  <!--icons-->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
  <!--java script-->
  <link rel="stylesheet" href="<c:url value="/assets/JS/steps.js"/>">
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
</div>
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>

</body>
</html>
