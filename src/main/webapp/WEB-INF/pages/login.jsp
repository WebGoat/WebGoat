<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Login Page</title>
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
                <h1 id="lessonTitle">Please login</h1>
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
                <form role="form" name='loginForm' action="<c:url value='j_spring_security_check' />" method='POST' style="width: 400px;">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Username</label>
                        <input type="text" class="form-control" id="exampleInputEmail1" placeholder="Username" name='username'>
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">Password</label>
                        <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password" name='password'>
                    </div>


                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}" />
                    <button class="btn btn-large btn-primary" type="submit">Sign in</button>
                </form>
                <br/><br/>
                <h4>The following accounts are built into Webgoat</h4>
                <table class="table table-bordered" style="width:400px;">
                    <thead>
                        <tr class="warning"><th>Account</th><th>User</th><th>Password</th></tr>
                    </thead>
                    <tbody>
                        <tr><td>Webgoat User</td><td>guest</td><td>guest</td></tr>
                        <tr><td>Webgoat Admin</td><td>webgoat</td><td>webgoat</td></tr>
                    </tbody>
                </table>
                <br/><br/>


            </section>
        </section> 
    </section>


</body>
</html>