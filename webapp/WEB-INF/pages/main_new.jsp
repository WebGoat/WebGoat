<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
         errorPage=""%>
<%@page import="org.owasp.webgoat.session.WebSession"%>
<%
    WebSession webSession = ((WebSession) session.getAttribute("websession"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
	    <!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
		<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
		<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
		<!--[if gt IE 8]><!-->
		
		<!--  CSS -->
		<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
		<!-- Bootstrap core CSS -->
		<link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css">
		<!-- Fonts from Font Awsome -->
		<link rel="stylesheet" href="css/font-awesome.min.css">
		<!-- CSS Animate -->
		<link rel="stylesheet" href="css/animate.css">
		<!-- Custom styles for this theme -->
		<link rel="stylesheet" href="css/main.css">
		
		<!-- JS -->
		<script src="js/angular/angular.min.js"></script>
		<script src="js/angular/angular-animate.min.js"></script>
		<!-- Feature detection -->
		<script src="js/modernizr-2.6.2.min.js"></script>
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="js/html5shiv.js"></script>
		<script src="js/respond.min.js"></script>
		<![endif]-->
		<script type="text/javascript">
			var goat=angular.module("goatApp", ['ngAnimate']);
		</script>
		<script type="text/javascript" src="js/goat.js"></script>		
		<script type="text/javascript" src="js/goatConstants.js"></script>		
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>WebGoat V6.0</title>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"/> 

    </head>

    <body class="animated fadeIn" ng-app="goatApp">
       <section id="container">
        <header id="header">
            <!--logo start-->
            <div class="brand">
                <a href="index.html" class="logo"><span>Web</span>Goat</a>
            </div>
            <!--logo end-->
            <div class="toggle-navigation toggle-left">
                <button type="button" class="btn btn-default" id="toggle-left" data-toggle="tooltip" data-placement="right" title="Toggle Navigation">
                    <i class="fa fa-bars"></i>
                </button>
            </div><!--toggle navigation end-->
        </header>
		
		        <!--sidebar left start-->
        <aside class="sidebar">
            <div id="leftside-navigation" class="nano" ng-controller="goatMenu">
                <ul class="nano-content">                
                    <li class="sub-menu" ng-repeat="item in menuTopics">
                    <!-- TODO: implement conditional rendering -->
                            <a ng-click="expanded = !expanded" href=""><i class="fa {{item.class}}"></i><span>{{item.name}}</span></a>
                            <ul class="slideDown" ng-show="expanded" style="display:block;">
                                    <li ng-repeat="child in item.children">
                                            <a ng-click="renderLesson(child.link)" title="link to {{child.name}}">{{child.name}}</a>
                                    </li>
                            </ul>
                    </li>
		</ul>
						
            </div>

        </aside>
        <!--sidebar left end-->
        <!--main content start-->
        <section class="main-content-wrapper">
            <section id="main-content">
                <div class="row">
               		<div class="col-md-12">
               			<div class="panel">
                            <div class="panel-body" id="lesson_content">                            
                            
               					<h1>About WebGoat</h1>
               					<hr />
               					<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque volutpat feugiat nunc, non vulputate urna dictum ut. Nam consectetur porttitor diam ut ultricies. Aenean dolor dolor, congue sed ornare non, elementum in mauris. Phasellus orci sem, rhoncus eu laoreet eu, aliquam nec ante. Suspendisse sit amet justo eget eros tempor tincidunt vel quis justo. Sed pulvinar enim id neque pellentesque, eu rhoncus lorem eleifend. Morbi congue tortor sit amet pulvinar posuere.</p>
              					<p>Integer rhoncus gravida arcu, at bibendum magna feugiat sit amet. Vivamus id lacinia massa. Praesent eu quam ullamcorper, tempor elit nec, lobortis massa. In in eros eu augue rhoncus semper. Vestibulum ornare purus vitae bibendum vulputate. Cras eleifend commodo lectus, eget pharetra justo mollis quis. Donec tempor magna lectus, vitae suscipit turpis venenatis et. Nulla facilisi.</p>
              					<p>Nam placerat magna in massa euismod fringilla. Pellentesque in cursus risus, eu hendrerit ligula. Quisque ultrices eget tortor ut eleifend. Praesent auctor libero nec quam fringilla faucibus. Curabitur cursus risus eu faucibus rutrum. Morbi dapibus nulla risus, et euismod eros posuere volutpat. Quisque ut diam diam. Quisque sed enim tortor. Suspendisse commodo magna nec felis ultricies laoreet. Donec sit amet vehicula eros. Phasellus at dapibus enim. Sed massa quam, aliquet eu mattis at, porttitor a nisi.</p>
              					<hr />
              					<p>Nam placerat magna in massa euismod fringilla. Pellentesque in cursus risus, eu hendrerit ligula. Quisque ultrices eget tortor ut eleifend. Praesent auctor libero nec quam fringilla faucibus. Curabitur cursus risus eu faucibus rutrum. Morbi dapibus nulla risus, et euismod eros posuere volutpat. Quisque ut diam diam. Quisque sed enim tortor. Suspendisse commodo magna nec felis ultricies laoreet. Donec sit amet vehicula eros. Phasellus at dapibus enim. Sed massa quam, aliquet eu mattis at, porttitor a nisi.</p>
               				</div>
               			</div>
               		</div>
				</div>
            </section>
        </section>
        <!--main content end-->
        
    </section>
    

    <!--Global JS-->
    <script src="js/jquery-1.10.2.min.js"></script>
    <script src="plugins/bootstrap/js/bootstrap.min.js"></script>
<!--     <script src="plugins/waypoints/waypoints.min.js"></script> -->
<!--     <script src="js/application.js"></script> -->

        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
        <script src="http://malsup.github.com/jquery.form.js"></script>  
        <script>
        //Load global functions
			
            // set this to true if you want to see form submissions
            // set to false once we get all the kinks worked out
            var DEBUG_FORM_SUBMISSION = false;

            $(document).ready(function() {
                // bind to click events on menu links
                $('.menu-link').bind('click', function(event) {
                    event.preventDefault();
                    $.get(this.href, {}, function(reply) {
                        $("#lesson_content").html(reply);
                    }, "html");
                });

            });
            // make all forms ajax forms
            var options = {
                target: '#lesson_content', // target element(s) to be updated with server response                     
                beforeSubmit: showRequest, // pre-submit callback, comment out after debugging 
                success: showResponse  // post-submit callback, comment out after debugging 

                        // other available options: 
                        //url:       url         // override for form's 'action' attribute 
                        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
                        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
                        //clearForm: true        // clear all form fields after successful submit 
                        //resetForm: true        // reset the form after successful submit 

                        // $.ajax options can be used here too, for example: 
                        //timeout:   3000 
            };
            // pre-submit callback 
            function showRequest(formData, jqForm, options) {
                if (DEBUG_FORM_SUBMISSION) {
                    // formData is an array; here we use $.param to convert it to a string to display it 
                    // but the form plugin does this for you automatically when it submits the data 
                    var queryString = $.param(formData);

                    // jqForm is a jQuery object encapsulating the form element.  To access the 
                    // DOM element for the form do this: 
                    // var formElement = jqForm[0]; 

                    alert('About to submit: \n\n' + queryString);
                }

                // here we could return false to prevent the form from being submitted; 
                // returning anything other than false will allow the form submit to continue 
                return true;
            }

            // post-submit callback 
            function showResponse(responseText, statusText, xhr, $form) {
                // for normal html responses, the first argument to the success callback 
                // is the XMLHttpRequest object's responseText property 

                // if the ajaxForm method was passed an Options Object with the dataType 
                // property set to 'xml' then the first argument to the success callback 
                // is the XMLHttpRequest object's responseXML property 

                // if the ajaxForm method was passed an Options Object with the dataType 
                // property set to 'json' then the first argument to the success callback 
                // is the json data object returned by the server 
                if (DEBUG_FORM_SUBMISSION) {
                    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
                            '\n\nThe output div should have already been updated with the responseText.');
                }
            }
            function makeFormsAjax() {
                $("form").ajaxForm(options);
            }
        </script>
    </body>
</html>
