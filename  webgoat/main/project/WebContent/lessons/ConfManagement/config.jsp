<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Configuration Page</title>
</head>
<body>
<% response.sendRedirect("/WebGoat/attack?" +
		        "Screen=" + request.getParameter("Screen") +
		        "&menu=" + request.getParameter("menu") +
		        "&succeeded=yes"); 
%>

</body>
</html>