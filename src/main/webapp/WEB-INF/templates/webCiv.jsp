<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:write property="title">Web Civ!</sitemesh:write></title>
	<link rel="stylesheet" href="<c:url value="https://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dijit/themes/nihilo/nihilo.css"/>" />
	<script type="text/javascript">
		var dojoConfig = {
			async: true,
			parseOnLoad: true,
			packages: [{
				name: "pgGallery",
				location: location.pathname.replace(/\/[^/]+$/, '') + '/js'
				}]
		};
</script>
	<sitemesh:write property="head" />
</head>
<body class="nihilo" style="display:none">
	<!-- Load Dojo -->
	<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/dojo.js"></script>
    <script type="text/javascript">
    	require(["dojo/ready", "dojo/dom-style", "dojo/_base/window", "dojo/parser"], function(ready, style, win){
    		ready(function(){
    			style.set(win.body(),"display","block");
    		});
    	});
    </script>
     <sitemesh:write property="body" />
</body>
</html>