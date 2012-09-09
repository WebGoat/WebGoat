<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
	errorPage=""%>
<%@page import="org.owasp.webgoat.session.WebSession"%>
<%
	WebSession webSession = ((WebSession) session
			.getAttribute("websession"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>WebGoat V5.4</title>
<link rel="stylesheet" href="css/webgoat.css" type="text/css" />
</head>

<body>

<div id="wrap">
<div id="top"></div>
<div id="start">
<p>Thank you for taking the time to improve WebGoat!</p>
<p>The lesson you were on was: <%=webSession.getCurrentLesson().getName()%></p>
<p>There are several ways to report a bug, fix a bug, or get help.</p>

<b>To report a bug:</b>
<ol>
	<li>File a WebGoat defect using <a
		href="http://code.google.com/p/webgoat/issues/list">Google Code
	WebGoat Issues</a>. Please be as specific as possible. If you have a
	recommended solution for a bug, include the solution in the bug report.</li>
</ol>
<b>To get help:</b>
<ol>
	<li>Look in the <a
		href="http://code.google.com/p/webgoat/wiki/FAQ">FAQ</a>, 
	the most common problems are in the FAQ. The FAQ also allows user comments,
	but it is not monitored like the WebGoat mailing list.</li>
	<li>Send an email to the <a
		href="mailto: owasp-webgoat@lists.owasp.org?subject=WebGoat Help Request - Lesson: 
												 <%=webSession.getCurrentLesson().getName()%>">WebGoat
	mail list</a>. The WebGoat mail list is the preferred method to ask for
	help. It is likely that someone has already experienced the issue you
	are seeing. In order to post to the list you must be <a
		href="https://lists.owasp.org/mailman/listinfo/owasp-webgoat">subscribed</a>
	to the WebGoat Mail List.</li>
	<li>Send an email to <a
		href="mailto: <%=webSession.getWebgoatContext().getFeedbackAddress()%>?subject=WebGoat Direct Help Request - Lesson: 
												 <%=webSession.getCurrentLesson().getName()%>">Bruce
	Mayhew</a></li>
</ol>
<b>To fix a bug, typo, or enhance WebGoat:</b>
<ol>
	<li>Send an email to <a
		href="mailto: <%=webSession.getWebgoatContext().getFeedbackAddress()%>?subject=WebGoat Contributor Request">Bruce
	Mayhew</a>. This will start the discussion of getting you added to the <a
		href="http://code.google.com/p/webgoat/people/list">WebGoat
	Contributers List</a>. Once you become a WebGoat contributor, you can fix
	as many bugs/lessons as you desire.</li>
</ol>

		<div id="bottom">
			<div align="center"><a href="http://www.owasp.org">OWASP Foundation</a> | 
								<a href="http://www.owasp.org/index.php/OWASP_WebGoat_Project">Project WebGoat</a> 
			</div>
	  	</div>

</div>
</div>
</body>
</html>
