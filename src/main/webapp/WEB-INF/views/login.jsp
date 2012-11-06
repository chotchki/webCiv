<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
</head>
<body>
	<div class="row-fluid">
		<div class="span3 offset2">
			<form action="<c:url value="/login/form"/>" method="POST">
    			<fieldset>
    				<legend>Login</legend>
    				<label>Username</label>
    				<input type="text" placeholder="">
    				<label>Password</label>
    				<input type="password" placeholder="">
    				<br />
    				<button type="submit" class="btn">Submit</button>
    			</fieldset>
    		</form>
		</div>
		<div class="span2" style="text-align: center">
			<h3>- Or -</h3>
		</div>
		<div class="span3">
			<h4>Register</h4>
		</div>
	</div>
</body>
</html>