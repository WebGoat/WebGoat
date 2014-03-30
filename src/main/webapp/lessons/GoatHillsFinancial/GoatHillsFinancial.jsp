<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*" 
	errorPage="" %>
<%@page import="org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;"%>
<style>
<jsp:include page="GoatHillsFinancial.css" />
</style>
<%
WebSession webSession = ((WebSession)session.getAttribute("websession"));
System.out.println("WebSession is " + webSession);
GoatHillsFinancial currentLesson = (GoatHillsFinancial) webSession.getCurrentLesson();
System.out.println("CurrentLesson = " + currentLesson);
%>
<div id="lesson_wrapper">
	<div id="lesson_header"></div>
	<div class="lesson_workspace">
	<%
	String subViewPage = currentLesson.getPage(webSession);
	System.out.println("SubViewPage is " + subViewPage);
	if (subViewPage != null)
	{
		//System.out.println("Including sub view page: " + subViewPage);
	%>
	<jsp:include page="<%=subViewPage%>" />
	<%
	}
	%>

	</div>
</div>