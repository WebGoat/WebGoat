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
<link rel="stylesheet" href="css/webgoat_challenge.css" type="text/css" />
</head>

<body>

<div id="wrap_ch">
<div id="top_ch"></div>
<div id="start_ch">
<p>Thank you for using WebGoat! This program is a demonstration of common web application flaws.
The exercises are intended to provide hands on experience with
application penetration testing techniques. </p>
<p>The WebGoat project is lead
by Bruce Mayhew. Please send all comments to Bruce at <%=webSession.getWebgoatContext().getFeedbackAddress()%>.</p>
<p>Thanks to <a href="http://www.ouncelabs.com"><img align="top" height="20" width="160" border = "0" src="images/logos/ounce.jpg" alt="Ounce Labs"/></a> for supporting Bruce on the WebGoat Project.</p>

<div id="team_ch">
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
		Lesson Contributers </span></div>
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
		<div align="center" class="style2">Aspect Security</div>
		<div align="center" class="style2">Sherif Koussa</div>
		<div align="center" class="style2">Romain Brechet</div>
		<div align="center" class="style2"></div>

		</td>
	</tr>
	<tr>
		<td height="25" valign="bottom">
		<div align="center"><span class="style1">Special Thanks
		for V5.2</span></div>
		</td>
		<td height="25" valign="bottom">
		<div align="center"><span class="style1">Documentation
		Contributers</span></div>
		</td>
	</tr>
	<tr>
		<td>
		<div align="center" class="style2">Reto Lippuner</div>
		<div align="center" class="style2">Marcel Wirth	</div>
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
<div id="warning_ch">WARNING<br />
While running this program, your machine is extremely vulnerable to
attack. You should disconnect from the network while using this program.
<br />
<br />
This program is for educational purposes only. Use of these techniques
without permission could lead to job termination, financial liability,
and/or criminal penalties.</div>
</div>
</body>
</html>
