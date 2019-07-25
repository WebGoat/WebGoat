package org.owasp.webgoat;

import java.util.HashMap;
import java.util.Map;

public class SqlInjectionMitigation_TestHelper extends TestHelper {

	public void runTests(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "SqlInjectionMitigations");
		
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("field1", "getConnection");
		params.put("field2", "PreparedStatement prep");
		params.put("field3", "prepareStatement");
		params.put("field4", "?");
		params.put("field5", "?");
		params.put("field6", "prep.setString(1,\"\")");
		params.put("field7", "prep.setString(2,\\\"\\\")");
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionMitigations/attack10a", params, true);
	
		params.put("editor", "try {\r\n" + 
				"    Connection conn = DriverManager.getConnection(DBURL,DBUSER,DBPW);\r\n" + 
				"    PreparedStatement prep = conn.prepareStatement(\"select id from users where name = ?\");\r\n" + 
				"    prep.setString(1,\"me\");\r\n" + 
				"    prep.execute();\r\n" + 
				"    System.out.println(conn);   //should output 'null'\r\n" + 
				"} catch (Exception e) {\r\n" + 
				"    System.out.println(\"Oops. Something went wrong!\");\r\n" + 
				"}");
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionMitigations/attack10b", params, true);
		
		//checkResults(cookie, webgoatURL, "/SqlInjectionMitigations/");
	
	}
}
