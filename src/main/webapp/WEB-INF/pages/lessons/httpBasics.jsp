<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.AbstractLesson" 
	errorPage=""
	isELIgnored="false" %>
	 
 <div id="lessonPlans" style="visibility:hidden; height:1px; position:absolute; left:260px; top:130px; width:425px; z-index:105;">
 	<div align="Center"> 
		<p><b>Lesson Plan Title:</b> Http Basics </p>
		</div>
		<p><b>Concept / Topic To Teach:</b> </p>
	This lesson presents the basics for understanding the transfer of data between the browser and the web application.<br>
	
	<div align="Left"> 
		<p>
			<b>How HTTP works:</b>
		</p>
		All HTTP transactions follow the same general format. Each client request and server response has three parts:  the request or response line, a header section, and the entity body. The client initiates a transaction as follows: <br>
		<br>
		The client contacts the server and sends a document request <br>
	</div>
	<br>

	<ul>GET /index.html?param=value HTTP/1.0</ul>
	Next, the client sends optional header information to inform the server of its configuration and the document formats it will accept.<br>
	<br>
	<ul>User-Agent: Mozilla/4.06 Accept: image/gif,image/jpeg, */*</ul>
	After sending the request and headers, the client may send additional data. This data is mostly used by CGI programs using the POST method.<br>
	<p><b>General Goal(s):</b> </p>
	<%-- Start Instructions --%>	
	Enter your name in the input field below and press "go" to submit. The server will accept the request, reverse the input, and display it back to the user, illustrating the basics of handling an HTTP request.
	<br/><br/>
	The user should become familiar with the features of WebGoat by manipulating the above 
	buttons to view hints, show the HTTP request parameters, the HTTP request cookies, and the Java source code. You may also try using WebScarab for the first time.
	<%-- Stop Instructions --%>

	<br/>
	<br/>
	<a href="javascript:toggle('lessonPlans')" target="_top" onclick="MM_nbGroup('down','group1','plans','',1)">Close this Window</a>
</div>
 
 
<%
	Course course = ((Course)session.getAttribute("course"));
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
%>	 
 
 <%--
	This form posts to httpBasics.do.  However, we must append the "menu" request parameter in order
	for the current submenu to display properly, hence the getLink() call to build the form's 
	action attribute below. 
  --%>
 <form:form method="POST" action="<%= webSession.getCurrentLesson().getLink() %>">
 	<p>
 		Enter your name in the input field below and press "go" to submit. 
 		The server will accept the request, reverse the input, and display it back to the user, 
 		illustrating the basics of handling an HTTP request.
 	</p> 
		
	<p>
		The user should become familiar with the features of WebGoat by manipulating 
		the above buttons to view hints, show the HTTP request parameters, 
		the HTTP request cookies, and the Java source code. 
		You may also try using WebScarab for the first time.
	</p>

	<p>
		Enter your name: 
		<form:input path="personName" />
		<input name="SUBMIT" type="SUBMIT" value="Go!"/>
	</p>
</form:form>
