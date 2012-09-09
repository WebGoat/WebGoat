<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="java.util.*, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.RoleBasedAccessControl.RoleBasedAccessControl" 
	errorPage="" %>
<%
	WebSession webSession = ((WebSession)session.getAttribute("websession"));
	int myUserId = webSession.getUserIdInLesson();
%>
	<div class="lesson_title_box"><strong>Welcome Back </strong><span class="lesson_text_db"><%=webSession.getUserNameInLesson()%></span> - Staff Listing Page</div>
		<br>
		<br>
		<br>
		<p>Select from the list below	</p>

		<form id="form1" name="form1" method="post" action="<%=webSession.getCurrentLesson().getFormAction()%>">
  <table width="60%" border="0" cellpadding="3">
    <tr>
      <td>  <label>
  <select name="<%=RoleBasedAccessControl.EMPLOYEE_ID%>" size="11">
			      	<%
			      	List employees = (List) session.getAttribute("RoleBasedAccessControl." + RoleBasedAccessControl.STAFF_ATTRIBUTE_KEY);
			      	Iterator i = employees.iterator();
			      	EmployeeStub stub = (EmployeeStub) i.next();%>
			      	<option selected value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")"%></option><%
					while (i.hasNext())
					{
						stub = (EmployeeStub) i.next();%>
						<option value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")"%></option><%
					}%>
  </select>
  </label></td>
      <td>
	        	<input type="submit" name="action" value="<%=RoleBasedAccessControl.SEARCHSTAFF_ACTION%>"/><br>
	        	<input type="submit" name="action" value="<%=RoleBasedAccessControl.VIEWPROFILE_ACTION%>"/><br>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, RoleBasedAccessControl.CREATEPROFILE_ACTION))
				{
				%>
					<input type="submit" name="action" disabled value="<%=RoleBasedAccessControl.CREATEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, RoleBasedAccessControl.DELETEPROFILE_ACTION))
				{
				%>
					<input type="submit" name="action" value="<%=RoleBasedAccessControl.DELETEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
			<br>
					<input type="submit" name="action" value="<%=RoleBasedAccessControl.LOGOUT_ACTION%>"/>
	  </td>
    </tr>
  </table>

		</form>
		