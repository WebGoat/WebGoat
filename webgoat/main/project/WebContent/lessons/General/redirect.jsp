<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HTTP Splitting</title>
</head>
<body>
<% response.sendRedirect(request.getContextPath() + "/attack?" +
 		        "Screen=" + request.getParameter("Screen") +
 		        "&menu=" + request.getParameter("menu") +
 		        "&fromRedirect=yes&language=" + request.getParameter("language")); 
%>
</body>
</html>