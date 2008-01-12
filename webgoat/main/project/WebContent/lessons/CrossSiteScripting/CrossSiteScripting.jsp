<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.CrossSiteScripting.CrossSiteScripting" 
	errorPage="" %>
<style>
<jsp:include page="CrossSiteScripting.css" />
</style>
<%
WebSession webSession = ((WebSession)session.getAttribute("websession"));
CrossSiteScripting currentLesson = (CrossSiteScripting) webSession.getCurrentLesson();
%>
<div id="lesson_wrapper">
	<div id="lesson_header"></div>
	<div class="lesson_workspace">
	<%
	String subViewPage = currentLesson.getPage(webSession);
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