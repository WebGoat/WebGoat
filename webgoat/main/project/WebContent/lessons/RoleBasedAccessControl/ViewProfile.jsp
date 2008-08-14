<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl" 
	errorPage="" %>
<%
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
	Employee employee = (Employee) session.getAttribute("RoleBasedAccessControl." + RoleBasedAccessControl.EMPLOYEE_ATTRIBUTE_KEY);
	//int myUserId = webSession.getUserIdInLesson();
%>
		<div class="lesson_title_box"><strong>Welcome Back </strong><span class="lesson_text_db"><%=webSession.getUserNameInLesson()%></span> - View Profile Page</div>
		<div class="lesson_text">
				<Table>
				<TR><TD>
						First Name:
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getFirstName())%></span>
					</TD>
					<TD>				
						Last Name:
					</TD>
					<TD>
					 	<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getLastName())%></span>
					</TD>
				</TR>
				<TR><TD>				
						Street: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getAddress1())%></span>
					</TD>
					<TD>				
						City/State: 
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getAddress2())%></span>
					</TD>
				</TR>
				<TR><TD>
						Phone: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getPhoneNumber())%></span>
					</TD>
					<TD>				
						Start Date: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getStartDate())%></span>
					</TD>
				</TR>
				<TR><TD>
			    		SSN: 
			    	</TD>
			    	<TD>
			    		<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getSsn())%></span>
					</TD>
					<TD>				
						Salary: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getSalary())%></span>
					</TD>
				</TR>
				<TR><TD>
						Credit Card: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getCcn())%></span>
					</TD>
					<TD>				
						Credit Card Limit: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getCcnLimit())%></span>
					</TD>
				</TR>
				<TR><TD>
						Comments: 
					</TD>
					<TD colspan="3">
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getPersonalDescription())%></span>
					</TD>
				</TR>				
				<TR>
					<TD colspan="2">	
						Disciplinary Explanation: 
					</TD>
					<TD>				
						Disc. Dates: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getDisciplinaryActionDate())%></span>
					</TD>
				<TR>
					<TD colspan="4">
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getDisciplinaryActionNotes())%></span>
					</TD>
				</TR>
				<TR>
				<TD>				
						Manager: 
					</TD>
					<TD>
						<span class="lesson_text_db"><%=(employee == null ? "unknown" : employee.getManager())%></span>
					</TD>	
				</TR>
				</Table>
		</div>
		<div class="lesson_buttons_bottom">
		    <table width="460" height="20" border="0" cellpadding="0" cellspacing="0">
                 <tr>
                 	<td width="50">
					 <%					
					 if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.LISTSTAFF_ACTION))
					 {
					 %>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=RoleBasedAccessControl.EMPLOYEE_ID%>" value="<%=(employee == null ? "unknown" : employee.getId())%>">
							<input type="submit" name="action" value="<%=RoleBasedAccessControl.LISTSTAFF_ACTION%>"/>
						</form>
					 <%
					 }%>
					 </td>
		             <td width="50">
					 <%					
					 if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.EDITPROFILE_ACTION))
					 {
					 %>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=RoleBasedAccessControl.EMPLOYEE_ID%>" value="<%=(employee == null ? "unknown" : employee.getId())%>">
							<input type="submit" name="action" value="<%=RoleBasedAccessControl.EDITPROFILE_ACTION%>"/>
						</form>
					<%
					}
					%>
					</td>					
                    <td width="60">
					<%					
					if (webSession.isAuthorizedInLesson(webSession.getUserIdInLesson(), RoleBasedAccessControl.DELETEPROFILE_ACTION))
					{
					%>
						<form method="POST" action="<%=webSession.getCurrentLesson().getFormAction()%>">
							<input type="hidden" name="<%=RoleBasedAccessControl.EMPLOYEE_ID%>" value="<%=(employee == null ? "unknown" : employee.getId())%>">
							<input type="submit" name="action" value="<%=RoleBasedAccessControl.DELETEPROFILE_ACTION%>"/>
						</form>
					<%
					}
					%>
					</td>
                      <td width="190">&nbsp;</td>
                      <td width="76">
						<form method="POST">
							<input type="submit" name="action" value="<%=RoleBasedAccessControl.LOGOUT_ACTION%>"/>
						</form>
					</td>
				</tr>
         	</table>
		</div>