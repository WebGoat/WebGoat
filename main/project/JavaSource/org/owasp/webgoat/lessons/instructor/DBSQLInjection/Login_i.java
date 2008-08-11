
package org.owasp.webgoat.lessons.instructor.DBSQLInjection;

/*
 * The solution is to choose Neville's userid, and enter a password like:
 * ' OR '1'='1
 * Modify the Stored function LOGIN_EMPLOYEE to use fixed statements or bind variables
 * 
 * 
 * For ORACLE:
CREATE OR REPLACE FUNCTION WEBGOAT_guest.EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) RETURN NUMBER AS
    cnt NUMBER;
BEGIN
  SELECT COUNT(*) INTO cnt FROM EMPLOYEE
    WHERE USERID = v_id
      AND PASSWORD = v_password;
  RETURN cnt;
END;
/

* OR

CREATE OR REPLACE FUNCTION WEBGOAT_guest.EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) RETURN NUMBER AS
    stmt VARCHAR(32767); cnt NUMBER;
BEGIN
    stmt  := 'SELECT COUNT (*) FROM EMPLOYEE WHERE USERID = :1 AND PASSWORD = :2';
    EXECUTE IMMEDIATE stmt INTO cnt USING v_id, v_password;
    RETURN cnt;
END;
/
    
 * For SQL SERVER
    
CREATE FUNCTION webgoat_guest.EMPLOYEE_LOGIN (
    @v_id INT,
    @v_password VARCHAR(100)
) RETURNS INTEGER
AS
    BEGIN
        DECLARE @count int
        SELECT @count = COUNT(*) FROM EMPLOYEE WHERE USERID = @v_id AND PASSWORD = @v_password;
        return @count
    END

*/
