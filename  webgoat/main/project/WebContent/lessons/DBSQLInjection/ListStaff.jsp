<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="java.util.*, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.DBSQLInjection.DBSQLInjection" 
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
  <select name="<%=DBSQLInjection.EMPLOYEE_ID%>" size="11">
			      	<%
			      	List employees = (List) session.getAttribute("DBSQLInjection." + DBSQLInjection.STAFF_ATTRIBUTE_KEY);
			      	Iterator i = employees.iterator();
					while (i.hasNext())
					{
						EmployeeStub stub = (EmployeeStub) i.next();%>
						<option value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName()+ " (" + stub.getRole() + ")"%></option><%
					}%>
  </select>
  </label></td>
      <td>
	        	<input type="submit" name="action" value="<%=DBSQLInjection.SEARCHSTAFF_ACTION%>"/><br>
	        	<input type="submit" name="action" value="<%=DBSQLInjection.VIEWPROFILE_ACTION%>"/><br>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, DBSQLInjection.CREATEPROFILE_ACTION))
				{
				%>
					<input type="submit" name="action" value="<%=DBSQLInjection.CREATEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
            		<% 
				if (webSession.isAuthorizedInLesson(myUserId, DBSQLInjection.DELETEPROFILE_ACTION))
				{
				%>
					<input type="submit" name="action" value="<%=DBSQLInjection.DELETEPROFILE_ACTION%>"/><br>
				<% 
				}
				%>
			<br>
					<input type="submit" name="action" value="<%=DBSQLInjection.LOGOUT_ACTION%>"/>
	  </td>
    </tr>
  </table>

		</form>
		
