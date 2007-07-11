DROP USER webgoat;
CREATE USER webgoat IDENTIFIED BY webgoat DEFAULT TABLESPACE users;
GRANT CONNECT, RESOURCE TO webgoat;
GRANT CREATE PROCEDURE TO webgoat;

CREATE OR REPLACE PROCEDURE EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) AS
    stmt VARCHAR(32767);v_userid NUMBER;
BEGIN
    stmt  := 'SELECT USERID FROM EMPLOYEE WHERE USERID = ' || v_id || ' AND PASSWORD = ''' || v_password || '''';
    EXECUTE IMMEDIATE stmt INTO v_userid;
END;

CREATE OR REPLACE PROCEDURE EMPLOYEE_LOGIN_BACKUP(v_id NUMBER, v_password VARCHAR) AS
    stmt VARCHAR(32767);v_userid NUMBER;
BEGIN
    stmt  := 'SELECT USERID FROM EMPLOYEE WHERE USERID = ' || v_id || ' AND PASSWORD = ''' || v_password || '''';
    EXECUTE IMMEDIATE stmt INTO v_userid;
END;

CREATE OR REPLACE PROCEDURE UPDATE_EMPLOYEE(
    v_userid IN employee.userid%type, 
    v_first_name IN employee.first_name%type, 
    v_last_name IN employee.last_name%type, 
    v_ssn IN employee.ssn%type, 
    v_title IN employee.title%type, 
    v_phone IN employee.phone%type, 
    v_address1 IN employee.address1%type, 
    v_address2 IN employee.address2%type, 
    v_manager IN employee.manager%type, 
    v_start_date IN employee.start_date%type, 
    v_salary IN employee.salary%type, 
    v_ccn IN employee.ccn%type, 
    v_ccn_limit IN employee.ccn_limit%type, 
    v_disciplined_date IN employee.disciplined_date%type, 
    v_disciplined_notes IN employee.disciplined_notes%type, 
    v_personal_description IN employee.personal_description%type
)
AS 
BEGIN
    UPDATE EMPLOYEE
    SET
        first_name = v_first_name, 
        last_name = v_last_name, 
        ssn = v_ssn, 
        title = v_title, 
        phone = v_phone, 
        address1 = v_address1, 
        address2 = v_address2, 
        manager = v_manager, 
        start_date = v_Start_date,
        salary = v_salary, 
        ccn = v_ccn, 
        ccn_limit = v_ccn_limit, 
        disciplined_date = v_disciplined_date, 
        disciplined_notes = v_disciplined_notes, 
        personal_description = v_personal_description
    WHERE
        userid = v_userid;
END;

CREATE OR REPLACE PROCEDURE UPDATE_EMPLOYEE_BACKUP(
    v_userid IN employee.userid%type, 
    v_first_name IN employee.first_name%type, 
    v_last_name IN employee.last_name%type, 
    v_ssn IN employee.ssn%type, 
    v_title IN employee.title%type, 
    v_phone IN employee.phone%type, 
    v_address1 IN employee.address1%type, 
    v_address2 IN employee.address2%type, 
    v_manager IN employee.manager%type, 
    v_start_date IN employee.start_date%type, 
    v_salary IN employee.salary%type, 
    v_ccn IN employee.ccn%type, 
    v_ccn_limit IN employee.ccn_limit%type, 
    v_disciplined_date IN employee.disciplined_date%type, 
    v_disciplined_notes IN employee.disciplined_notes%type, 
    v_personal_description IN employee.personal_description%type
)
AS 
BEGIN
    UPDATE EMPLOYEE
    SET
        first_name = v_first_name, 
        last_name = v_last_name, 
        ssn = v_ssn, 
        title = v_title, 
        phone = v_phone, 
        address1 = v_address1, 
        address2 = v_address2, 
        manager = v_manager, 
        start_date = v_Start_date,
        salary = v_salary, 
        ccn = v_ccn, 
        ccn_limit = v_ccn_limit, 
        disciplined_date = v_disciplined_date, 
        disciplined_notes = v_disciplined_notes, 
        personal_description = v_personal_description
    WHERE
        userid = v_userid;
END;


exit;
