EXEC sp_configure 'clr enabled', 1
GO

RECONFIGURE
GO

USE master;

go

DROP LOGIN webgoat_guest;

go

DROP database webgoat;

go


CREATE database webgoat;

go

USE webgoat;

go

CREATE SCHEMA webgoat_guest;

go

CREATE LOGIN webgoat_guest with password = '_webgoat';

go

CREATE USER webgoat_guest with default_schema = webgoat_guest;

go

GRANT CONTROL TO webgoat_guest;

go


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

go

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
	DROP PROCEDURE webgoat_guest.UPDATE_EMPLOYEE_BACKUP
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

go

CREATE PROCEDURE webgoat_guest.UPDATE_EMPLOYEE_BACKUP
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

go

IF EXISTS
(
	SELECT	1
	FROM	INFORMATION_SCHEMA.ROUTINES
	WHERE		ROUTINE_NAME 	= 'EMPLOYEE_LOGIN'
		AND	ROUTINE_SCHEMA	= 'webgoat_guest'
		AND	ROUTINE_TYPE	= 'FUNCTION'
)
BEGIN
	DROP FUNCTION webgoat_guest.EMPLOYEE_LOGIN
	DROP FUNCTION webgoat_guest.EMPLOYEE_LOGIN_BACKUP
END
GO

CREATE FUNCTION webgoat_guest.EMPLOYEE_LOGIN (
    @v_id INT,
    @v_password VARCHAR(100)
) RETURNS INTEGER
AS
    BEGIN
        DECLARE @sql nvarchar(4000), @count int
        SELECT @sql = N'SELECT @cnt = COUNT(*) FROM EMPLOYEE WHERE USERID = ' + convert(varchar(10),@v_id) + N' AND PASSWORD = ''' + @v_password + N'''';
        EXEC sp_executesql @sql, N'@cnt int OUTPUT', @cnt = @count OUTPUT
        return @count
    END
GO

CREATE FUNCTION webgoat_guest.EMPLOYEE_LOGIN_BACKUP (
    @v_id INT,
    @v_password VARCHAR(100)
) RETURNS INTEGER
AS
    BEGIN
        DECLARE @sql nvarchar(4000), @count int
        SELECT @sql = N'SELECT @cnt = COUNT(*) FROM EMPLOYEE WHERE USERID = ' + convert(varchar(10),@v_id) + N' AND PASSWORD = ''' + @v_password + N'''';
        EXEC sp_executesql @sql, N'@cnt int OUTPUT', @cnt = @count OUTPUT
        return @count
    END
GO

IF EXISTS
(
	SELECT	1
	FROM	INFORMATION_SCHEMA.ROUTINES
	WHERE		ROUTINE_NAME 	= 'RegexMatch'
		AND	ROUTINE_SCHEMA	= 'webgoat_guest'
		AND	ROUTINE_TYPE	= 'FUNCTION'
)
BEGIN
	DROP FUNCTION webgoat_guest.RegexMatch
END
GO

IF EXISTS (SELECT name FROM sys.assemblies WHERE name = N'RegexMatch') 
	DROP ASSEMBLY RegexMatch;
GO

CREATE ASSEMBLY RegexMatch FROM 'C:\AspectClass\Database\Labs\tomcat\webapps\WebGoat\WEB-INF\RegexMatch.dll' WITH PERMISSION_SET = SAFE;
GO

CREATE FUNCTION webgoat_guest.RegexMatch (
@input NVARCHAR(MAX),
@pattern NVARCHAR(MAX)
) RETURNS BIT
AS EXTERNAL NAME  RegexMatch.[UserDefinedFunctions].RegexMatch;
GO
