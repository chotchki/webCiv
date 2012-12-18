<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="<c:url value="/css/auth-buttons.css" />" />
	<script type="text/javascript">
		require(["jquery"], function($){
			$('#google').click(function(){
				$('openid_identifier').val('https://www.google.com/accounts/o8/id');
				
			});
			$('#yahoo').click(function(){
				$('openid_identifier').val('https://www.yahoo.com');
				
			});
		});
	</script>
</head>
<body>
	<div class="row-fluid">
		<div class="span3 offset2">
			<!-- <p><a class="btn-auth btn-facebook" href="#button">Sign in with <b>Facebook</b></a></p>
			<p><a class="btn-auth btn-twitter" id="twitter" href="#">Sign in with <b>Twitter</b></a></p>-->
			<!--<form action="<c:url value="/j_spring_openid_security_check"/>" method="POST">
				<input type="hidden" id="openid_identifier" name="openid_identifier" maxlength="255" />
				<p><a class="btn-auth btn-google" id="google" href="#">Sign in with <b>Google</b></a></p>
				<p><a class="btn-auth btn-yahoo" id="yahoo" href="#">Sign in with <b>Yahoo!</b></a></p>
			</form>-->
			
			<form action="<c:url value="/login/form"/>" method="POST">
    			<fieldset>
    				<legend>Login Below</legend>
    				<label>Username</label>
    				<input type="text" name=j_username" placeholder="">
    				<label>Password</label>
    				<input type="password" name="j_password" placeholder="">
    				<br />
    				<button type="submit" class="btn">Submit</button>
    			</fieldset>
    		</form>
		</div>
		
		<div class="span3 offset1">
			<form action="<c:url value="/login/form"/>" method="POST">
    			<fieldset>
    				<legend>Register</legend>
    				<label>Username</label>
    				<input type="text" name="username" placeholder="">
    				<label>Password</label>
    				<input type="password" name="password" placeholder="">
    				<label>Retype Password</label>
    				<input type="password" name="confirmpassword" placeholder="">
    				<br />
    				<button type="submit" class="btn">Submit</button>
    			</fieldset>
    		</form>
		</div>
	</div>
</body>
</html>