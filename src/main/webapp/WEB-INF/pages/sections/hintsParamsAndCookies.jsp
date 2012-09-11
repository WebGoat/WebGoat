<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.Iterator, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.AbstractLesson, org.owasp.webgoat.util.*" 
	errorPage=""
	isELIgnored="false" %>

<%
Course course = ((Course)session.getAttribute("course"));
WebSession webSession = ((WebSession)session.getAttribute("websession"));
AbstractLesson currentLesson = webSession.getCurrentLesson();

if (webSession.getHint() != null)
{
%>
	<div id="hint" class="info"> <%= webSession.getHint() %> </div><br>
<%	
}

if (webSession.getParams() != null)
{
	Iterator i = webSession.getParams().iterator();
	while (i.hasNext())
	{
		Parameter p = (Parameter) i.next();
%>		
		<div id="parameter" class="info"> <%= p.getName()%> = <%= p.getValue() %></div><br>
<%		
	}
}


if (webSession.getCookies() != null)
{
	Iterator i = webSession.getCookies().iterator();
	while (i.hasNext())
	{
		Cookie c = (Cookie) i.next();
%>		
		<div id="cookie" class="info"> <%= c.getName() %> <img src="images/icons/rightArrow.jpg" alt="\"><%= c.getValue() %></div><br>
<%
	}
}
%>