<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:write property="title">Web Civ!</sitemesh:write></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	
	<!-- Twitter Bootstrap -->
	<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
	<link href="<c:url value="css/bootstrap.min.css" />" rel="stylesheet" />
	
	
	<!-- Make IE work! -->
	<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	
	
	<sitemesh:write property="head" />
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="<c:url value="/"/>">WebCiv</a>
          <p class="navbar-text pull-right">
              Logged in as <a href="#" class="navbar-link">Username</a>
		  </p>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li><a href="<c:url value="/"/>" >Home</a></li>
              <li><a href="<c:url value="/about"/>">About</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    <div class="container-fluid">
      <sitemesh:write property="body" />
      <hr>

      <footer>
        <p>&copy; Christopher Hotchkiss 2012</p>
        <p>Template Powered by <a href="http://twitter.github.com/bootstrap/">Bootstrap</a> by <a href="https://twitter.com/">Twitter</a></p>
      </footer>

    </div><!--/.fluid-container-->

	<!-- Load Javascript last -->
	<script data-main="<c:url value="js/webCiv"/>" src="<c:url value="js/require.js"/>" ></script>
</body>
</html>