DROP USER webgoat_guest CASCADE;
CREATE USER webgoat_guest IDENTIFIED BY webgoat DEFAULT TABLESPACE users;
GRANT CONNECT, RESOURCE TO webgoat_guest;
GRANT CREATE PROCEDURE TO webgoat_guest;

CREATE TABLE WEBGOAT_guest.EMPLOYEE (
    userid INT NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    ssn VARCHAR(12),
    password VARCHAR(10),
    title VARCHAR(20),
    phone VARCHAR(13),
    address1 VARCHAR(80),
    address2 VARCHAR(80),
    manager INT,
    start_date CHAR(8),
    salary INT,
    ccn VARCHAR(30),
    ccn_limit INT,
    disciplined_date CHAR(8),
    disciplined_notes VARCHAR(60),
    personal_description VARCHAR(60)
);


CREATE OR REPLACE FUNCTION WEBGOAT_guest.EMPLOYEE_LOGIN(v_id NUMBER, v_password VARCHAR) RETURN NUMBER AS
    stmt VARCHAR(32767);cnt NUMBER;
BEGIN
    stmt  := 'SELECT COUNT (*) FROM EMPLOYEE WHERE USERID = ' || v_id || ' AND PASSWORD = ''' || v_password || '''';
    EXECUTE IMMEDIATE stmt INTO cnt;
    RETURN cnt;
END;
/

CREATE OR REPLACE FUNCTION WEBGOAT_guest.EMPLOYEE_LOGIN_BACKUP(v_id NUMBER, v_password VARCHAR) RETURN NUMBER AS
    stmt VARCHAR(32767);cnt NUMBER;
BEGIN
    stmt  := 'SELECT COUNT (*) FROM EMPLOYEE WHERE USERID = ' || v_id || ' AND PASSWORD = ''' || v_password || '''';
    EXECUTE IMMEDIATE stmt INTO cnt;
    RETURN cnt;
END;
/

CREATE OR REPLACE PROCEDURE WEBGOAT_guest.UPDATE_EMPLOYEE(
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
/

CREATE OR REPLACE PROCEDURE WEBGOAT_guest.UPDATE_EMPLOYEE_BACKUP(
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
/


exit;


