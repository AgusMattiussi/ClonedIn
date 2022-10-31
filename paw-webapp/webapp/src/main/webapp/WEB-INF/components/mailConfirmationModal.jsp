<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal fade" id="mailConfirmationModal" tabindex="-1" aria-labelledby="mailConfirmationModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="mailConfirmationModalLabel"><spring:message code="mailModalTitle"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <spring:message code="mailModalMsg"/>
      </div>
    </div>
  </div>
</div>
