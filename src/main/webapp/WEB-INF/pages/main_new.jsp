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
        <script src="js/jquery/jquery-1.10.2.min.js"></script>
        <script src="js/angular/angular.min.js"></script>
        <!-- angular modules -->
        <script src="js/angular/angular-animate.min.js"></script>
        <script src="js/angular/ui-bootstrap-tpls-0.11.0.min.js"></script>
        <!-- Feature detection -->
        <script src="js/modernizr-2.6.2.min.js"></script>
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="js/html5shiv.js"></script>
        <script src="js/respond.min.js"></script>
        <![endif]-->

        <!--Global JS-->

        <script src="js/jquery_form/jquery.form.js"></script>  
        <script src="plugins/bootstrap/js/bootstrap.min.js"></script>

        <script src="js/application.js"></script>
        <script type="text/javascript">
            var goat = angular.module("goatApp", ['ngAnimate', 'ui.bootstrap']);
        </script>
        <script type="text/javascript" src="js/goatConstants.js"></script>
        <script type="text/javascript" src="js/goatUtil.js"></script>
        <script type="text/javascript" src="js/goatData.js"></script>
        <script type="text/javascript" src="js/goatLesson.js"></script>
        <script type="text/javascript" src="js/goatControllers.js"></script>
        <!-- end of JS -->



        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>WebGoat</title>
    </head>

    <body class="animated fadeIn" ng-app="goatApp">
        <section id="container" ng-controller="goatLesson">
            <header id="header">
                <!--logo start-->
                <div class="brand">
                    <a href="${pageContext.request.contextPath}/start.mvc" class="logo"><span>Web</span>Goat</a>
                </div>
                <!--logo end-->
                <div class="toggle-navigation toggle-left">
                    <button type="button" class="btn btn-default" id="toggle-left" data-toggle="tooltip" data-placement="right" title="Toggle Navigation">
                        <i class="fa fa-bars"></i>
                    </button>
                </div><!--toggle navigation end-->
                <div class="lessonTitle" >
                    <h1 id="lessonTitle"></h1>
                </div><!--lesson title end-->
                <div class="user-nav pull-right" style="margin-right: 75px;">
                    <div class="dropdown" style="display:inline">
                        <button type="button" class="btn btn-default dropdown-toggle" id="dropdownMenu1" ng-disabled="disabled">
                            <i class="fa fa-user"></i> <span class="caret"></span>
                        </button>                   
                        <ul class="dropdown-menu dropdown-menu-left" role="menu" aria-labelledby="dropdownMenu1">
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="<c:url value="j_spring_security_logout" />">Logout</a></li>
                            <li role="presentation" class="divider"></li>     
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">User: ${user}</a></li>
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">Role: ${role}</a></li>
                            <li role="presentation" class="divider"></li>   
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">${version}</a></li>
                            <li role="presentation" class="disabled"><a role="menuitem" tabindex="-1" href="#">Build: ${build}</a></li>                            

                        </ul>
                    </div>
                    <button type="button" class="btn btn-default right_nav_button" ng-click="showAbout()" data-toggle="tooltip" title="About WebGoat">
                        <i class="fa fa-info"></i>
                    </button>
                    <a href="mailto:${contactEmail}?Subject=Webgoat%20feedback" target="_top">
                        <button type="button" class="btn btn-default right_nav_button"data-toggle="tooltip" title="Contact Us">
                            <i class="fa fa-envelope"></i>
                        </button>
                    </a>


                </div>
            </header>

            <!--sidebar left start-->
            <aside class="sidebar" >
                <div id="leftside-navigation" ng-controller="goatMenu" class="nano">
                    <ul class="nano-content">
                        <li class="sub-menu" ng-repeat="item in menuTopics">
                            <a ng-click="accordionMenu(item.id)" href=""><i class="fa {{item.class}}"></i><span>{{item.name}}</span></a><!-- expanded = !expanded-->
                            <ul class="slideDown lessonsAndStages {{item.displayClass}}" id="{{item.id}}" isOpen=0>
                                <li ng-repeat="lesson in item.children" class="{{lesson.selectedClass}}">
                                    <a ng-click="renderLesson(lesson.id, lesson.link, {showSource: lesson.showSource, showHints: lesson.showHints})" id="{{lesson.id}}" class="{{lesson.selectedClass}}" title="link to {{lesson.name}}" href="">{{lesson.name}}</a><span class="{{lesson.completeClass}}"></span>
                                    <span ng-repeat="stage in lesson.children">
                                        <a ng-click="renderLesson(stage.id, stage.link, {showSource: stage.showSource, showHints: stage.showHints})" class="selectedClass" id="{{stage.id}}"  title="link to {{stage.name}}" href="">{{stage.name}}</a><span class="{{stage.completeClass}}"></span>
                                    </span>
                                </li>
                            </ul>
                        </li>
                    </ul> 
                </div>

            </aside>
            <!--sidebar left end-->
            <!--main content start-->
            <section class="main-content-wrapper">
                <section id="main-content" > <!--ng-controller="goatLesson"-->
                    <div class="row">
                        <div class="col-md-8">
                            <div class="col-md-12" align="left">
                                <div class="panel">
                                    <div class="panel-body">
                                        <button type="button" id="showSourceBtn" ng-show="showSource" class="btn btn-primary btn-xs" ng-click="showLessonSource()">Java [Source]</button>
                                        <button type="button" id="showSolutionBtn" class="btn btn-primary btn-xs" ng-click="showLessonSolution()">Solution</button>
                                        <button type="button" id="showPlanBtn" class="btn btn-primary btn-xs" ng-click="showLessonPlan()">Lesson Plan</button>
                                        <button type="button" id="showHintsBtn" ng-show="showHints" class="btn btn-primary btn-xs"  ng-click="viewHints()">Hints</button>
                                        <button type="button" id="restartLessonBtn"  class="btn btn-xs"  ng-click="restartLesson()">Restart Lesson</button>
                                    </div>
                                </div>
                                <div class="lessonHelp" id="lesson_hint_row">
                                    <h4>Hints</h4>
                                    <div class="panel" >
                                        <div class="panel-body" id="lesson_hint">
                                            <span class="glyphicon-class glyphicon glyphicon-circle-arrow-left" id="showPrevHintBtn" ng-click="viewPrevHint()"></span>
                                            <span class="glyphicon-class glyphicon glyphicon-circle-arrow-right" id="showNextHintBtn" ng-click="viewNextHint()"></span>
                                            <br/>
                                            <span ng-show="showHints" bind-html-unsafe="curHint"></span>
                                            <!--<span id="curHintContainer"></span>-->
                                        </div>                                    
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="panel" >
                                    <div class="panel-body" id="lesson_content">    
                                        <b>This should default to the "How to Work with Webgoat" lesson</b>
                                    </div>

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
                                        <div id="cookiesAndParamsView">
                                            <div class="cookiesView">
                                                <h4>Cookies</h4>
                                                <div class="cookieContainer" ng-repeat="cookie in cookies">
                                                    <table class="cookieTable table-striped table-nonfluid" >
                                                        <thead>
                                                            <tr><th class="col-sm-1"></th><th class="col-sm-1"></th></tr> <!-- Field / Value -->
                                                        </thead>
                                                        <tbody>
                                                            <tr ng-repeat="(key, value) in cookie">
                                                                <td>{{key}}</td>
                                                                <td>{{value}}</td>
                                                            </tr>
                                                        </tbody>
                                                        <!--<li ng-repeat="(key, value) in cookie">{{key}} :: {{ value}} </td>-->
                                                        <!--</ul>-->
                                                    </table>
                                                </div>
                                            </div>
                                            <div id="paramsView"> <!--class="paramsView"-->
                                                <h4>Params</h4>
                                                <table class="paramsTable table-striped table-nonfluid" id="paramsTable">
                                                    <thead>
                                                        <tr><th>Param</th><th>Value</th></tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr ng-repeat="param in parameters">
                                                            <td>{{param.name}}</td>
                                                            <td>{{param.value}}</td>
                                                        </tr>						
                                                    </tbody>
                                                </table>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div><!--col-md-4 end-->         
                    </div>
                    <div id="lessonHelpsWrapper">
                        <!--div class="row lessonHelp" id="lesson_hint_row">
                            <div class="col-md-12">
                                <h4>Hints</h4>
                                <div class="panel" >
                                    <div class="panel-body" id="lesson_hint">
                                        <span class="glyphicon-class glyphicon glyphicon-circle-arrow-left" id="showPrevHintBtn" ng-click="viewPrevHint()"></span>
                                        <span class="glyphicon-class glyphicon glyphicon-circle-arrow-right" id="showNextHintBtn" ng-click="viewNextHint()"></span>
                                        <br/>
                                        {{curHint}}
                                    </div>                                    
                                </div>
                            </div>
                        </div-->
                        <div class="row lessonHelp" id="lesson_cookies_row">
                            <div class="col-md-12">
                                <h4>Lesson Parameters and Cookies</h4>
                                <div class="panel" >
                                    <div class="panel-body" id="lesson_cookies">	

                                    </div>                                    
                                </div>
                            </div>
                        </div>   
                        <div class="row lessonHelp" id="lesson_hint_row">
                            <div class="col-md-12">
                                <h4>Lesson Hints</h4>
                                <div class="panel" >
                                    <div class="panel-body" id="lesson_hint">	

                                    </div>                                    
                                </div>
                            </div>
                        </div>                 
                        <div class="row lessonHelp" id="lesson_plan_row">
                            <div class="col-md-12">
                                <h4>Lesson Plan</h4>
                                <div class="panel" >
                                    <div class="panel-body" id="lesson_plan">
                                        <!-- allowing jQuery to handle this one -->
                                    </div>                                    
                                </div>
                            </div>
                        </div> 
                        <div class="row lessonHelp" id="lesson_solution_row">
                            <div class="col-md-12">
                                <h4>Lesson Solution</h4>
                                <div class="panel">
                                    <div class="panel-body" id="lesson_solution">
                                    </div>                                    
                                </div>
                            </div>
                        </div> 
                        <div class="row lessonHelp" id="lesson_source_row">
                            <div class="col-md-12">
                                <h4>Lesson Source Code</h4>
                                <div class="panel">
                                    <div class="panel-body" id="lesson_source">
                                        <pre>{{source}}</pre>
                                    </div>                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </section>

            <!--main content end-->

        </section>

        <!--main content end-->

        </section>

        <script>
            $(document).ready(function() {
                //TODO merge appliction.js code into other js files
                app.init();
            });
        </script>

        <!-- About WebGoat Modal -->
        <div class="modal fade" id="aboutModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <jsp:include page="../pages/about.jsp"/> 
                </div>
            </div>
        </div>
    </body>
</html>
