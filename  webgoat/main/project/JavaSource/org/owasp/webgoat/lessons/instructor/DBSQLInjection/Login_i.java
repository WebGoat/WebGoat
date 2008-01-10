package org.owasp.webgoat.lessons.instructor.DBSQLInjection;

/*
 * The solution is to choose Neville's userid, and enter a password like:
 * ' OR userid=112 OR password='
 * Modify the Stored procedure LOGIN_EMPLOYEE to use fixed statements or bind variables
 * 
 * 
CREATE OR REPLACE PROCEDURE EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) AS
    v_userid NUMBER;
BEGIN
  SELECT USERID INTO v_userid FROM EMPLOYEE
    WHERE USERID = v_id
      AND PASSWORD = v_password;
END;
/

* OR

CREATE OR REPLACE PROCEDURE EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) AS
    stmt VARCHAR(1000);
    v_userid NUMBER;
BEGIN
    stmt  := 'SELECT USERID FROM EMPLOYEE WHERE USERID = :1 AND PASSWORD = :2';
    EXECUTE IMMEDIATE stmt INTO v_userid USING v_id, v_password;
END;
/
    
*/
