<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
         errorPage=""%>
<%@page import="org.owasp.webgoat.session.WebSession"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    WebSession webSession = ((WebSession) session.getAttribute(WebSession.SESSION));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Expires" CONTENT="0">
        <meta http-equiv="Pragma" CONTENT="no-cache">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Cache-Control" CONTENT="no-store">

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

        <!-- JS -->
        
        <script src="js/modernizr-2.6.2.min.js"></script>
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.min.js"></script>
        <![endif]-->

        <!-- Require.js used to load js asynchronously -->
        <script src="js/libs/require.min.js" data-main="js/main.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>WebGoat</title>
    </head>
    <body>
        <section id="container">
            <header id="header">
                <!--logo start-->
                <div class="brand">
                    <a href="${pageContext.request.contextPath}/welcome.mvc" class="logo"><span>Web</span>Goat</a>
                </div>
                <!--logo end-->
                <div class="toggle-navigation toggle-left">
                    <button type="button" class="btn btn-default" id="toggle-menu" data-toggle="tooltip" data-placement="right" title="Toggle Navigation">
                        <i class="fa fa-bars"></i>
                    </button>
                </div><!--toggle navigation end-->
                <div id="lesson-title-wrapper" >
                    
                </div><!--lesson title end-->
                <div class="user-nav pull-right" id="user-and-info-nav" style="margin-right: 75px;">
                    <div class="dropdown" style="display:inline">
                        <button type="button" data-toggle="dropdown" class="btn btn-default dropdown-toggle" id="user-menu" >
                            <i class="fa fa-user"></i> <span class="caret"></span>
                        </button>                   
                        <ul class="dropdown-menu dropdown-menu-left">
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="j_spring_security_logout" />">Logout</a></li>
                            <li role="presentation" class="divider"></li>     
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">User: ${user}</a></li>
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">Role: ${role}</a></li>
                            <li role="presentation" class="divider"></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="#developer-controls">Show developer controls</a></li>      
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">${version}</a></li>
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">Build: ${build}</a></li>
                        </ul>
                    </div>
                    <button type="button" id="about-button"  class="btn btn-default right_nav_button" title="About WebGoat" data-toggle="modal" data-target="#about-modal"> 
                        <i class="fa fa-info"></i>
                    </button>
                    <a href="mailto:${contactEmail}?Subject=Webgoat%20feedback" target="_top">
                        <button type="button" class="btn btn-default right_nav_button"data-toggle="tooltip" title="Contact Us">
                            <i class="fa fa-envelope"></i>
                        </button>
                    </a>


                </div>
            </header>

            <aside class="sidebar" >
                <div id="menu-container"></div>
            </aside>
            <!--sidebar left end-->

            <!--main content start-->
            <section class="main-content-wrapper">
                <section id="main-content" > <!--ng-controller="goatLesson"-->
                    <div class="row">
                        <div class="col-md-8">
                            <div class="col-md-12" align="left">
                                <div class="panel" id="help-controls">
                                    <button class="btn btn-primary btn-xs help-button" id="show-source-button">Show Source</button>
                                    <button class="btn btn-primary btn-xs help-button" id="show-solution-button">Show Solution</button>
                                    <button class="btn btn-primary btn-xs help-button" id="show-plan-button">Show Plan</button>
                                    <button class="btn btn-primary btn-xs help-button" id="show-hints-button">Show Hints</button>
                                    <button class="btn btn-xs help-button" id="restart-lesson-button">Restart Lesson</button>
                                </div>
                                <div class="lesson-hint" id="lesson-hint-container">
                                    <h4>Hints</h4>
                                    <div class="panel" >
                                        <div class="panel-body" id="lesson-hint">
                                            <span class="glyphicon-class glyphicon glyphicon-circle-arrow-left" id="show-prev-hint"></span>
                                            <span class="glyphicon-class glyphicon glyphicon-circle-arrow-right" id="show-next-hint"></span>
                                            <br/>
                                            <span id="lesson-hint-content"></span>
                                        </div>                                    
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-12" align="left">
                                <div id="lesson-progress" class="info"></div>
                                <div id="lesson-content-wrapper" class="panel">

                                </div>
                            </div>
                        </div><!--col-md-8 end-->
                        <div class="col-md-4">
                            <div class="col-md-12">
                                <div class="panel">
                                    <div class="panel-body">
                                        <div align="left">
                                            <h3>Cookies / Parameters</h3>
                                        </div>
                                        <hr />
                                        <div id="cookies-and-params">
                                            <div id="cookies-view">
                                                <h4>Cookies</h4>
                                            </div>
                                            <div id="params-view"> <!--class="paramsView"-->
                                                <h4>Params</h4>
                                            </div>
                                        </div>
                                        <div id="developer-control-container">
	                                        <div align="left">
	                                            <h3>Developer controls</h3>
	                                        </div>
	                                        <hr />
	                                        <div id="developer-controls">
	                                        
	                                        </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div><!--col-md-4 end-->         
                    </div>
                    <div id="lesson-helps-wrapper" class="panel">
                        <div class="lesson-help" id="lesson-plan-row">
                            <div class="col-md-12">
                                <h4>Lesson Plan</h4>
                                <div class="panel" >
                                    <div class="panel-body" id="lesson-plan-content">
                                        <!-- allowing jQuery to handle this one -->
                                    </div>                                    
                                </div>
                            </div>
                        </div> 
                        <div class="lesson-help" id="lesson-solution-row">
                            <div class="col-md-12">
                                <h4>Lesson Solution</h4>
                                <div class="panel">
                                    <div class="panel-body" id="lesson-solution-content">
                                    </div>                                    
                                </div>
                            </div>
                        </div> 
                        <div class="lesson-help" id="lesson-source-row">
                            <div class="col-md-12">
                                <h4>Lesson Source Code</h4>
                                <div class="panel">
                                    <div class="panel-body" id="lesson-source-content">
                                    </div>                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </section>

        </section>


        <!-- About WebGoat Modal -->
        <div class="modal" id="about-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <jsp:include page="../pages/about.jsp"/> 
                </div>
            </div>
        </div>
    </body>


</html>
