<%@ page contentType="text/html; charset=ISO-8859-1" language="java" 
	import="java.util.*, org.owasp.webgoat.session.*, org.owasp.webgoat.lessons.DBSQLInjection.DBSQLInjection" 
	errorPage="" %>
	<div id="lesson_login">
		<div id="lesson_login_txt">
			<% 
			WebSession webSession = ((WebSession)session.getAttribute("websession"));
			%>
			<form id="form1" name="form1" method="post" action="attack?menu=<%=webSession.getCurrentMenu()%>">
			    	<label>
			      	<select name="<%=DBSQLInjection.EMPLOYEE_ID%>">
			      	<%
			      	Vector attrs = new Vector();
			      	Enumeration ee = session.getAttributeNames();
			      	while (ee.hasMoreElements())
			      		attrs.add(ee.nextElement());
			      	//System.out.println("Login.jsp inspecting session attributes: " + attrs);
			      	//System.out.println("Retrieving employees list");
			      	List employees = (List) session.getAttribute("DBSQLInjection." + DBSQLInjection.STAFF_ATTRIBUTE_KEY);
			      	Iterator i = employees.iterator();
					while (i.hasNext())
					{
						EmployeeStub stub = (EmployeeStub) i.next();
					%>
			      	<option value="<%=Integer.toString(stub.getId())%>"><%=stub.getFirstName() + " " + stub.getLastName() + " (" + stub.getRole() + ")"%></option>
					<%}%>
	                </select>
		        </label>
				<br>
			    	<label>Password
			    		<input name="password" type="password" size="10" maxlength="8" />
			    </label>
				<br>
				<input type="submit" name="action" value="<%=DBSQLInjection.LOGIN_ACTION%>"/>
			</form>
		</div>
	</div>