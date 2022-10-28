<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>


<nav class="d-flex justify-content-center align-items-center">
  <ul class="pagination">
    <li class="page-item">
      <a class="page-link text-decoration-none" style="color: black" href="<c:url value = "${param.path}page=1"/>">
        <spring:message code="indexPaginationFirst"/>
      </a>
    </li>
    <c:forEach var="i" begin="1" end="${param.pages}">
      <li class="page-item">
        <c:choose>
          <c:when test="${param.currentPage == i}">
            <a class="page-link text-decoration-none" style="color: black; font-weight: bold;" href="<c:url value="${param.path}page=${i}"/>">
              <c:out value="${i}"/>
            </a>
          </c:when>
          <c:otherwise>
            <a class="page-link text-decoration-none" style="color: black" href="<c:url value="${param.path}page=${i}"/>">
              <c:out value="${i}"/>
            </a>
          </c:otherwise>
        </c:choose>
      </li>
    </c:forEach>
    <li class="page-item">
      <a class="page-link text-decoration-none" style="color: black" href="<c:url value = "${param.path}page=${param.pages}"/>">
        <spring:message code="indexPaginationEnd"/>
      </a>
    </li>
  </ul>
</nav>