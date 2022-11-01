<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="modal fade" id="closeJobOfferModal${param.jobOfferId}" tabindex="-1" aria-labelledby="closeJobOfferLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="closeJobOfferLabel"><spring:message code="closeJobOfferModalTitle"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <spring:message code="closeJobOfferModalMsg"/>
      </div>
      <div class="modal-footer justify-content-center">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="white-space:normal; margin-bottom: 0.75rem; width: 200px">
          <spring:message code="closeJobOfferModalCancelButton"/>
        </button>
        <a href="<c:url value="/closeJobOffer/${param.jobOfferId}?eid=${param.enterpriseId}"/>" >
          <button class="btn btn-danger" style="white-space:normal; margin-bottom: 0.75rem; width: 200px">
            <spring:message code="cancelJobOfferModalConfirmButton"/>
          </button>
        </a>
      </div>
    </div>
  </div>
</div>
