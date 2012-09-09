
package org.owasp.webgoat.session;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.owasp.webgoat.lessons.AbstractLesson;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 */
public class CreateDB
{

	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	private void createMessageTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Drop admin user table
		try
		{
			String dropTable = "DROP TABLE messages";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop message database");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE messages (" + "num int not null," + "title varchar(50),"
					+ "message varchar(200)," + "user_name varchar(50) not null, " + "lesson_type varchar(50) not null"
					+ ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating message database " + e.getLocalizedMessage());
		}
	}

    /**
     * Description of the Method
     *
     * @param connection Description of the Parameter
     *
     * @exception SQLException Description of the Exception
     */
    private void createMFEImagesTable(Connection connection) throws SQLException
    {
		Statement statement = connection.createStatement();
	
		// Drop mfe_images table
		try
		{
		    String dropTable = "DROP TABLE mfe_images";
		    statement.executeUpdate(dropTable);
		}
		catch (SQLException e)
		{
		    System.out.println("Info - Could not drop mfe_images table from database");
		}
	
		// Create the new mfe_images table
		try
		{
		    String createTableStatement = "CREATE TABLE mfe_images ("
			    + "user_name varchar(50) not null, "
			    + "image_relative_url varchar(50) not null"
			    + ")";
		    statement.executeUpdate(createTableStatement);
		}
		catch (SQLException e)
		{
		    System.out.println("Error creating mfe_images table in database " + e.getLocalizedMessage());
		}

    }
	
	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	private void createProductTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Drop admin user table
		try
		{
			String dropTable = "DROP TABLE product_system_data";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop product table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE product_system_data ("
					+ "productid varchar(6) not null primary key," + "product_name varchar(20)," + "price varchar(10)"
					+ ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating product table " + e.getLocalizedMessage());
		}

		// Populate
		String insertData1 = "INSERT INTO product_system_data VALUES ('32226','Dog Bone','$1.99')";
		String insertData2 = "INSERT INTO product_system_data VALUES ('35632','DVD Player','$214.99')";
		String insertData3 = "INSERT INTO product_system_data VALUES ('24569','60 GB Hard Drive','$149.99')";
		String insertData4 = "INSERT INTO product_system_data VALUES ('56970','80 GB Hard Drive','$179.99')";
		String insertData5 = "INSERT INTO product_system_data VALUES ('14365','56 inch HDTV','$6999.99')";
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
	}

	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	private void createUserAdminTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Drop admin user table
		try
		{
			String dropTable = "DROP TABLE user_system_data";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop user admin table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE user_system_data (" + "userid varchar(5) not null primary key,"
					+ "user_name varchar(12)," + "password varchar(10)," + "cookie varchar(30)" + ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating user admin table " + e.getLocalizedMessage());
		}

		// Populate
		String insertData1 = "INSERT INTO user_system_data VALUES ('101','jsnow','passwd1', '')";
		String insertData2 = "INSERT INTO user_system_data VALUES ('102','jdoe','passwd2', '')";
		String insertData3 = "INSERT INTO user_system_data VALUES ('103','jplane','passwd3', '')";
		String insertData4 = "INSERT INTO user_system_data VALUES ('104','jeff','jeff', '')";
		String insertData5 = "INSERT INTO user_system_data VALUES ('105','dave','dave', '')";
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
	}

	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	private void createUserDataTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Delete table if there is one
		try
		{
			String dropTable = "DROP TABLE user_data";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop user table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE user_data (" + "userid int not null,"
					+ "first_name varchar(20)," + "last_name varchar(20)," + "cc_number varchar(30),"
					+ "cc_type varchar(10)," + "cookie varchar(20)," + "login_count int" + ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating user table " + e.getLocalizedMessage());
		}

		// Populate it
		String insertData1 = "INSERT INTO user_data VALUES (101,'Joe','Snow','987654321','VISA',' ',0)";
		String insertData2 = "INSERT INTO user_data VALUES (101,'Joe','Snow','2234200065411','MC',' ',0)";
		String insertData3 = "INSERT INTO user_data VALUES (102,'John','Smith','2435600002222','MC',' ',0)";
		String insertData4 = "INSERT INTO user_data VALUES (102,'John','Smith','4352209902222','AMEX',' ',0)";
		String insertData5 = "INSERT INTO user_data VALUES (103,'Jane','Plane','123456789','MC',' ',0)";
		String insertData6 = "INSERT INTO user_data VALUES (103,'Jane','Plane','333498703333','AMEX',' ',0)";
		String insertData7 = "INSERT INTO user_data VALUES (10312,'Jolly','Hershey','176896789','MC',' ',0)";
		String insertData8 = "INSERT INTO user_data VALUES (10312,'Jolly','Hershey','333300003333','AMEX',' ',0)";
		String insertData9 = "INSERT INTO user_data VALUES (10323,'Grumpy','youaretheweakestlink','673834489','MC',' ',0)";
		String insertData10 = "INSERT INTO user_data VALUES (10323,'Grumpy','youaretheweakestlink','33413003333','AMEX',' ',0)";
		String insertData11 = "INSERT INTO user_data VALUES (15603,'Peter','Sand','123609789','MC',' ',0)";
		String insertData12 = "INSERT INTO user_data VALUES (15603,'Peter','Sand','338893453333','AMEX',' ',0)";
		String insertData13 = "INSERT INTO user_data VALUES (15613,'Joesph','Something','33843453533','AMEX',' ',0)";
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
		statement.executeUpdate(insertData7);
		statement.executeUpdate(insertData8);
		statement.executeUpdate(insertData9);
		statement.executeUpdate(insertData10);
		statement.executeUpdate(insertData11);
		statement.executeUpdate(insertData12);
		statement.executeUpdate(insertData13);

	}

	private void createLoginTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Delete table if there is one
		try
		{
			String dropTable = "DROP TABLE user_login";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop user_login table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE user_login (" + "userid varchar(5),"
					+ "webgoat_user varchar(20)" + ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating user_login table " + e.getLocalizedMessage());
		}

	}

	// creates the table pins which is used in the blind sql injection lesson
	private void createBlindSQLLessonTable(Connection connection) throws SQLException
    {
		Statement statement = connection.createStatement();
	
		// Delete table if there is one
		try
		{
		    String dropTable = "DROP TABLE pins";
		    statement.executeUpdate(dropTable);
		}
		catch (SQLException e)
		{
		    System.out.println("Info - Could not drop pins table");
		}
	
		// Create the new table
		try
		{
		    String createTableStatement = "CREATE TABLE pins ("
			    + "cc_number varchar(30),"
			    + "pin int," 
			    + "name varchar(20)" 
			    + ")";
		    statement.executeUpdate(createTableStatement);
		}
		catch (SQLException e)
		{
		    System.out.println("Error creating pins table " + e.getLocalizedMessage());
		}
	
		// Populate it
		String insertData1 = "INSERT INTO pins VALUES ('987654321098765', 1234, 'Joe')";
		String insertData2 = "INSERT INTO pins VALUES ('1234567890123456', 4567, 'Jack')";
		String insertData3 = "INSERT INTO pins VALUES ('4321432143214321', 4321, 'Jill')";
		String insertData4 = "INSERT INTO pins VALUES ('1111111111111111', 7777, 'Jim')";
		String insertData5 = "INSERT INTO pins VALUES ('1111222233334444', 2364, 'John')";
		
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
	
    }
	
	// creates the table salaries which is used in the lessons 
	// which add or modify data using sql injection 
	private void createModifyWithSQLLessonTable(Connection connection) throws SQLException
    {
		Statement statement = connection.createStatement();
	
		// Delete table if there is one
		try
		{
		    String dropTable = "DROP TABLE salaries";
		    statement.executeUpdate(dropTable);
		}
		catch (SQLException e)
		{
		    System.out.println("Info - Could not drop salaries table");
		}
	
		// Create the new table
		try
		{
		    String createTableStatement = "CREATE TABLE salaries ("
			    + "userid varchar(50),"
			    + "salary int" 
			    + ")";
		    statement.executeUpdate(createTableStatement);
		}
		catch (SQLException e)
		{
		    System.out.println("Error creating salaries table " + e.getLocalizedMessage());
		}
	
		// Populate it
		String insertData1 = "INSERT INTO salaries VALUES ('jsmith', 20000)";
		String insertData2 = "INSERT INTO salaries VALUES ('lsmith', 45000)";
		String insertData3 = "INSERT INTO salaries VALUES ('wgoat', 100000)";
		String insertData4 = "INSERT INTO salaries VALUES ('rjones', 777777)";
		String insertData5 = "INSERT INTO salaries VALUES ('manderson', 65000)";
		
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
	
    }
	
	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	private void createWeatherDataTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Delete table if there is one
		try
		{
			String dropTable = "DROP TABLE weather_data";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop weather table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE weather_data (" + "station int not null,"
					+ "name varchar(20) not null," + "state char(2) not null," + "min_temp int not null,"
					+ "max_temp int not null" + ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating weather table " + e.getLocalizedMessage());
		}

		// Populate it
		String insertData1 = "INSERT INTO weather_data VALUES (101,'Columbia','MD',-10,102)";
		String insertData2 = "INSERT INTO weather_data VALUES (102,'Seattle','WA',-15,90)";
		String insertData3 = "INSERT INTO weather_data VALUES (103,'New York','NY',-10,110)";
		String insertData4 = "INSERT INTO weather_data VALUES (104,'Houston','TX',20,120)";
		String insertData5 = "INSERT INTO weather_data VALUES (10001,'Camp David','MD',-10,100)";
		String insertData6 = "INSERT INTO weather_data VALUES (11001,'Ice Station Zebra','NA',-60,30)";
		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
	}

	/**
	 * Create users with tans
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private void createTanUserDataTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Delete table if there is one
		try
		{
			String dropTable = "DROP TABLE user_data_tan";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop user_data_tan table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE user_data_tan (" + "userid int not null,"
					+ "first_name varchar(20)," + "last_name varchar(20)," + "cc_number varchar(30),"
					+ "cc_type varchar(10)," + "cookie varchar(20)," + "login_count int," + "password varchar(20)"
					+ ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating user_data_tan table " + e.getLocalizedMessage());
		}

		// Populate it
		String insertData1 = "INSERT INTO user_data_tan VALUES (101,'Joe','Snow','987654321','VISA',' ',0, 'banana')";
		String insertData2 = "INSERT INTO user_data_tan VALUES (102,'Jane','Plane','74589864','MC',' ',0, 'tarzan')";
		String insertData3 = "INSERT INTO user_data_tan VALUES (103,'Jack','Sparrow','68659365','MC',' ',0, 'sniffy')";

		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
	}

	/**
	 * Create the Table for the tans
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	private void createTanTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		// Delete table if there is one
		try
		{
			String dropTable = "DROP TABLE tan";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop tan table");
		}

		// Create the new table
		try
		{
			String createTableStatement = "CREATE TABLE tan (" + "userid int not null," + "tanNr int," + "tanValue int"
					+ ")";
			statement.executeUpdate(createTableStatement);
		} catch (SQLException e)
		{
			System.out.println("Error creating tan table " + e.getLocalizedMessage());
		}

		// Populate it
		String insertData1 = "INSERT INTO tan VALUES (101,1,15161)";
		String insertData2 = "INSERT INTO tan VALUES (101,2,4894)";
		String insertData3 = "INSERT INTO tan VALUES (101,3,18794)";
		String insertData4 = "INSERT INTO tan VALUES (101,4,1564)";
		String insertData5 = "INSERT INTO tan VALUES (101,5,45751)";

		String insertData6 = "INSERT INTO tan VALUES (102,1,15648)";
		String insertData7 = "INSERT INTO tan VALUES (102,2,92156)";
		String insertData8 = "INSERT INTO tan VALUES (102,3,4879)";
		String insertData9 = "INSERT INTO tan VALUES (102,4,9458)";
		String insertData10 = "INSERT INTO tan VALUES (102,5,4879)";

		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
		statement.executeUpdate(insertData7);
		statement.executeUpdate(insertData8);
		statement.executeUpdate(insertData9);
		statement.executeUpdate(insertData10);

	}

	// --------------------------------------------------------------------------
	// --------------------------------------------------------------------------
	//
	// The tables below are for WebGoat Financials
	//
	// DO NOT MODIFY THESE TABLES - unless you change the org chart
	// and access control matrix documents
	//
	// --------------------------------------------------------------------------
	// --------------------------------------------------------------------------

	private void createEmployeeTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		try
		{
			String dropTable = "DROP TABLE employee";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop employee table");
		}

		// Create Table
		try
		{
			String createTable = "CREATE TABLE employee ("
					// + "userid INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
					+ "userid INT NOT NULL PRIMARY KEY," + "first_name VARCHAR(20)," + "last_name VARCHAR(20),"
					+ "ssn VARCHAR(12)," + "password VARCHAR(10)," + "title VARCHAR(20)," + "phone VARCHAR(13),"
					+ "address1 VARCHAR(80)," + "address2 VARCHAR(80)," + "manager INT," + "start_date CHAR(8),"
					+ "salary INT," + "ccn VARCHAR(30)," + "ccn_limit INT," + "email VARCHAR(30)," // reason
					// for the recent write-up
					+ "disciplined_date CHAR(8)," // date of write up, NA otherwise
					+ "disciplined_notes VARCHAR(60)," // reason for the recent write-up
					+ "personal_description VARCHAR(60)" // We can be rude here
					// + ",CONSTRAINT fl UNIQUE NONCLUSTERED (first_name, last_name)"
					+ ")";

			statement.executeUpdate(createTable);
		} catch (SQLException e)
		{
			System.out.println("Error: unable to create employee table " + e.getLocalizedMessage());
		}

		String insertData1 = "INSERT INTO employee VALUES (101, 'Larry', 'Stooge', '386-09-5451', 'larry',"
				+ "'Technician','443-689-0192','9175 Guilford Rd','New York, NY', 102, 01012000,55000,'2578546969853547',"
				+ "5000,'larry@stooges.com',010106,'Constantly harassing coworkers','Does not work well with others')";

		String insertData2 = "INSERT INTO employee VALUES (102, 'Moe', 'Stooge', '936-18-4524','moe',"
				+ "'CSO','443-938-5301', '3013 AMD Ave', 'New York, NY', 112, 03082003, 140000, 'NA', 0, 'moe@stooges.com', 0101013, "
				+ "'Hit Curly over head', 'Very dominating over Larry and Curly')";

		String insertData3 = "INSERT INTO employee VALUES (103, 'Curly', 'Stooge', '961-08-0047','curly',"
				+ "'Technician','410-667-6654', '1112 Crusoe Lane', 'New York, NY', 102, 02122001, 50000, 'NA', 0, 'curly@stooges.com', 0101014, "
				+ "'Hit Moe back', 'Owes three-thousand to company for fradulent purchases')";

		String insertData4 = "INSERT INTO employee VALUES (104, 'Eric', 'Walker', '445-66-5565','eric',"
				+ "'Engineer','410-887-1193', '1160 Prescott Rd', 'New York, NY', 107, 12152005, 13000, 'NA', 0, 'eric@modelsrus.com',0101013, "
				+ "'Bothering Larry about webgoat problems', 'Late. Always needs help. Too intern-ish.')";

		String insertData5 = "INSERT INTO employee VALUES (105, 'Tom', 'Cat', '792-14-6364','tom',"
				+ "'Engineer','443-599-0762', '2211 HyperThread Rd.', 'New York, NY', 106, 01011999, 80000, '5481360857968521', 30000, 'tom@wb.com', 0, "
				+ "'NA', 'Co-Owner.')";

		String insertData6 = "INSERT INTO employee VALUES (106, 'Jerry', 'Mouse', '858-55-4452','jerry',"
				+ "'Human Resources','443-699-3366', '3011 Unix Drive', 'New York, NY', 102, 01011999, 70000, '6981754825013564', 20000, 'jerry@wb.com', 0, "
				+ "'NA', 'Co-Owner.')";

		String insertData7 = "INSERT INTO employee VALUES (107, 'David', 'Giambi', '439-20-9405','david',"
				+ "'Human Resources','610-521-8413', '5132 DIMM Avenue', 'New York, NY', 102, 05011999, 100000, '6981754825018101', 10000, 'david@modelsrus.com', 061402, "
				+ "'Hacked into accounting server. Modified personal pay.', 'Strong work habbit. Questionable ethics.')";

		String insertData8 = "INSERT INTO employee VALUES (108, 'Bruce', 'McGuirre', '707-95-9482','bruce',"
				+ "'Engineer','610-282-1103', '8899 FreeBSD Drive<script>alert(document.cookie)</script> ', 'New York, NY', 107, 03012000, 110000, '6981754825854136', 30000, 'bruce@modelsrus.com', 061502, "
				+ "'Tortuous Boot Camp workout at 5am. Employees felt sick.', 'Enjoys watching others struggle in exercises.')";

		String insertData9 = "INSERT INTO employee VALUES (109, 'Sean', 'Livingston', '136-55-1046','sean',"
				+ "'Engineer','610-878-9549', '6422 dFlyBSD Road', 'New York, NY', 107, 06012003, 130000, '6981754825014510', 5000, 'sean@modelsrus.com', 072804, "
				+ "'Late to work 30 days in row due to excessive Halo 2', 'Has some fascination with Steelers. Go Ravens.')";

		String insertData10 = "INSERT INTO employee VALUES (110, 'Joanne', 'McDougal', '789-54-2413','joanne',"
				+ "'Human Resources','610-213-6341', '5567 Broadband Lane', 'New York, NY', 106, 01012001, 90000, '6981754825081054', 300, 'joanne@modelsrus.com', 112005, "
				+ "'Used company cc to purchase new car. Limit adjusted.', 'Finds it necessary to leave early every day.')";

		String insertData11 = "INSERT INTO employee VALUES (111, 'John', 'Wayne', '129-69-4572', 'john',"
				+ "'CTO','610-213-1134', '129 Third St', 'New York, NY', 112, 01012001, 200000, '4437334565679921', 300, 'john@guns.com', 112005, "
				+ "'', '')";
		String insertData12 = "INSERT INTO employee VALUES (112, 'Neville', 'Bartholomew', '111-111-1111', 'socks',"
				+ "'CEO','408-587-0024', '1 Corporate Headquarters', 'San Jose, CA', 112, 03012000, 450000, '4803389267684109', 300000, 'neville@modelsrus.com', 112005, "
				+ "'', '')";

		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
		statement.executeUpdate(insertData7);
		statement.executeUpdate(insertData8);
		statement.executeUpdate(insertData9);
		statement.executeUpdate(insertData10);
		statement.executeUpdate(insertData11);
		statement.executeUpdate(insertData12);

	}

	private void createRolesTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		try
		{
			String dropTable = "DROP TABLE roles";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop roles table");
		}

		try
		{
			String createTable = "CREATE TABLE roles (" + "userid INT NOT NULL," + "role VARCHAR(10) NOT NULL,"
					+ "PRIMARY KEY (userid, role)" + ")";

			statement.executeUpdate(createTable);
		} catch (SQLException e)
		{
			System.out.println("Error: Unable to create role table: " + e.getLocalizedMessage());
		}

		String insertData1 = "INSERT INTO roles VALUES (101, 'employee')";
		String insertData2 = "INSERT INTO roles VALUES (102, 'manager')";
		String insertData3 = "INSERT INTO roles VALUES (103, 'employee')";
		String insertData4 = "INSERT INTO roles VALUES (104, 'employee')";
		String insertData5 = "INSERT INTO roles VALUES (105, 'employee')";
		String insertData6 = "INSERT INTO roles VALUES (106, 'hr')";
		String insertData7 = "INSERT INTO roles VALUES (107, 'manager')";
		String insertData8 = "INSERT INTO roles VALUES (108, 'employee')";
		String insertData9 = "INSERT INTO roles VALUES (109, 'employee')";
		String insertData10 = "INSERT INTO roles VALUES (110, 'hr')";
		String insertData11 = "INSERT INTO roles VALUES (111, 'admin')";
		String insertData12 = "INSERT INTO roles VALUES (112, 'admin')";

		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
		statement.executeUpdate(insertData7);
		statement.executeUpdate(insertData8);
		statement.executeUpdate(insertData9);
		statement.executeUpdate(insertData10);
		statement.executeUpdate(insertData11);
		statement.executeUpdate(insertData12);
	}

	private void createAuthTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		try
		{
			String dropTable = "DROP TABLE auth";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop auth table");
		}

		try
		{
			String createTable = "CREATE TABLE auth (" + "role VARCHAR(10) NOT NULL,"
					+ "functionid VARCHAR(20) NOT NULL," + "PRIMARY KEY (role, functionid)" + ")";

			statement.executeUpdate(createTable);
		} catch (SQLException e)
		{
			System.out.println("Error: unable to create auth table: " + e.getLocalizedMessage());
		}

		String insertData1 = "INSERT INTO auth VALUES('employee', 'Logout')";
		String insertData2 = "INSERT INTO auth VALUES('employee', 'ListStaff')";
		String insertData3 = "INSERT INTO auth VALUES('employee', 'ViewProfile')";
		String insertData4 = "INSERT INTO auth VALUES('employee', 'EditProfile')";
		String insertData4_1 = "INSERT INTO auth VALUES('employee', 'SearchStaff')";
		String insertData4_2 = "INSERT INTO auth VALUES('employee', 'FindProfile')";
		String insertData5 = "INSERT INTO auth VALUES('manager', 'Logout')";
		String insertData6 = "INSERT INTO auth VALUES('manager', 'ListStaff')";
		String insertData7 = "INSERT INTO auth VALUES('manager', 'ViewProfile')";
		String insertData7_1 = "INSERT INTO auth VALUES('manager', 'SearchStaff')";
		String insertData7_2 = "INSERT INTO auth VALUES('manager', 'FindProfile')";
		// String insertData8 = "INSERT INTO auth VALUES('manager', 'EditProfile')";
		// String insertData9 = "INSERT INTO auth VALUES('manager', 'CreateProfile')";
		// String insertData10 = "INSERT INTO auth VALUES('manager', 'DeleteProfile')";
		// String insertData11 = "INSERT INTO auth VALUES('manager', 'UpdateProfile')";
		String insertData12 = "INSERT INTO auth VALUES('hr', 'Logout')";
		String insertData13 = "INSERT INTO auth VALUES('hr', 'ListStaff')";
		String insertData14 = "INSERT INTO auth VALUES('hr', 'ViewProfile')";
		String insertData15 = "INSERT INTO auth VALUES('hr', 'EditProfile')";
		String insertData16 = "INSERT INTO auth VALUES('hr', 'CreateProfile')";
		String insertData17 = "INSERT INTO auth VALUES('hr', 'DeleteProfile')";
		String insertData18 = "INSERT INTO auth VALUES('hr', 'UpdateProfile')";
		String insertData18_1 = "INSERT INTO auth VALUES('hr', 'SearchStaff')";
		String insertData18_2 = "INSERT INTO auth VALUES('hr', 'FindProfile')";
		String insertData19 = "INSERT INTO auth VALUES('admin', 'Logout')";
		String insertData20 = "INSERT INTO auth VALUES('admin', 'ListStaff')";
		String insertData21 = "INSERT INTO auth VALUES('admin', 'ViewProfile')";
		String insertData22 = "INSERT INTO auth VALUES('admin', 'EditProfile')";
		String insertData23 = "INSERT INTO auth VALUES('admin', 'CreateProfile')";
		String insertData24 = "INSERT INTO auth VALUES('admin', 'DeleteProfile')";
		String insertData25 = "INSERT INTO auth VALUES('admin', 'UpdateProfile')";
		String insertData25_1 = "INSERT INTO auth VALUES('admin', 'SearchStaff')";
		String insertData25_2 = "INSERT INTO auth VALUES('admin', 'FindProfile')";

		// Add a permission for the webgoat role to see the source.
		// The challenge(s) will change the default role to "challenge"
		String insertData26 = "INSERT INTO auth VALUES('" + AbstractLesson.USER_ROLE + "','" + WebSession.SHOWSOURCE
				+ "')";
		String insertData27 = "INSERT INTO auth VALUES('" + AbstractLesson.USER_ROLE + "','" + WebSession.SHOWHINTS
				+ "')";
		// Add a permission for the webgoat role to see the solution.
		// The challenge(s) will change the default role to "challenge"
		String insertData28 = "INSERT INTO auth VALUES('" + AbstractLesson.USER_ROLE + "','" + WebSession.SHOWSOLUTION
				+ "')";

		statement.executeUpdate(insertData1);
		statement.executeUpdate(insertData2);
		statement.executeUpdate(insertData3);
		statement.executeUpdate(insertData4);
		statement.executeUpdate(insertData4_1);
		statement.executeUpdate(insertData4_2);
		statement.executeUpdate(insertData5);
		statement.executeUpdate(insertData6);
		statement.executeUpdate(insertData7);
		statement.executeUpdate(insertData7_1);
		statement.executeUpdate(insertData7_2);
		// statement.executeUpdate(insertData8);
		// statement.executeUpdate(insertData9);
		// statement.executeUpdate(insertData10);
		// statement.executeUpdate(insertData11);
		statement.executeUpdate(insertData12);
		statement.executeUpdate(insertData13);
		statement.executeUpdate(insertData14);
		statement.executeUpdate(insertData15);
		statement.executeUpdate(insertData16);
		statement.executeUpdate(insertData17);
		statement.executeUpdate(insertData18);
		statement.executeUpdate(insertData18_1);
		statement.executeUpdate(insertData18_2);
		statement.executeUpdate(insertData19);
		statement.executeUpdate(insertData20);
		statement.executeUpdate(insertData21);
		statement.executeUpdate(insertData22);
		statement.executeUpdate(insertData23);
		statement.executeUpdate(insertData24);
		statement.executeUpdate(insertData25);
		statement.executeUpdate(insertData25_1);
		statement.executeUpdate(insertData25_2);
		statement.executeUpdate(insertData26);
		statement.executeUpdate(insertData27);
		statement.executeUpdate(insertData28);
	}

	private void createOwnershipTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		try
		{
			String dropTable = "DROP TABLE ownership";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop ownership table");
		}

		try
		{
			String createTable = "CREATE TABLE ownership (" + "employer_id INT NOT NULL," + "employee_id INT NOT NULL,"
					+ "PRIMARY KEY (employee_id, employer_id)" + ")";

			statement.executeUpdate(createTable);
		} catch (SQLException e)
		{
			System.out.println("Error: unable to create ownership table: " + e.getLocalizedMessage());
		}

		String inputData = "INSERT INTO ownership VALUES (112, 101)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 102)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 103)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 104)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 105)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 106)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 107)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 108)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 109)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 110)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 111)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (112, 112)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (102, 101)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 102)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 103)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 104)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 105)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 106)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 107)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 108)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 109)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 110)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (102, 111)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (111, 101)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 102)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 103)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 104)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 105)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 106)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 107)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 108)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 109)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 110)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (111, 111)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (106, 105)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (106, 106)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (106, 110)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (101, 101)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (103, 103)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (107, 104)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (107, 108)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (107, 109)";
		statement.executeUpdate(inputData);
		inputData = "INSERT INTO ownership VALUES (107, 107)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (105, 105)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (110, 110)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (104, 104)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (108, 108)";
		statement.executeUpdate(inputData);

		inputData = "INSERT INTO ownership VALUES (109, 109)";
		statement.executeUpdate(inputData);

	}

	// --------------------------------------------------------------------------
	//
	// End of WebGoat Financials
	//
	// --------------------------------------------------------------------------

	/**
	 * Start creation of data for WebServices labs
	 */

	private void createTransactionTable(Connection connection) throws SQLException
	{
		Statement statement = connection.createStatement();

		try
		{
			String dropTable = "DROP TABLE transactions";
			statement.executeUpdate(dropTable);
		} catch (SQLException e)
		{
			System.out.println("Info - Could not drop transactions table");
		}

		try
		{
			String createTable = "CREATE TABLE Transactions (" + "userName VARCHAR(16) NOT NULL, "
					+ "sequence INTEGER NOT NULL, " + "from_account VARCHAR(16) NOT NULL, "
					+ "to_account VARCHAR(16) NOT NULL, " + "transactionDate TIMESTAMP NOT NULL, "
					+ "description VARCHAR(255) NOT NULL, " + "amount INTEGER NOT NULL" + ")";

			statement.executeUpdate(createTable);
		} catch (SQLException e)
		{
			System.out.println("Error: unable to create transactions table: " + e.getLocalizedMessage());
			throw e;
		}

		String[] data = new String[] {
				"'dave', 0, '238-4723-4024', '324-7635-9867', '2008-02-06 21:40:00', 'Mortgage', '150'",
				"'dave', 1, '238-4723-4024', '324-7635-9867', '2008-02-12 21:41:00', 'Car', '150'",
				"'dave', 2, '238-4723-4024', '324-7635-9867', '2008-02-20 21:42:00', 'School fees', '150'",
				"'CEO', 3, '348-6324-9872', '345-3490-8345', '2008-02-15 21:40:00', 'Rolls Royce', '-150000'",
				"'CEO', 4, '348-6324-9872', '342-5893-4503', '2008-02-25 21:41:00', 'Mansion', '-150000'",
				"'CEO', 5, '348-6324-9872', '980-2344-5492', '2008-02-27 21:42:00', 'Vacation', '-150000'",
				"'jeff', 6, '934-2002-3485', '783-2409-8234', '2008-02-01 21:40:00', 'Vet', '250'",
				"'jeff', 7, '934-2002-3485', '634-5879-0345', '2008-02-19 21:41:00', 'Doctor', '800'",
				"'jeff', 8, '934-2002-3485', '435-4325-3358', '2008-02-20 21:42:00', 'X-rays', '200'", };
		try
		{
			for (int i = 0; i < data.length; i++)
			{
				statement.executeUpdate("INSERT INTO Transactions VALUES (" + data[i] + ");");
			}
		} catch (SQLException sqle)
		{
			System.out.println("Error: Unable to insert transactions:  " + sqle.getLocalizedMessage());
			int errorCode = sqle.getErrorCode();
			System.out.println("Error Code: " + errorCode);
			// ignore exceptions for Oracle and SQL Server
			if (errorCode != 911 && errorCode != 273) { throw sqle; }
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param connection
	 *            Description of the Parameter
	 * 
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public void makeDB(Connection connection) throws SQLException
	{
		System.out.println("Successful connection to database");
		createUserDataTable(connection);
		createLoginTable(connection);
		createBlindSQLLessonTable(connection);
		createUserAdminTable(connection);
		createProductTable(connection);
		createMessageTable(connection);
		createEmployeeTable(connection);
		createRolesTable(connection);
		createAuthTable(connection);
		createOwnershipTable(connection);
		createWeatherDataTable(connection);
		createTransactionTable(connection);
		createTanUserDataTable(connection);
		createTanTable(connection);
		createMFEImagesTable(connection);
		createModifyWithSQLLessonTable(connection);
		System.out.println("Success: creating tables.");
	}
}
