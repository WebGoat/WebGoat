<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.WebSession" 
	errorPage="" %>

<%
WebSession webSession = ((WebSession)session.getAttribute("websession"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Untitled Document</title>
<link href="css/webgoat.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id=#wrap>
	<%
			String source = webSession.getSource();
			if (source != null)
			{
				String printSource = "<div id=\"source\">" + source + "</div><br>";
				out.println(printSource);
			}
	%>
	</div>
</body>
</html>
