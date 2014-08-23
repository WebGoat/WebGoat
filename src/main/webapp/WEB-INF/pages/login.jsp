<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Login Page</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
        <style type="text/css">
            body {
                padding-top: 40px;
                padding-bottom: 40px;
                background-color: #f5f5f5;
            }

            .form-signin {
                max-width: 300px;
                padding: 19px 29px 29px;
                margin: 0 auto 20px;
                background-color: #fff;
                border: 1px solid #e5e5e5;
                -webkit-border-radius: 5px;
                -moz-border-radius: 5px;
                border-radius: 5px;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
            }
            .form-signin .form-signin-heading,
            .form-signin .checkbox {
                margin-bottom: 10px;
            }
            .form-signin input[type="text"],
            .form-signin input[type="password"] {
                font-size: 16px;
                height: auto;
                margin-bottom: 15px;
                padding: 7px 9px;
            }

        </style>
    </head>
    <body onload='document.loginForm.username.focus();'>

        <div class="container">
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="msg">${msg}</div>
            </c:if>
            <form class="form-signin" name='loginForm'
                  action="<c:url value='j_spring_security_check' />" method='POST'>
                <h2 class="form-signin-heading">Please sign in</h2>
                <input type="text" class="input-block-level" placeholder="Email address" name='username'>
                <input type="password" class="input-block-level" placeholder="Password"  name='password'>

                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}" />
                <button class="btn btn-large btn-primary" type="submit">Sign in</button>
            </form>
            <div class="panel panel-info" style="max-width: 300px; margin: 0 auto 20px;">
                <div class="panel-heading">
                    Login with one of the following accounts
                </div>
                <div class="panel-body">
                    <!-- Table -->
                    <table class="table table-bordered">
                        <thead>
                            <tr><td>Account</td><td>User</td><td>Password</td></tr>
                        </thead>
                        <tbody>
                            <tr><td>Webgoat User</td><td>guest</td><td>guest</td></tr>
                            <tr><td>Webgoat Admin</td><td>webgoat</td><td>webgoat</td></tr>
                            <tr><td>Server Admin</td><td>server</td><td>server</td></tr>
                        </tbody>
                    </table>


                </div>
            </div>
        </div> <!-- /container -->



    </body>
</html>