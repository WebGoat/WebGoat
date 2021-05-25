package org.owasp.webgoat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SqlInjectionLessonTest extends IntegrationTest {

    public static final String sql_2 = "select department from employees where last_name='Franco'";
    public static final String sql_3 = "update employees set department='Sales' where last_name='Barnett'";
    public static final String sql_4_drop = "alter table employees drop column phone";
    public static final String sql_4_add = "alter table employees add column phone varchar(20)";
    public static final String sql_5 = "grant select on grant_rights to unauthorized_user";
    public static final String sql_9_account = " ' ";
    public static final String sql_9_operator = "or";
    public static final String sql_9_injection = "'1'='1";
    public static final String sql_10_login_count = "2";
    public static final String sql_10_userid = "1 or 1=1";

    public static final String sql_11_a = "Smith' or '1' = '1";
    public static final String sql_11_b = "3SL99A'  or '1'='1";

    public static final String sql_12_a = "Smith";
    public static final String sql_12_b = "3SL99A' ; update employees set salary= '100000' where last_name='Smith";

    public static final String sql_13 = "%update% '; drop table access_log ; --'";

    @Test
    public void runTests() {
        startLesson("SqlInjection");

        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("query", sql_2);
        checkAssignment(url("/WebGoat/SqlInjection/attack2"), params, true);

        params.clear();
        params.put("query", sql_3);
        checkAssignment(url("/WebGoat/SqlInjection/attack3"), params, true);

        params.clear();
        params.put("query", sql_4_add);
        checkAssignment(url("/WebGoat/SqlInjection/attack4"), params, true);

        params.clear();
        params.put("query", sql_5);
        checkAssignment(url("/WebGoat/SqlInjection/attack5"), params, true);

        params.clear();
        params.put("operator", sql_9_operator);
        params.put("account", sql_9_account);
        params.put("injection", sql_9_injection);
        checkAssignment(url("/WebGoat/SqlInjection/assignment5a"), params, true);

        params.clear();
        params.put("login_count", sql_10_login_count);
        params.put("userid", sql_10_userid);
        checkAssignment(url("/WebGoat/SqlInjection/assignment5b"), params, true);

        params.clear();
        params.put("name", sql_11_a);
        params.put("auth_tan", sql_11_b);
        checkAssignment(url("/WebGoat/SqlInjection/attack8"), params, true);

        params.clear();
        params.put("name", sql_12_a);
        params.put("auth_tan", sql_12_b);
        checkAssignment(url("/WebGoat/SqlInjection/attack9"), params, true);

        params.clear();
        params.put("action_string", sql_13);
        checkAssignment(url("/WebGoat/SqlInjection/attack10"), params, true);

        checkResults("/SqlInjection/");

    }
}
