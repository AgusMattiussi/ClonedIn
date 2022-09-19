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
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <title><spring:message code="login_pagetitle"/></title>
    <link rel="icon" type="image/x-icon" href="<c:url value="/assets/images/tabLogo.png"/>">
</head>
<body>
<jsp:include page="../components/navbar0.jsp"/>
<div class="d-flex justify-content-between mt-2">
    <div class="container-fluid">
        <div class="row justify-content-center mt-0">
            <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
                <div class="card px-0 pt-4 pb-0 mt-3 mb-3"  style="background: #F2F2F2">
                    <h2><strong><spring:message code="login_title"/></strong></h2>
                    <spring:message code="login_mail" var="emailPlaceholder"/>
                    <spring:message code="login_pass" var="passPlaceholder"/>
                    <div class="row">
                        <div class="col-md-12 mx-0">
                            <div id="msform">
                                <c:url value="/login" var="loginUrl"/>
                                <form:form modelAttribute="loginForm" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
                                <fieldset>
                                    <div class="form-card">
                                        <form:input type="email" path="email" placeholder="${emailPlaceholder}"/>
                                        <form:errors path="email" cssClass="formError" element="p"/>
                                        <form:input type="password" path="password" placeholder="${passPlaceholder}"/>
                                        <form:errors path="password" cssClass="formError" element="p"/>
                                        <label>
<%--                                           <form:checkbox path="remember_me" type="checkbox"/>--%>
                                           <spring:message code="remember_me"/>
                                        </label>
                                    </div>
                                    <div>
                                        <button type="submit" name="end" class="btn action-button"><spring:message code="login_buttonmsg"/></button>
                                    </div>
                                    <div>
                                        <p><spring:message code="login_message"/></p>
                                        <div class="row">
                                            <div class="col">
                                                <a href="<c:url value="/createUser"/>"><button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; font-size:40px; margin-top: 5px"><i class="bi bi-person large"></i></button></a>
                                                <p><spring:message code="login_user"/></p>
                                            </div>
                                            <div class="col">
                                                <a href="<c:url value="/createEnterprise"/>"><button type="button" class="btn waves-effect" style="background-color: #459F78; color: white; font-size:40px; margin-top: 5px"><i class="bi bi-building large"></i></button></a>
                                                <p><spring:message code="login_company"/></p>
                                            </div>
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