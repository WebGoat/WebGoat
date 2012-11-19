<%@ page language="java" contentType="text/html; charset=ISO-8859-1" import="java.util.regex.*" import="org.owasp.webgoat.lessons.DangerousEval"
    pageEncoding="ISO-8859-1"%>
<%
String action = request.getParameter("action");
String field1 = request.getParameter("field1");
String field2 = request.getParameter("field2");
String regex1 = "^[0-9]{3}$";// any three digits
Pattern pattern1 = Pattern.compile(regex1);

if(action == null) action = "Purchase";
if(field1 == null) field1 = "123";
if(field2 == null) field2 = "-1";

/** For security reasons, we remove all '<' and '>' characters to prevent XSS **/
// Thank you Victor Bucutea for noticing replaceAll only cleans taint to the return value.
field1 = field1.replaceAll("<", "");
field1 = field1.replaceAll(">", "");
field2 = field2.replaceAll("<", "");
field2 = field2.replaceAll(">", "");

if("Purchase".equals(action))
{
	if(!pattern1.matcher(field1).matches())
	{
		/** If they supplied the right attack, pass them **/
		if(field1.indexOf("');") != -1 && field1.indexOf("alert") != -1 && field1.indexOf("document.cookie") != -1)
		{
			session.setAttribute(DangerousEval.PASSED, "true");
		}
		
		out.write("alert('Whoops: You entered an incorrect access code of \"" + field1 + "\"');");
	}
	else
	{
		out.write("alert('Purchase completed successfully with credit card \"" + field2 + "\" and access code \"" + field1 + "\"');");
	}
}
%>
