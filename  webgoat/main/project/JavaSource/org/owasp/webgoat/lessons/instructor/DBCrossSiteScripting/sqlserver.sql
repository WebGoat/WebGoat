

IF EXISTS
(
	SELECT	1
	FROM	INFORMATION_SCHEMA.ROUTINES
	WHERE		ROUTINE_NAME 	= 'UPDATE_EMPLOYEE'
		AND	ROUTINE_SCHEMA	= 'webgoat_guest'
		AND	ROUTINE_TYPE	= 'PROCEDURE'
)
BEGIN
	DROP PROCEDURE webgoat_guest.UPDATE_EMPLOYEE
END
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