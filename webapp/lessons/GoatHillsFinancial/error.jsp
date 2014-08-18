<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial"
	errorPage="" %>
<%
	WebSession webSession = ((WebSession)session.getAttribute(WebSession.SESSION));
//	int myUserId = getIntSessionAttribute(webSession, "GoatHillsFinancial." + GoatHillsFinancial.USER_ID);
%>
<br><br><br>An error has occurred.
<br><br><br>
<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">

 <input type="submit" name="action" value="<%=GoatHillsFinancial.LOGIN_ACTION%>"/>
</form>