<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.CrossSiteScripting.CrossSiteScripting" 
	errorPage="" %>
	<div id="lesson_search">
			<% 
			WebSession webSession = ((WebSession)session.getAttribute("websession"));
			String searchedName = request.getParameter(CrossSiteScripting.SEARCHNAME);
			if (searchedName != null)
			{
			%>
				Employee <%=searchedName%> not found.
			<%
			}
			%>
			<form id="form1" name="form1" method="post" action="<%=webSession.getCurrentLesson().getFormAction()%>">
			    	<label>Name
					<input class="lesson_text_db" type="text" name="<%=CrossSiteScripting.SEARCHNAME%>"/>
		        </label>
				<br>
				<input type="submit" name="action" value="<%=CrossSiteScripting.FINDPROFILE_ACTION%>"/>
			</form>
	</div>