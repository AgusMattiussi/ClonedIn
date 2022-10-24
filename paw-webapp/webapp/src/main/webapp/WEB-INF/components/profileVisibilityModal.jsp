<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal fade" id="profileVisibilityModal" tabindex="-1" aria-labelledby="profileVisibilityModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="profileVisibilityModalLabel"><spring:message code="answerModalTitle"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <spring:message code="profileVisbilityModalMsg"/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          <spring:message code="mailModalClose"/>
        </button>
      </div>
    </div>
  </div>
</div>
