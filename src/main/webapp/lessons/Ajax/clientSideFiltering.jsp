
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.io.*, javax.xml.xpath.*, org.xml.sax.InputSource,org.w3c.dom.*,org.apache.ecs.html.* " %>  
    
<% 

	String userId = request.getParameter("userId");


	NodeList nodes = null;
	
	

	File d = new File(this.getServletContext().getRealPath("lessons/Ajax/employees.xml"));
	
	if(d.exists()){
		System.out.print("File does exist");
	}
	else{
		System.out.print("File DOES NOT exist");
	}
	
	System.out.println(d.getAbsolutePath());
	XPathFactory factory = XPathFactory.newInstance();
	XPath xPath = factory.newXPath();
	InputSource inputSource = new InputSource(new FileInputStream(d));





	
	StringBuffer sb = new StringBuffer();
	
	sb.append("/Employees/Employee/UserID | ");
	sb.append("/Employees/Employee/FirstName | ");
	sb.append("/Employees/Employee/LastName | ");
	sb.append("/Employees/Employee/SSN | ");
	sb.append("/Employees/Employee/Salary ");
	
	String expression = sb.toString();
	
	
	System.out.print("expression:" + expression);



	nodes = (NodeList) xPath.evaluate(expression, inputSource,
	XPathConstants.NODESET);
	int nodesLength = nodes.getLength();

	
	System.out.println("nodesLength:" + nodesLength);

	TR tr;
	
	int COLUMNS = 5;
	
	Table t2 = null;
    if (nodesLength > 0)
    {
		t2 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(
			1).setWidth("90%").setAlign("center");
		tr = new TR();	
		tr.addElement(new TD().addElement("UserID"));
		tr.addElement(new TD().addElement("First Name"));
		tr.addElement(new TD().addElement("Last Name"));
		tr.addElement(new TD().addElement("SSN"));
		tr.addElement(new TD().addElement("Salary"));
		t2.addElement(tr);
    }
    

    
    tr = new TR();
    
    for (int i = 0; i < nodesLength; i++)
    {
		Node node = nodes.item(i);
		
		if(i%COLUMNS==0){
			tr = new TR();
			tr.setID(node.getTextContent());
			//tr.setStyle("display: none");
		}
		
		tr.addElement(new TD().addElement(node.getTextContent()));
		
		if(i%COLUMNS==(COLUMNS-1)){
			t2.addElement(tr);		
		}
    }
    
    if(t2 != null){
   	 	out.println(t2.toString());
    }
    else{
    	out.println("No Results");
    }
    
    

    
	
	
	
	
	


%>
    
