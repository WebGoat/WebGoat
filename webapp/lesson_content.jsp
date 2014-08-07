<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
         import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.Category, org.owasp.webgoat.lessons.AbstractLesson, org.owasp.webgoat.util.*, java.util.*" 
         errorPage=""  %>
<%
    Course course = ((Course) session.getAttribute("course"));
    WebSession webSession = ((WebSession) session.getAttribute("websession"));
    AbstractLesson currentLesson = webSession.getCurrentLesson();
%>


<!-- HTML fragment correpsonding to the lesson content -->
<%@page import="org.owasp.webgoat.lessons.RandomLessonAdapter"%>

<!--
<link rel="stylesheet" href="css/webgoat.css" type="text/css" />
<link rel="stylesheet" href="css/lesson.css" type="text/css" />
<link rel="stylesheet" href="css/menu.css" type="text/css" />
<link rel="stylesheet" href="css/layers.css" type="text/css" />
<script language="JavaScript1.2" src="javascript/javascript.js" type="text/javascript"></script>
<script language="JavaScript1.2" src="javascript/menu_system.js" type="text/javascript"></script>
<script language="JavaScript1.2" src="javascript/lessonNav.js" type="text/javascript"></script>
<script language="JavaScript1.2" src="javascript/makeWindow.js" type="text/javascript"></script>
<script language="JavaScript1.2" src="javascript/toggle.js" type="text/javascript"></script>
-->


<div id="lessonContent">
    <%
        AbstractLesson lesson = webSession.getCurrentLesson();
        if (lesson instanceof RandomLessonAdapter) {
            RandomLessonAdapter rla = (RandomLessonAdapter) lesson;
    %>
    <div class="info">Stage <%= rla.getLessonTracker(webSession).getStageNumber(rla.getStage(webSession)) + 1%></div>
    <%
        }
    %>
    <%=webSession.getInstructions()%></div>
<div id="message" class="info"><%=webSession.getMessage()%></div>

<%
    if (currentLesson.getTemplatePage(webSession) != null) {
        //System.out.println("Main.jsp - current lesson: " + currentLesson.getName() );
        //System.out.println("         - template Page: " + currentLesson.getTemplatePage(webSession));
%>
<jsp:include page="<%=currentLesson.getTemplatePage(webSession)%>" />
<%
} else {
%>
<div id="lessonContent"><%=currentLesson.getContent()%></div>
<%
    }
%>






