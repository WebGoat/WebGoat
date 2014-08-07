<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
         errorPage=""%>
<%@page import="org.owasp.webgoat.session.WebSession"%>
<%
    //WebSession webSession = ((WebSession) session.getAttribute("websession"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>WebGoat V6.0</title>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"/>
        <style>
            /*
            * Base structure
            */

            /* Move down content because we have a fixed navbar that is 50px tall */
            body {
                padding-top: 50px;
            }


            /*
             * Global add-ons
             */

            .sub-header {
                padding-bottom: 10px;
                border-bottom: 1px solid #eee;
            }

            /*
             * Top navigation
             * Hide default border to remove 1px line.
             */
            .navbar-fixed-top {
                border: 0;
            }

            /*
             * Sidebar
             */

            /* Hide for mobile, show later */
            .sidebar {
                display: none;
            }
            @media (min-width: 768px) {
                .sidebar {
                    position: fixed;
                    top: 51px;
                    bottom: 0;
                    left: 0;
                    z-index: 1000;
                    display: block;
                    padding: 20px;
                    overflow-x: hidden;
                    overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
                    background-color: #f5f5f5;
                    border-right: 1px solid #eee;
                }
            }

            /* Sidebar navigation */
            .nav-sidebar {
                margin-right: -21px; /* 20px padding + 1px border */
                margin-bottom: 20px;
                margin-left: -20px;
            }
            .nav-sidebar > li > a {
                padding-right: 20px;
                padding-left: 20px;
            }
            .nav-sidebar > .active > a,
            .nav-sidebar > .active > a:hover,
            .nav-sidebar > .active > a:focus {
                color: #fff;
                background-color: #428bca;
            }


            /*
             * Main content
             */

            .main {
                padding: 20px;
            }
            @media (min-width: 768px) {
                .main {
                    padding-right: 40px;
                    padding-left: 40px;
                }
            }
            .main .page-header {
                margin-top: 0;
            }


            /*
             * Placeholder dashboard ideas
             */

            .placeholders {
                margin-bottom: 30px;
                text-align: center;
            }
            .placeholders h4 {
                margin-bottom: 0;
            }
            .placeholder {
                margin-bottom: 20px;
            }
            .placeholder img {
                display: inline-block;
                border-radius: 50%;
            }
        </style>

    </head>

    <body>

        <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Webgoat 6.0</a>
                </div>
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">

                        <li><a href="#">Settings</a></li>
                        <li><a href="#">Profile</a></li>
                        <li><a href="#">Help</a></li>
                        <li><a href="j_spring_security_logout">Logout</a></li>
                    </ul>
                    <!--
                    <form class="navbar-form navbar-right">
                        <input type="text" class="form-control" placeholder="Search...">
                    </form>
                    -->
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-3 col-md-2 sidebar">
                    <ul class="nav nav-sidebar">
                        <li>This should be built from service</li>
                        <li class="active"><a href="#">General</a></li>
                        <li><a href="attack?Screen=4&amp;menu=100" target="lesson" class="menu-link">HTTP Basics</a></li>
                        <li><a href="attack?Screen=58&menu=100" class="menu-link">HTTP Splitting</a></li>
                    </ul>
                    <ul class="nav nav-sidebar">
                        <li><a href="">Nav item</a></li>
                        <li><a href="">Nav item again</a></li>
                        <li><a href="">One more nav</a></li>
                        <li><a href="">Another nav item</a></li>
                        <li><a href="">More navigation</a></li>
                    </ul>
                    <ul class="nav nav-sidebar">
                        <li><a href="">Nav item again</a></li>
                        <li><a href="">One more nav</a></li>
                        <li><a href="">Another nav item</a></li>
                    </ul>
                </div>
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <h1 class="page-header">Lesson Content</h1>

                    <div class="" id="lesson_content">
                        Lesson content goes here
                    </div>


                </div>
            </div>
        </div>

        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="http://malsup.github.com/jquery.form.js"></script> 
        <script>
            $(document).ready(function() {
                // bind to click events on menu links
                $('.menu-link').bind('click', function(event) {
                    event.preventDefault();
                    $.get(this.href, {}, function(reply) {
                        $("#lesson_content").html(reply);
                    }, "html");
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
                    // formData is an array; here we use $.param to convert it to a string to display it 
                    // but the form plugin does this for you automatically when it submits the data 
                    var queryString = $.param(formData);

                    // jqForm is a jQuery object encapsulating the form element.  To access the 
                    // DOM element for the form do this: 
                    // var formElement = jqForm[0]; 

                    alert('About to submit: \n\n' + queryString);

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

                    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
                            '\n\nThe output div should have already been updated with the responseText.');
                }
                // bind form using 'ajaxForm' 
                $("form[name='form']").ajaxForm(options);
            });
        </script>
    </body>
</html>
