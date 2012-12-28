<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
</head>
<body>
	<div class="row-fluid">
		<c:if test="${not empty successRegister}">
		<div class="alert alert-success">
			<c:out value="${successRegister}" />
		</div>
		</c:if>
		<div class="hero-unit">
			<h1>Conquer Your Planet and Beyond!</h1>
			<br />
			<br />
            <p><a class="btn btn-primary btn-large">Play &raquo;</a></p>
          </div>
	</div>
</body>
</html>