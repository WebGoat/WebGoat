package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Chuck Willis of <a href="http://www.mandiant.com">MANDIANT</a> 
 * @created July 11, 2008
 */
public class MaliciousFileExecution extends LessonAdapter
{

	private final static A MANDIANT_LOGO = new A().setHref("http://www.mandiant.com").addElement(new IMG("images/logos/mandiant.png").setAlt("MANDIANT").setBorder(0).setHspace(0).setVspace(0));
	
	// the UPLOADS_DIRECTORY is where uploads are stored such that they can be references
	// in image tags as "uploads/filename.ext".  This directory string should not contain any path separators (/ or \)
	private String uploads_and_target_parent_directory = null; 
	
	private final static String UPLOADS_RELATIVE_PATH = "uploads";
	
	// this is the target directory that the user must put a file in to pass the lessson.  The file must be named
	// username.txt.  This directory string should not contain any path separators (/ or \)

	private final static String TARGET_RELATIVE_PATH = "mfe_target"; 
	
	// this should probably go in a constructor, but we need the session object...
	// may be able to do something like:
	//    String directory = this.getServletContext().getRealPath("/");
	private void fill_uploads_and_target_parent_directory(WebSession s) {
		//uploads_and_target_parent_directory = s.getWebgoatContext().getServlet().getServletContext().getRealPath("/");
		uploads_and_target_parent_directory = s.getContext().getRealPath("/");
		// make sure it ends with a / or \
		if(!uploads_and_target_parent_directory.endsWith(File.separator)) {
			uploads_and_target_parent_directory = uploads_and_target_parent_directory + 
				File.separator;
		}
		System.out.println("uploads_and_target_parent_directory set to = " 
				+ uploads_and_target_parent_directory);
		
		// make sure the directories exist
		File uploads_dir = new File(uploads_and_target_parent_directory
				+ UPLOADS_RELATIVE_PATH);
		uploads_dir.mkdir();
		
		File target_dir = new File(uploads_and_target_parent_directory 
				+ TARGET_RELATIVE_PATH);
		target_dir.mkdir();
		
		// delete the user's target file if it is already there since we must
			// have restarted webgoat
		File userfile = new File(uploads_and_target_parent_directory 
				+ TARGET_RELATIVE_PATH + java.io.File.separator 
				+ s.getUserName() + ".txt");
		
		userfile.delete();
		
	}
	
    /**
     * Description of the Method
     * 
     * @param s
     *                Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
    	
    if(uploads_and_target_parent_directory == null) {
    	fill_uploads_and_target_parent_directory(s);
    }
    
    
	ElementContainer ec = new ElementContainer();

	try
	{
		
		// check for success - see if the target file exists yet
		
		File userfile = new File(uploads_and_target_parent_directory 
				+ TARGET_RELATIVE_PATH + java.io.File.separator 
				+ s.getUserName() + ".txt");
		
		if(userfile.exists()) {
			makeSuccess(s);
		}
		
	    Connection connection = DatabaseUtilities.getConnection(s);
	    
		ec.addElement(new H1().addElement("WebGoat Image Storage"));
	    
	    // show the current image
	    ec.addElement(new P().addElement("Your current image:"));
	    
	    String image_query = "SELECT image_relative_url FROM mfe_images WHERE user_name = '" 
	    	+ s.getUserName() + "'";

	    Statement image_statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet image_results = image_statement.executeQuery(image_query);
		
		if(image_results.next() == false) {
			// result set was empty
	    	ec.addElement(new P().addElement("No image uploaded"));
	    	System.out.println("No image uploaded");
	    } else {

			String image_url = image_results.getString(1);
	    	
	    	ec.addElement(new IMG(image_url).setBorder(0).setHspace(0).setVspace(0));
	    	
	    	System.out.println("Found image named: " + image_url);

	    }
	    
	    ec.addElement(new P().addElement("Upload a new image:"));

	    Input input = new Input(Input.FILE, "myfile", "");
	    ec.addElement(input);
	    
	    Element b = ECSFactory.makeButton("Start Upload");
	    ec.addElement(b);
	    
	}
	catch (Exception e)
	{
	    s.setMessage("Error generating " + this.getClass().getName());
	    e.printStackTrace();
	}

	return (ec);
    }

    /**
     * Gets the category attribute of the SqlInjection object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
	return Category.MALICIOUS_EXECUTION;
    }

    /**
     * Gets the credits attribute of the AbstractLesson object
     * 
     * @return The credits value
     */
    public Element getCredits()
    {
    	return super.getCustomCredits("Created by Chuck Willis&nbsp;", MANDIANT_LOGO);
    }

