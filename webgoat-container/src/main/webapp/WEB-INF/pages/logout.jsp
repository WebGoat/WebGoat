<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Logout Page</title>
        <!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
        <!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
        <!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
        <!--[if gt IE 8]><!-->

        <!--  CSS -->
        <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon"/>
        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css"/>
        <!-- Fonts from Font Awsome -->
        <link rel="stylesheet" href="css/font-awesome.min.css"/>
        <!-- CSS Animate -->
        <link rel="stylesheet" href="css/animate.css"/>
        <!-- Custom styles for this theme -->
        <link rel="stylesheet" href="css/main.css"/>
        <!--  end of CSS -->


    </style>
</head>
<body onload='document.loginForm.username.focus();'>
    <section id="container" ng-controller="goatLesson">
        <header id="header">
            <!--logo start-->
            <div class="brand">
                <a href="${pageContext.request.contextPath}/start.mvc" class="logo"><span>Web</span>Goat</a>
            </div>
            <!--logo end-->
            <div class="toggle-navigation toggle-left">

            </div><!--toggle navigation end-->
            <div class="lessonTitle" >
                <h1 id="lessonTitle">Logout</h1>
            </div><!--lesson title end-->

        </header>
        <section class="main-content-wrapper">

            <section id="main-content" >
                <c:if test="${not empty error}">
                    <div class="error">${error}</div>
                </c:if>
                <c:if test="${not empty msg}">
                    <div class="msg">${msg}</div>
                </c:if>
                <br/><br/>
                <div class="alert alert-success" role="alert" style="width: 400px;">
                    You have logged out successfully
                </div>
                
                <hr/>
                <h4>Click here if you would like to log back in: <a href="<c:url value="login.mvc" />" > Login</a></h4>


            </section>
        </section> 
    </section>


</body>
</html>



