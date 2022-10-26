<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <%@include file="../components/imports.jsp"%>
  <!-- CSS -->
  <link rel="stylesheet" href="<c:url value="/assets/css/steps.css"/>"/>
  <title><spring:message code="contactFormPageTitle"/></title>
</head>
<body>
  <jsp:include page="../components/navbar.jsp">
    <jsp:param name="id" value="${loggedUserID}" />
  </jsp:include>
  <div class="d-flex justify-content-between mt-2">
    <div class="container-fluid">
      <div class="row justify-content-center mt-0">
        <div class="col-11 col-sm-9 col-md-7 col-lg-6 text-center p-0 mt-3 mb-2">
          <div class="card px-0 pt-4 pb-0 mt-3 mb-3" style="background: #F2F2F2">
            <h2><strong><spring:message code="contactFormTitle"/> <c:url value="${user.name}"/></strong></h2>
            <p><spring:message code="contactFormWarning"/></p>
            <spring:message code="contactFormSubject" var="subjectPlaceholder"/>
            <spring:message code="contactFormMessage" var="messagePlaceholder"/>
            <spring:message code="contactFormInformation" var="informationPlaceholder"/>
            <div class="row">
              <div class="col-md-12 mx-0">
                <div id="msform">
                  <c:choose>
                    <c:when test = "${jobOffers.size() == 0}">
                      <h5 class="mt-5 mb-5"><spring:message code="jobOfferFormNoJobOffersMsg"/></h5>
                      <a href="<c:url value="/profileEnterprise/${loggedUserID}"/>">
                        <button type="button" name="end" class="btn next action-button" style="width: fit-content"><spring:message code="navbarMyJobOffers"/></button>
                      </a>
                    </c:when>
                    <c:otherwise>
                      <c:url value="/contact/${user.id}" var="postPath"/>
                      <form:form modelAttribute="simpleContactForm" action="${postPath}" method="post" accept-charset="utf-8">
                        <fieldset>
                          <div class="form-card">
                            <h2 class="fs-title"><spring:message code="contactFormSubtitle"/></h2>
                            <form:input type="text" path="message" placeholder="${messagePlaceholder}"/>
                            <form:errors path="message" cssClass="formError" element="p"/>
                            <div class="d-flex">
                              <label class="area" style="margin-top: 1.2rem; margin-left: 10px"><spring:message code="contactFormJobOfferSelect"/></label>
                              <div style="margin-left: 15px; margin-top: 1.2rem;">
                                <form:select path="jobOfferId" cssClass="list-dt ml-auto">
                                  <c:forEach items="${jobOffers}" var="jobOffer">
                                    <form:option value="${jobOffer.id}">${jobOffer.position}</form:option>
                                  </c:forEach>
                                </form:select>
                                <form:errors path="jobOfferId" cssClass="formError" element="p"/>
                              </div>
                            </div>
                          </div>
                          <p><spring:message code="contactFormRequiredMsg"/></p>
                          <button type="submit" class="btn action-button" <%--data-bs-toggle="modal" data-bs-target="#mailConfirmationModal"--%>>
                            <spring:message code="contactFormButtonMsg"/>
                          </button>
                          <div class="row">
                            <a href="<c:url value="/profileUser/${user.id}"/>">
                              <button type="button" class="btn next btn-outline" style="color: #459F78"><spring:message code="returnButtonMsg"/></button>
                            </a>
                          </div>
                        </fieldset>
                      </form:form>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal -->
  <jsp:include page="../components/mailConfirmationModal.jsp"/>
</body>
</html>