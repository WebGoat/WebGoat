<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
         import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.Category, org.owasp.webgoat.lessons.AbstractLesson, org.owasp.webgoat.util.*, java.util.*" 
         errorPage=""  %>
<%
    WebSession webSession = ((WebSession) session.getAttribute(WebSession.SESSION));
    Course course = webSession.getCourse();
    AbstractLesson currentLesson = webSession.getCurrentLesson();
    LabelManager labelManager = BeanProvider.getBean("labelManager", LabelManager.class);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.owasp.webgoat.lessons.RandomLessonAdapter"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title><%=currentLesson.getTitle()%></title>
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
    <%
        final String menuPrefix = WebSession.MENU;
        final String submenuPrefix = "submenu";
        final String mbutPrefix = "mbut";
        String printHint = "";
        String printParameters = "";
        String printCookies = "";
        String lessonComplete = "<img src=\"images/buttons/lessonComplete.jpg\">";

        List categories = course.getCategories();

        StringBuffer buildList = new StringBuffer();

        Iterator iter1 = categories.iterator();
        while (iter1.hasNext()) {
            Category category = (Category) iter1.next();

            buildList.append("'");
            buildList.append(menuPrefix);
            buildList.append(category.getRanking());
            buildList.append("','");
            buildList.append(submenuPrefix);
            buildList.append(category.getRanking());
            buildList.append("','");
            buildList.append(mbutPrefix);
            buildList.append(category.getRanking());
            buildList.append("'");

            if (iter1.hasNext()) {
                buildList.append(",");
            }
        }%>
    <body class="page" onload="setMenuMagic1(10, 40, 10, 'menubottom',<%=buildList%>);
        trigMM1url('<%= menuPrefix%>', 1);
        MM_preloadImages('images/buttons/hintLeftOver.jpg', 'images/buttons/hintOver.jpg', 'images/buttons/hintRightOver.jpg', 'images/buttons/paramsOver.jpg', 'images/buttons/htmlOver.jpg', 'images/buttons/cookiesOver.jpg', 'images/buttons/javaOver.jpg', 'images/buttons/plansOver.jpg', 'images/buttons/logout.jpg', 'images/buttons/helpOver.jpg');
        initIframe();">

        <div id="wrap">
            <%
                int topCord = 140;
                int zIndex = 105;

                Iterator iter2 = categories.iterator();
                while (iter2.hasNext()) {
                    Category category = (Category) iter2.next();
            %>
            <div id="<%=menuPrefix + category.getRanking()%>" style="position:absolute; left:30px; top:<%=topCord%>px; width:160px; z-index:<%=zIndex%>"><a href="javascript:;" onclick="trigMenuMagic1('<%=menuPrefix + category.getRanking()%>', 1);
                        return false" onfocus="if (this.blur)
                                    this.blur()"><img src="images/menu_images/1x1.gif" width="1" height=1"20" name="mbut<%=category.getRanking()%>" border="0" alt=""/><%=category.getName()%></a></div>
                <%
                        topCord = topCord + 30;
                        zIndex = zIndex + 1;
                    }

                    int topSubMenu = 72;

                    Iterator iter3 = categories.iterator();
                    while (iter3.hasNext()) {
                        Category category = (Category) iter3.next();
                        List lessons = webSession.getLessons(category);
                        Iterator iter4 = lessons.iterator();
                %>    
            <div id="submenu<%=category.getRanking()%>" class="pviimenudiv" style="position:absolute; left:200px; top:<%=topSubMenu%>px; width:150px; visibility: hidden; z-index:<%=zIndex%>">
                <table width="150" border="0" cellspacing="6" cellpadding="0"><%

                    topSubMenu = topSubMenu + 30;
                    zIndex = zIndex + 1;

                    while (iter4.hasNext()) {
                        AbstractLesson lesson = (AbstractLesson) iter4.next();

                    %><tr>
                        <td><%=(lesson.isCompleted(webSession) ? lessonComplete : "")%><a href="<%=lesson.getLink()%>"><%=lesson.getTitle()%></a></td>
                    </tr>
                    <% if (lesson instanceof RandomLessonAdapter) {
                            RandomLessonAdapter rla = (RandomLessonAdapter) lesson;
                            String[] stages = rla.getStages();
                            if (stages != null)
                                for (int i = 0; i < stages.length; i++) {
                    %>
                    <tr><td class="pviimenudivstage"><%=(rla.isStageComplete(webSession, stages[i]) ? lessonComplete : "")%><a href="<%=lesson.getLink() + "/" + (i + 1)%>">Stage <%=i + 1%>: <%=stages[i]%></a>
                        </td></tr>
                        <%
                                    }
                            }
                        %>
                        <%
                            }
                        %>
                </table>
            </div><%
                    }%>
            <div id="top"></div>
            <div id="topLeft">
                <div align="left">
                    <% if (currentLesson.getAvailableLanguages().size() != 0) {
                    %>
                    <form method="get" action="attack" style="display: inline;">
                        Choose another language: <select name="language" size="1"
                                                         onChange="changeLanguage();">
                            <%
                                for (String lang : currentLesson.getAvailableLanguages()) {
                            %>
                            <option value="<%=lang%>"
                                    <% if (webSession.getCurrrentLanguage().equals(lang)) {
                                        out.println("selected");
                                    }%>><%=lang%>
                            </option>
                            <%

                                }
                            %>
                        </select></form>
                        <%
                        } else {
                        %>
                    Internationalization is not available for this lesson
                    <%
                        }
                    %>
                </div></div>
            <div align="right" id="topRight">
                <a href="j_spring_security_logout" onmouseout="MM_swapImgRestore()"
                   onmouseover="MM_swapImage('logout', '', 'images/buttons/logoutOver.jpg', 1)"><img
                        src="images/buttons/logout.jpg" alt="LogOut" name="logout" width="45"
                        height="22" border="0" id="logout" /></a> <a href="#getFAQ()"
                                                             onmouseout="MM_swapImgRestore()"
                                                             onmouseover="MM_swapImage('help', '', 'images/buttons/helpOver.jpg', 1)"><img
                        src="images/buttons/help.jpg" alt="Help" name="help" width="22"
                        height="22" border="0" id="help" /></a>
            </div>
            <div id="lessonTitle" align="right"><%=currentLesson.getTitle()%></div>
            <div id="hMenuBar">
                <%
                    if (webSession.isAuthorizedInLesson(webSession.getRole(), WebSession.SHOWHINTS)) {
                %>
                <a href="<%= webSession.getCurrentLesson().getLink()%>&show=PreviousHint" target="_top" onclick="MM_nbGroup('down', 'group1', 'hintLeft', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'hintLeft', 'images/buttons/hintLeftOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/hintLeft.jpg" alt="Previous Hint" name="hintLeft" width="20" height="20" border="0" id="hintLeft"/>
                </a>
                <a href="<%= webSession.getCurrentLesson().getLink()%>&show=NextHint" target="_top" onclick="MM_nbGroup('down', 'group1', 'hint', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'hint', 'images/buttons/hintOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/hint.jpg" alt="Hints" name="hint" width="35" height="20" border="0" id="hint"/>
                </a>
                <a href="<%= webSession.getCurrentLesson().getLink()%>&show=NextHint" target="_top" onclick="MM_nbGroup('down', 'group1', 'hintRight', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'hintRight', 'images/buttons/hintRightOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/hintRight.jpg" alt="Next Hint" name="hintRight" width="20" height="20" border="0" id="hintRight"/>
                </a>
                <%}%>
                <a href="<%= webSession.getCurrentLesson().getLink()%>&show=Params" target="_top" onclick="MM_nbGroup('down', 'group1', 'params', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'params', 'images/buttons/paramsOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/params.jpg" alt="Show Params" name="<%= webSession.getCurrentLesson().getLink()%>&show=Params" width="87" height="20" border="0" id="params"/>
                </a>
                <a href="<%= webSession.getCurrentLesson().getLink()%>&show=Cookies" target="_top" onclick="MM_nbGroup('down', 'group1', 'cookies', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'cookies', 'images/buttons/cookiesOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/cookies.jpg" alt="Show Cookies" name="cookies" width="99" height="20" border="0" id="cookies"/>
                </a>
                <a href="javascript:toggle('lessonPlans')" target="_top" onclick="MM_nbGroup('down', 'group1', 'plans', '', 1)" 
                   onmouseover="MM_nbGroup('over', 'plans', 'images/buttons/plansOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/plans.jpg" alt="Lesson Plans" width="89" height="20" border="0" id="plans"/>
                </a>
                <%
                    if (webSession.isAuthorizedInLesson(webSession.getRole(), WebSession.SHOWSOURCE)) {
                %>
                <a href="source" onclick="makeWindow(this.href + '?source=true', 'Java Source');
                                        return false;" target="javaWin"
                   onmouseover="MM_nbGroup('over', 'java', 'images/buttons/javaOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/java.jpg" alt="Show Java" name="java" width="75" height="20" border="0" id="java"/>
                </a>
                <a href="source" onclick="makeWindow(this.href + '?solution=true', 'Java Solution');
                                        return false;" target="javaWin"
                   onmouseover="MM_nbGroup('over', 'solutions', 'images/buttons/solutionsOver.jpg', '', 1)" 
                   onmouseout="MM_nbGroup('out')">
                    <img src="images/buttons/solutions.jpg" alt="Show Solution" name="solutions" width="73" height="20" border="0" id="solutions"/>
                </a>
                <%}%>

            </div>
            <div id="twoCol">
                <div id="menuSpacer"></div>
                <div id="lessonAreaTop">
                    <%
                        if (currentLesson != null) {
                    %>
                    <div id="training_wrap">
                        <div id="training" class="info"><a href="http://yehg.net/lab/pr0js/training/webgoat.php" target="_blank"><%=labelManager.get("SolutionVideos")%></a></div>
                        <div id="reset" class="info"><a href="<%=webSession.getRestartLink()%>"><%=labelManager.get("RestartLesson")%></a></div>
                    </div>
                    <%
                        }
                    %>
                </div>
                <div id="lessonArea">
                    <%
                        if (webSession.getHint() != null) {
                            printHint = "<div id=\"hint\" class=\"info\">" + webSession.getHint() + "</div><br>";
                            out.println(printHint);
                        }

                        if (webSession.getParams() != null) {
                            Iterator i = webSession.getParams().iterator();
                            while (i.hasNext()) {
                                Parameter p = (Parameter) i.next();
                                printParameters = "<div id=\"parameter\" class=\"info\">" + p.getName() + "=" + p.getValue() + "</div><br>";
                                out.println(printParameters);
                            }
                        }

                        if (webSession.getCookies() != null) {
                            Iterator i = webSession.getCookies().iterator();
                            while (i.hasNext()) {
                                Cookie c = (Cookie) i.next();
                                printCookies = "<div id=\"cookie\" class=\"info\">" + c.getName() + " <img src=\"images/icons/rightArrow.jpg\" alt=\"\"> " + c.getValue() + "</div><br>";
                                out.println(printCookies);
                                }
                            }%>
                    <div id="lessonPlans" style="visibility:hidden; height:1px; position:absolute; left:260px; top:130px; width:425px; z-index:105;"><%=currentLesson.getLessonPlan(webSession)%>
                        <br/>
                        <br/>
                        <a href="javascript:toggle('lessonPlans')" target="_top" onclick="MM_nbGroup('down', 'group1', 'plans', '', 1)">Close this Window</a>
                    </div>
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
                </div>
            </div>

            <div id="bottom">
                <div align="center"><a href="http://www.owasp.org">OWASP Foundation</a> | 
                    <a href="http://www.owasp.org/index.php/OWASP_WebGoat_Project">Project WebGoat</a> | 
                    <a href="reportBug.jsp">Report Bug</a>
                </div>
            </div>
        </div>
    </body>
</html>
