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
			<c:if test="${not empty loginFailed}">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<c:out value="${loginFailed}" />
			</div>
			</c:if>
			<form action="<c:url value="/signin/authenticate"/>" method="POST">
    			<fieldset>
    				<legend>Login Below</legend>
    				<label>Username</label>
    				<input type="text" name="j_username" placeholder="" autofocus="autofocus">
    				<label>Password</label>
    				<input type="password" name="j_password" placeholder="">
    				<br />
    				<button type="submit" class="btn">Submit</button>
    			</fieldset>
    		</form>
		</div>
		
		<div class="span3 offset1">
			<form action="<c:url value="/register"/>" method="POST">
    			<fieldset>
    				<legend>Register</legend>
    				<span class="help-block"><c:out value="${error}" /></span>
    				<div class="control-group <c:if test="${not empty errorUsername}">error</c:if>">
    					<label>Username</label>
    					<input type="text" name="user.username" placeholder="">
    					<span class="help-block"><c:out value="${errorUsername}" /></span>
    					<br />
    				</div>
    				
    				<div class="control-group <c:if test="${(not empty errorPassword) or (not empty errorRetypePassword)}">error</c:if>">
    					<label>Password</label>
    					<input type="password" name="user.password" placeholder="">
    					<span class="help-block"><c:out value="${errorPassword}" /></span>
    					<br />
    					
    					<label>Retype Password</label>
    					<input type="password" name="retypePassword" placeholder="">
    					<span class="help-block"><c:out value="${errorRetypePassword}" /></span>
    				</div>
    				
    				<br />
    				<button type="submit" class="btn">Submit</button>
    			</fieldset>
    		</form>
		</div>
	</div>
</body>
</html>