<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<div class="modal fade" id="applicationModal" tabindex="-1" aria-labelledby="applicationModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="applicationModalLabel"><spring:message code="applicationModalTitle"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <spring:message code="applicationModalMsg"/>
      </div>
      <div class="modal-footer justify-content-center">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="white-space:normal; margin-bottom: 0.75rem; width: 200px">
          <spring:message code="closeJobOfferModalCancelButton"/>
        </button>
        <a href="<c:url value="/applyToJobOffer/${param.jobOfferId}/${param.currentPage}"/>" >
          <button class="btn btn-success" style="white-space:normal; margin-bottom: 0.75rem; width: 200px">
            <spring:message code="cancelJobOfferModalConfirmButton"/>
          </button>
        </a>
      </div>
    </div>
  </div>
</div>