    /**
     * Gets the hints attribute of the DatabaseFieldScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        if(uploads_and_target_parent_directory == null) {
        	fill_uploads_and_target_parent_directory(s);
        }
        
        String target_filename = uploads_and_target_parent_directory
			+ TARGET_RELATIVE_PATH 
			+ java.io.File.separator 
			+ s.getUserName() + ".txt";
        
	List<String> hints = new ArrayList<String>();

	    hints.add("Where are uploaded images stored?  Can you browse to them directly?");
	    
	    hints.add("What type of file can you upload to a J2EE server that will be executed when you browse to it?");
	    
	    hints.add("You want to upload a .jsp file that creates an instance of the class java.io.File " +
	    		" and calls the createNewFile() method of that instance.");
	 
	    hints.add("Below are some helpful links..." +
	    "<br><br>Here is a page with an example of a simple .jsp file using a Scriptlet:" +
	    "<br><a href=\"http://www.jsptut.com/Scriptlets.jsp\">" +
	    "http://www.jsptut.com/Scriptlets.jsp</a>" +
	    "<br><br>Here is an page with an example of using createNewFile():" +
	    "<br><a href=\"http://www.roseindia.net/java/example/java/io/CreateFile.shtml\">" +
	    "http://www.roseindia.net/java/example/java/io/CreateFile.shtml</a>" +
	    "<br><br>Here is the API specification for java.io.File:" +
	    "<br><a href=\"http://java.sun.com/j2se/1.5.0/docs/api/java/io/File.html\">" +
	    "http://java.sun.com/j2se/1.5.0/docs/api/java/io/File.html</a>"
	    );
	    
	    hints
    	.add("Here is an example .jsp file, modify it to use java.io.File and its createNewFile() method:"
    		+ "<br><br>&lt;HTML&gt;"
    		+ "<br>&lt;%"
    		+ "<br>java.lang.String hello = new java.lang.String(\"Hello World!\");"
    		+ "<br>System.out.println(hello);"
    		+ "<br>%&gt;"
    		+ "<br>&lt;/HTML&gt;"
    		+ "<br><br>NOTE: executing this file will print \"Hello World!\" to the Tomcat Console, not to your client browser"
		    );
	    
	    
	    hints
	    	.add("SOLUTION:<br><br>Upload a file with a .jsp extension and this content:"
	    		+ "<br><br>&lt;HTML&gt;"
	    		+ "<br>&lt;%"
	    		+ "<br>java.io.File file = new java.io.File(\""
	    		+ target_filename.replaceAll("\\\\", "\\\\\\\\") // if we are on windows, we need to 
	    			// make sure path separators are doubled / escaped
	    		+ "\");"
	    		+ "<br>file.createNewFile();"
	    		+ "<br>%&gt;"
	    		+ "<br>&lt;/HTML&gt;"
	    		+ "<br><br>After you have uploaded your jsp file, you can get the system to execute it by opening it in your browser at the URL below (or by just refreshing this page):" 
    			+ "<br><br>http://webgoat_ip:port/WebGoat/" + UPLOADS_RELATIVE_PATH + "/yourfilename.jsp"
    			);
	    
	return hints;
    }

    // this is a custom method for this lesson to restart.  It is called in WebSession.restartLesson
    // in a currently somewhat "hacked up" manner that is specific to this lesson.  There probably
    // should be an abstract type for lessons that need custom "restarting" code.
    public void restartLesson(WebSession s)
    {
 
        if(uploads_and_target_parent_directory == null) {
        	fill_uploads_and_target_parent_directory(s);
        }
    	
    	System.out.println("Restarting Malicious File Execution lesson for user " + s.getUserName());
    	
    	// delete the user's target file
		File userfile = new File(uploads_and_target_parent_directory 
				+ TARGET_RELATIVE_PATH 
				+ java.io.File.separator 
				+ s.getUserName() + ".txt");
		
		userfile.delete();
		
		// remove the row from the mfe table
        // add url to database table
        
		try {
			Connection connection = DatabaseUtilities.getConnection(s);
			
	        Statement statement = connection.createStatement();
	        
	        String deleteuserrow = "DELETE from mfe_images WHERE user_name = '"
	        	+ s.getUserName() + "';";

	        statement.executeUpdate(deleteuserrow);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
   
    // cleanup code has been disabled for now.  I'm not sure where it can be called cleanly
    // where it will know what directory to use since that is pulled from the session object
    
    // this method will delete files in the target directory and the uploads directory
    // it should be called when WebGoat starts
//    public static void cleanDirectories() {
//    	// delete files in TARGET_DIRECTORY
//    	File target_dir = new File(TARGET_RELATIVE_PATH);
//    	deleteFilesInDir(target_dir);
//    	
//    	// delete files in uploads directory
//    	File uploads_dir = new File(uploads_and_target_parent_directory + UPLOADS_RELATIVE_PATH);
//    	deleteFilesInDir(uploads_dir);
//    	
//    }
    
//    private static void deleteFilesInDir(File dir) {
//       	File[] dir_files = dir.listFiles();
//    	for(int i = 0; i < dir_files.length; i++) {
//    		// we won't recurse and we don't want to delete every file just in 
//    		// case TARGET_DIRECTORY or uploads directory is pointed
//			// somewhere stupid, like c:\ or /
//    		if(dir_files[i].isFile()) { 
//    			String lower_file_name = dir_files[i].getName().toLowerCase(); 
//    			
//    			if(lower_file_name.endsWith(".jpg") ||
//					lower_file_name.endsWith(".gif") ||
//					lower_file_name.endsWith(".png") ||
//					lower_file_name.endsWith(".jsp") ||
//					lower_file_name.endsWith(".txt") ||
//					lower_file_name.endsWith(".asp") || // in case they think this is a IIS server :-)
//					lower_file_name.endsWith(".aspx")) {
//    					dir_files[i].delete();
//    			}
//    		}
//    	}	
//    }
   
    
    /**
     * Gets the instructions attribute of the object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        if(uploads_and_target_parent_directory == null) {
        	fill_uploads_and_target_parent_directory(s);
        }
    	
	String instructions = "The form below allows you to upload an image which will be displayed on this page.  " 
		+ "Features like this are often found on web based discussion boards and social networking sites.  " 
		+ "This feature is vulnerable to Malicious File Execution."
		+ "<br><br>In order to pass this lession, upload and run a malicious file.  In order to prove that your file can execute,"
		+ " it should create another file named:<br><br> "
		+ uploads_and_target_parent_directory
		+ TARGET_RELATIVE_PATH 
		+ java.io.File.separator 
		+ s.getUserName() + ".txt" 
		+ "<br><br>Once you have created this file, you will pass the lesson.";

	return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(75);

    protected Integer getDefaultRanking()
    {
	return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the DatabaseFieldScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
	return ("Malicious File Execution");
    }

    /**
     * Constructor for the DatabaseFieldScreen object
     * 
     * @param s
     *                Description of the Parameter
     */
    public void handleRequest(WebSession s)
    {
    	
        if(uploads_and_target_parent_directory == null) {
        	fill_uploads_and_target_parent_directory(s);
        }
        
        
    		
	try
	{
		if(ServletFileUpload.isMultipartContent(s.getRequest())) {
			// multipart request - we have the file upload
			
//			 Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(500000); // files over 500k will be written to disk temporarily.  
			// files under that size will be stored in memory until written to disk by the request handler code below
			
//			 Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			
//			 Parse the request
			List /* FileItem */ items = upload.parseRequest(s.getRequest());
			
//			 Process the uploaded items
			java.util.Iterator iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();

			    if (item.isFormField()) {
			        
			    	// ignore regular form fields
			    	
			    } else {
			        
			    	// not a form field, must be a file upload
			    	if(item.getName().contains("/") || item.getName().contains("\\")) {
			    		System.out.println("Uploaded file contains a / or \\ (i.e. attempted directory traversal).  Not storing file.");
			    		// TODO - is there a way to show an error to the user here?
			    		
			    		s.setMessage("Directory traversal not allowed.  Nice try though.");
						
			    	} else {
			    	
				    	// write file to disk with original name in uploads directory
			    		String uploaded_file_path = uploads_and_target_parent_directory 
			    			+ UPLOADS_RELATIVE_PATH 
			    			+ java.io.File.separator
			    			+ item.getName();
				    	File uploadedFile = new File(uploaded_file_path);
				        item.write(uploadedFile);
				        System.out.println("Stored file:\n" + uploaded_file_path );
				        
				        // add url to database table
				        Connection connection = DatabaseUtilities.getConnection(s);
				        
				        Statement statement = connection.createStatement();
				        
				        // attempt an update
				        String updateData1 = "UPDATE mfe_images SET image_relative_url='" + UPLOADS_RELATIVE_PATH + "/"
				        	+ item.getName() + "' WHERE user_name = '"
				        	+ s.getUserName() + "';";
			    		
				        System.out.println("Updating row:\n" + updateData1 );
				    	if(statement.executeUpdate(updateData1) == 0) {
				        
				    		// update failed, we need to add a row
					        String insertData1 = "INSERT INTO mfe_images VALUES ('" +
					    		s.getUserName() + "','" + UPLOADS_RELATIVE_PATH + "/" + 
					    		item.getName() + "')";
					        
					        System.out.println("Inserting row:\n" + insertData1 );
					    	statement.executeUpdate(insertData1);
					    	
				    	}
			    	}
			    	
			    }
			}
			
		} 
			// now handle normally (if it was a multipart request or now)
			
			//super.handleRequest(s);
			
			// needed to cut and paste and edit rather than calling super 
			// here so that we could set the encoding type to multipart form data
			// call createContent first so messages will go somewhere

			Form form = new Form(getFormAction(), Form.POST).setName("form")
				.setEncType("multipart/form-data");

			form.addElement(createContent(s));

			setContent(form);
	}
	catch (Exception e)
	{
	    System.out.println("Exception caught: " + e);
	    e.printStackTrace(System.out);
	}
    }
}
