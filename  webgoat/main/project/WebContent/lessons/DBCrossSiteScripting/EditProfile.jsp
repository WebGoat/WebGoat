<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="java.util.*, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.DBCrossSiteScripting.DBCrossSiteScripting" 
	errorPage="" %>
<%
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
	Employee employee = (Employee) session.getAttribute("DBCrossSiteScripting.Employee");
%>
		<div class="lesson_title_box"><strong>Welcome Back </strong><span class="lesson_text_db"><%=webSession.getUserNameInLesson()%></span></div>
		<div class="lesson_text">
			<form id="form1" name="form1" method="post" action="<%=webSession.getCurrentLesson().getFormAction()%>">
				<Table>
				<TR><TD>
						First Name:
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.FIRST_NAME%>" type="text" value="<%=employee.getFirstName()%>"/>
					</TD>
					<TD>				
						Last Name:
					</TD>
					<TD>
					 	<input class="lesson_text_db" name="<%=DBCrossSiteScripting.LAST_NAME%>" type="text" value="<%=employee.getLastName()%>"/>
					</TD>
				</TR>
				<TR><TD>				
						Street: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.ADDRESS1%>" type="text" value="<%=employee.getAddress1()%>"/>
					</TD>
					<TD>				
						City/State: 
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.ADDRESS2%>" type="text" value="<%=employee.getAddress2()%>"/>
					</TD>
				</TR>
				<TR><TD>
						Phone: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.PHONE_NUMBER%>" type="text" value="<%=employee.getPhoneNumber()%>"/>
					</TD>
					<TD>				
						Start Date: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.START_DATE%>" type="text" value="<%=employee.getStartDate()%>"/>
					</TD>
				</TR>
				<TR><TD>
			    		SSN: 
			    	</TD>
			    	<TD>
			    		<input class="lesson_text_db" name="<%=DBCrossSiteScripting.SSN%>" type="text" value="<%=employee.getSsn()%>"/> 
					</TD>
					<TD>				
						Salary: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.SALARY%>" type="text" value="<%=employee.getSalary()%>"/>
					</TD>
				</TR>
				<TR><TD>
						Credit Card: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.CCN%>" type="text" value="<%=employee.getCcn()%>"/>
					</TD>
					<TD>				
						Credit Card Limit: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.CCN_LIMIT%>" type="text" value="<%=employee.getCcnLimit()%>"/>
					</TD>
				</TR>
				<TR><TD>
						Comments: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.DESCRIPTION%>" type="text" value="<%=employee.getPersonalDescription()%>"/>
					</TD>
					<TD>				
						Manager: 
					</TD>
					<TD>
						<select class="lesson_text_db" name="<%=DBCrossSiteScripting.MANAGER%>">
						<%
				      	List employees = (List) session.getAttribute("DBCrossSiteScripting.Staff");
				      	Iterator i = employees.iterator();
						while (i.hasNext())
						{
							EmployeeStub stub = (EmployeeStub) i.next();
								%>
								<option value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()%></option>
						<%}%>
						</select>
					</TD>	
				</TR>
				<TR><TD>
						Disciplinary Explanation: 
					</TD>
					<TD>
						<textarea name="<%=DBCrossSiteScripting.DISCIPLINARY_NOTES%>" cols="16" rows="3" class="lesson_text_db" ><%=employee.getDisciplinaryActionNotes()%></textarea>
					</TD>
					<TD>				
						Disciplinary Action Dates: 
					</TD>
					<TD>
						<input class="lesson_text_db" name="<%=DBCrossSiteScripting.DISCIPLINARY_DATE%>" type="text" value="<%=employee.getDisciplinaryActionDate()%>"/>
					</TD>
				</TR>
				</Table>
				<BR>
				<div class="lesson_buttons_bottom">
				<table width="460" height="20" border="0" cellpadding="0" cellspacing="0">
               		<tr>
                     		<td width="57">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.VIEWPROFILE_ACTION%>"/>
				  		</td>
				  		
                       	<td width="81">
 							<input name="<%=DBCrossSiteScripting.EMPLOYEE_ID%>" type="hidden" value="<%=employee.getId()%>">
							<input name="<%=DBCrossSiteScripting.TITLE%>" type="hidden" value="<%=employee.getTitle()%>">
							<input type="submit" name="action" value="<%=DBCrossSiteScripting.UPDATEPROFILE_ACTION%>"/>
						</td>
                        	<td width="211"></td>
                        	<td width="83">
	 						<input type="submit" name="action" value="<%=DBCrossSiteScripting.LOGOUT_ACTION%>"/>
						</td>
                 	</tr>
              	</table>
              	</div>
			</form>
		</div>