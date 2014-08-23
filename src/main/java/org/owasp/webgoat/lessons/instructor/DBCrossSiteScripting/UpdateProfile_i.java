
package org.owasp.webgoat.lessons.instructor.DBCrossSiteScripting;

import org.owasp.webgoat.lessons.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.lessons.CrossSiteScripting.UpdateProfile;
import org.owasp.webgoat.lessons.GoatHillsFinancial.GoatHillsFinancial;

/* STAGE 2 FIXES
Solution Summary (1. or 2.) 
    1. Modify the UPDATE_EMPLOYEE stored procedure in the database and add
       a validation step. Oracle 10G now supports regular expressions.
    2. Apply a column constraint can also work IFF the existing data is clean

Solution Steps: 
1. Talk about the different database approaches.
	a. Apply validation in the UPDATE stored proc
		- Possible to bypass by not using that stored proc

	b. Apply a table column constraint 
		- Cannot be bypassed. The DB enforces the constraint under all conditions

2. Fix the stored proc

Define the pattern.
Validate the field against the pattern.
Raise an exception if invalid.

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
    P_ADDRESS1 VARCHAR2(100) := '^[a-zA-Z0-9,\. ]{0,80}$';  
BEGIN
    IF NOT REGEXP_LIKE(v_address1, P_ADDRESS1) THEN     
        RAISE VALUE_ERROR;                              
    END IF;                                             
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

3. Apply a table column constraint
   	ALTER TABLE EMPLOYEE
		ADD CONSTRAINT address1_ck CHECK (REGEXP_LIKE(address1, '^[a-zA-Z0-9,\. ]{0,80}$'));
		

FOR SQL SERVER, the following is required:


DROP PROCEDURE webgoat_guest.UPDATE_EMPLOYEE
GO

CREATE PROCEDURE webgoat_guest.UPDATE_EMPLOYEE
    @v_userid INT,
    @v_first_name VARCHAR(20),
    @v_last_name VARCHAR(20),
    @v_ssn VARCHAR(12),
    @v_title VARCHAR(20),
    @v_phone VARCHAR(13),
    @v_address1 VARCHAR(80),
    @v_address2 VARCHAR(80),
    @v_manager INT,
    @v_start_date CHAR(8),
    @v_salary INT,
    @v_ccn VARCHAR(30),
    @v_ccn_limit INT,
    @v_disciplined_date CHAR(8),
    @v_disciplined_notes VARCHAR(60),
    @v_personal_description VARCHAR(60)
AS
    IF [webgoat_guest].RegexMatch(@v_address1, N'^[a-zA-Z0-9,\. ]{0,80}$') = 0
        BEGIN
          RAISERROR('Illegal characters in address1', 11, 1)
          RETURN
        END
    UPDATE EMPLOYEE
    SET
        first_name = @v_first_name, 
        last_name = @v_last_name, 
        ssn = @v_ssn, 
        title = @v_title, 
        phone = @v_phone, 
        address1 = @v_address1, 
        address2 = @v_address2, 
        manager = @v_manager, 
        start_date = @v_Start_date,
        salary = @v_salary, 
        ccn = @v_ccn, 
        ccn_limit = @v_ccn_limit, 
        disciplined_date = @v_disciplined_date, 
        disciplined_notes = @v_disciplined_notes, 
        personal_description = @v_personal_description
    WHERE
        userid = @v_userid;
GO

*/

public class UpdateProfile_i extends UpdateProfile
{
	public UpdateProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

}
