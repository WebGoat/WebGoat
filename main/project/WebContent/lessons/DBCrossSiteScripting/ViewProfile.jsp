<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.DBCrossSiteScripting.DBCrossSiteScripting" errorPage="" %>
<%
WebSession webSession = ((WebSession)session.getAttribute("websession"));
	Employee employee = (Employee) session.getAttribute("DBCrossSiteScripting." + DBCrossSiteScripting.EMPLOYEE_ATTRIBUTE_KEY);
%>
		<div class="lesson_title_box"><strong>Welcome Back </strong><span class="lesson_text_db"><%=webSession.getUserNameInLesson()%></span></div>
		<div class="lesson_text">
				<Table>
				<TR><TD>
						First Name:
					</TD>
					<TD>
						<%=employee.getFirstName()%>
					</TD>
					<TD>				
						Last Name:
					</TD>
					<TD>
					 	<%=employee.getLastName()%>
					</TD>
				</TR>
				<TR><TD>				
						Street: 
					</TD>
					<TD>
						<%=employee.getAddress1()%>
					</TD>
					<TD>				
						City/State: 
					<TD>
						<%=employee.getAddress2()%>
					</TD>
				</TR>
				<TR><TD>
						Phone: 
					</TD>
					<TD>
						<%=employee.getPhoneNumber()%>
					</TD>
					<TD>				
						Start Date: 
					</TD>
					<TD>
						<%=employee.getStartDate()%>
					</TD>
				</TR>
				<TR><TD>
			    		SSN: 
			    	</TD>
			    	<TD>
			    		<%=employee.getSsn()%>
					</TD>
					<TD>				
						Salary: 
					</TD>
					<TD>
						<%=employee.getSalary()%>
					</TD>
				</TR>
				<TR><TD>
						Credit Card: 
					</TD>
					<TD>
						<%=employee.getCcn()%>
					</TD>
					<TD>				
						Credit Card Limit: 
					</TD>
					<TD>
						<%=employee.getCcnLimit()%>
					</TD>
				</TR>
				<TR><TD>
						Comments: 
					</TD>
					<TD>
						<%=employee.getPersonalDescription()%>
					</TD>
					<TD>				
						Manager: 
					</TD>
					<TD>
						<%=employee.getManager()%>
					</TD>	
				</TR>
				<TR><TD>
						Disciplinary Explanation: 
					</TD>
					<TD>
						<%=employee.getDisciplinaryActionNotes()%>
					</TD>
					<TD>				
						Disciplinary Action Dates: 
					</TD>
					<TD>
						<%=employee.getDisciplinaryActionDate()%>
					</TD>
				</TR>
				</Table>
		</div>
		<div class="lesson_buttons_bottom">
		    <table width="460" height="20" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                 	<td width="50">
					<%					
					if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), DBCrossSiteScripting.LISTSTAFF_ACTION))
					{
					%>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=DBCrossSiteScripting.EMPLOYEE_ID%>" value="<%=employee.getId()%>">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.LISTSTAFF_ACTION%>"/>
						</form></td>
					<% 
					}
					%>			
		             <td width="50">
					<%					
					if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), DBCrossSiteScripting.EDITPROFILE_ACTION))
					{
					%>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=DBCrossSiteScripting.EMPLOYEE_ID%>" value="<%=employee.getId()%>">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.EDITPROFILE_ACTION%>"/>
						</form>
					<% 
					}
					%>			
					</td>
                    <td width="60">
					<%
					if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), DBCrossSiteScripting.DELETEPROFILE_ACTION))
					{
					%>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=DBCrossSiteScripting.EMPLOYEE_ID%>" value="<%=employee.getId()%>">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.DELETEPROFILE_ACTION%>"/>
						</form>
					<% 
					}
					%>
					</td>
                      <td width="190">&nbsp;</td>
                      <td width="76">
						<form method="POST">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.LOGOUT_ACTION%>"/>
						</form>
					</td>
				</tr>
         	</table>
		</div>
