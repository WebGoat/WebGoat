<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.AbstractLesson, org.owasp.webgoat.util.*" 
	errorPage=""
	isELIgnored="false" %>
	   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title><tiles:insertAttribute name="title-content" /></title>
		<link rel="stylesheet" href="css/webgoat.css" type="text/css" />
		<link rel="stylesheet" href="css/lesson.css" type="text/css" />
		<link rel="stylesheet" href="css/menu.css" type="text/css" />
		<link rel="stylesheet" href="css/layers.css" type="text/css" />
		<script language="JavaScript1.2" src="javascript/javascript.js" type="text/javascript"></script>
		<script language="JavaScript1.2" src="javascript/menu_system.js" type="text/javascript"></script>
		<script language="JavaScript1.2" src="javascript/lessonNav.js" type="text/javascript"></script>
		<script language="JavaScript1.2" src="javascript/makeWindow.js" type="text/javascript"></script>
		<script language="JavaScript1.2" src="javascript/toggle.js" type="text/javascript"></script>
	</head>
	
	<body>
		<%
		Course course = ((Course)session.getAttribute("course"));
		WebSession webSession = ((WebSession)session.getAttribute("websession"));
		
		// pcs 8/29/2012 - HACK  
		// 
		// Legacy lessons result in a call to WebSession.update().  Among other things, that call
		// sets the previous and current screens.  The latter determines the title that is displayed 
		// in the webgoat banner.  
		//
		// The new Spring-MVC jsps, among which is this genericLesson.jsp, are loaded via our dispatcher servlet
		// and does not pass through the code path that results in that update() call.  
		// 
		// As a result, we must call update() explicitly here.  If we refactor away that legacy code as part
		// of webgoat 6 development, we will need to get rid of the call below.
		//
		webSession.update(request, response, "genericLesson");
		AbstractLesson currentLesson = webSession.getCurrentLesson();
		%>
		
		<div id="header-style"><tiles:insertAttribute name="header-content" /></div>
		<div><tiles:insertAttribute name="menu-content" /></div>
		<div id="lessonTitle" align="right"><%= currentLesson.getTitle() %></div>
		<div id="primary-style"">
			<div id="lessonArea">			
				<tiles:insertAttribute name="hints-params-cookies" />
				<div id="twoCol">
			 	 	<div id="menuSpacer"></div>
			 	 	<div id="lessonAreaTop">	 	 	
					    <div id="training_wrap">
							<div id="training" class="info"><a href="http://yehg.net/lab/pr0js/training/webgoat.php" target="_blank"><%=WebGoatI18N.get("SolutionVideos")%></a></div>
							<div id="reset" class="info"><a href="<%=webSession.getRestartLink()%>"><%=WebGoatI18N.get("RestartLesson")%></a></div>
						</div>
					</div>
				</div>	
				<div id="lessonContent">
					<tiles:insertAttribute name="primary-content" />
				</div>
			</div>
		</div>	
		<div id="footer-style"><tiles:insertAttribute name="footer-content" /></div>
	</body>
</html>