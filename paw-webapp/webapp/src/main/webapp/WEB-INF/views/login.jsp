<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../components/imports.jsp"%>
    <!-- Script -->
    <script src="<c:url value="/assets/js/steps.js"/>"></script>
    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
    <title><spring:message code="login_pagetitle"/></title>
</head>
<body>
<jsp:include page="../components/navbarEmpty.jsp"/>
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
                                        <c:if test="${param.error != null}">
                                            <div id="error" class="formError" style="color: red">
                                                <spring:message code="message.badCredentials"/>
                                            </div>
                                        </c:if>
                                        <div class="d-flex">
                                            <div style="margin-top: 0.4rem; margin-left: 10px">
                                                <form:checkbox path="remember_me"/>
                                            </div>
                                            <div style="margin-left: 15px; margin-top: 1.2rem;">
                                                <spring:message code="remember_me"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <button type="submit" name="end" class="btn action-button" style="width: fit-content">
                                            <spring:message code="login_buttonmsg"/>
                                        </button>
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