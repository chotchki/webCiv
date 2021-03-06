<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
</head>
<body>
	<div class="row-fluid">
		<c:if test="${not empty successRegister}">
		<div class="alert alert-success">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<c:out value="${successRegister}" />
		</div>
		</c:if>
		<div class="hero-unit">
			<h1>Conquer Your Planet and Beyond!</h1>
			<br />
			<br />
			<sec:authorize access="isAnonymous()">
				<p><a href="<c:url value="/signin" />" class="btn btn-primary btn-large">Register / Sign in</a></p>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
            	<p><a class="btn btn-primary btn-large">Play &raquo;</a></p>
            </sec:authorize>
          </div>
	</div>
</body>
</html>