<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title><sitemesh:write property="title">Web Civ!</sitemesh:write></title>
<script type="text/javascript">
	var dojoConfig = {
		async : true,
		parseOnLoad : true,
		packages : [ {
			name : "webCiv",
			location : "<c:url value="/js" /> "
		} ]
	};
</script>
<link rel="stylesheet"
	href="<c:url value="https://ajax.googleapis.com/ajax/libs/dojo/1.8.0/dijit/themes/tundra/tundra.css"/>" />

<sitemesh:write property="head" />
</head>
<body class="tundra">
	<!-- Load Dojo -->
	<script src="//ajax.googleapis.com/ajax/libs/dojo/1.8.0/dojo/dojo.js" 
		data-dojo-config="async: true, packages: [ {
            name: 'webCiv',
            location: location.pathname.replace(/\/[^/]+$/, '') + '/js'
        } ]"></script>
        
     <sitemesh:write property="body" />
</body>
</html>