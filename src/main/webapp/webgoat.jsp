<%@ page contentType="text/html; charset=ISO-8859-1" language="java"
	errorPage=""%>
<%@page import="org.owasp.webgoat.session.WebSession"%>
<%
WebSession webSession = ((WebSession) session.getAttribute("websession"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>WebGoat V5.2</title>
<link rel="stylesheet" href="css/webgoat.css" type="text/css" />
</head>

<body>

<div id="wrap">
<div id="top"></div>
<div id="start">
<p>Thank you for using WebGoat! This program is a demonstration of common web application flaws.
The exercises are intended to provide hands on experience with
application penetration testing techniques. </p>
<p>The WebGoat project is lead
by Bruce Mayhew. Please send all comments to Bruce at <%=webSession.getWebgoatContext().getFeedbackAddress()%>.</p>

<div id="team">
<table border="0" align="center" class="lessonText">
	<tr>
		<td width="50%">
		<div align="center"><a href="http://www.owasp.org"><img
			border="0" src="images/logos/owasp.jpg" alt="OWASP Foundation"
			longdesc="http://www.owasp.org" /></a></div>
		</td>
		<td width="50%">
		<div align="center"><a href="http://www.aspectsecurity.com"><img
			border="0" src="images/logos/aspect.jpg" alt="Aspect Security"
			longdesc="http://www.aspectsecurity.com" /></a></div>
		</td>
	</tr>
	<tr>
		<td width="50%">
		<div align="center"><span class="style1"><br />
		WebGoat Design Team </span></div>
		</td>
		<td width="50%">
		<div align="center"><span class="style1"><br />
		V5.3 Lesson Contributers </span></div>
		</td>
	</tr>
	<tr>
		<td valign="top">
		<div align="center" class="style2">Bruce Mayhew</div>
		<div align="center" class="style2">David Anderson</div>
		<div align="center" class="style2">Rogan Dawes</div>
		<div align="center" class="style2">Laurence Casey (Graphics)</div>
		</td>
		<td valign="top">
		<div align="center" class="style2">Chuck Willis</div>
		<div align="center" class="style2">Cam Morris</div>
		<div align="center" class="style2"></div>

		</td>
	</tr>
	<tr>
		<td height="25" valign="bottom">
		<div align="center"><span class="style1">Special Thanks
		for V5.3</span></div>
		</td>
		<td height="25" valign="bottom">
		<div align="center"><span class="style1">Documentation
		Contributers</span></div>
		</td>
	</tr>
	<tr>
		<td>
		<div align="center" class="style2">Christine (Maven)</div>
		<div align="center" class="style2">Marek Jawurek (Internationalization)</div>
		<br/><div align="center" class="style2">To all who have sent comments</div>
		
		</td>
		<td>
		<div align="center" class="style2">Sherif Koussa<br />
		</div>
		<div align="center" class="style2">Aung Khant<br />
		(http://yehg.org/)</div>
		<div align="center" class="style2">Erwin Geirnaert<br />
		(http://www.zionsecurity.com/)</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center" class="style2">
		<form id="form" name="form" method="post" action="attack"><input
			type="submit" name="start" value="Start WebGoat" /></form>
		</div>
		</td>
	</tr>
	<tr>
		<td>
		<div align="center" class="style2">&nbsp;</div>
		</td>
	</tr>
</table>
</div>
</div>
<div align="center" class="style2">&nbsp;</div>
<div align="center" class="style2">&nbsp;</div>
<div align="center" class="style2">&nbsp;</div>
<div id="warning">WARNING<br />
While running this program, your machine is extremely vulnerable to
attack if you are not running on localhost. If tou are NOT running on localhost (default configuration), You should disconnect from the network while using this program.
<br />
<br />
This program is for educational purposes only. Use of these techniques
without permission could lead to job termination, financial liability,
and/or criminal penalties.</div>
</div>
</body>
</html>
