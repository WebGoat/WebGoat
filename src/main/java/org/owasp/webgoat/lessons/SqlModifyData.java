
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Chuck Willis <a href="http://www.securityfoundry.com">Chuck's web
 *         site</a> (this lesson is based on the String SQL Injection lesson)
 * @created October 29, 2009
 */
public class SqlModifyData extends SequentialLessonAdapter
{
    public final static A MANDIANT_LOGO = new A().setHref("http://www.mandiant.com").addElement(new IMG("images/logos/mandiant.png").setAlt("MANDIANT").setBorder(0).setHspace(0).setVspace(0));
    
    private final static String USERID = "userid";

    private final static String TARGET_USERID = "jsmith";
    private final static String NONTARGET_USERID = "lsmith";

    private String userid;

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

        try
        {
            Connection connection = DatabaseUtilities.getConnection(s);

            ec.addElement(makeAccountLine(s));

            String query = "SELECT * FROM salaries WHERE userid = '" + userid + "'";
            //ec.addElement(new PRE(query));

            try
            {
                // check target data
                Statement target_statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet target_results = target_statement.executeQuery("SELECT salary from salaries where userid='"+TARGET_USERID+"'");
                target_results.first();
                String before_salary_target_salary = target_results.getString(1);
                
                System.out.println("Before running query, salary for target userid " + TARGET_USERID + " = " + before_salary_target_salary );
                    
                target_results = target_statement.executeQuery("SELECT salary from salaries where userid='"+NONTARGET_USERID+"'");
                target_results.first();
                String before_salary_nontarget_salary = target_results.getString(1);
                
                System.out.println("Before running query, salary for nontarget userid " + NONTARGET_USERID + " = " + before_salary_nontarget_salary );
                
                // execute query
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                    ResultSet.CONCUR_READ_ONLY);
                //
                statement.execute(query);
                
                ResultSet results = statement.getResultSet();

                if ((results != null) && (results.first() == true))
                {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
                    results.last();
                }
                else
                {
                    ec.addElement(WebGoatI18N.get("NoResultsMatched"));
                }
                
                // see if target data was modified
                target_results = target_statement.executeQuery("SELECT salary from salaries where userid='"+TARGET_USERID+"'");
                target_results.first();
                String after_salary_target_salary = target_results.getString(1);
                
                System.out.println("After running query, salary for target userid " + TARGET_USERID + " = " + before_salary_target_salary );
                    
                target_results = target_statement.executeQuery("SELECT salary from salaries where userid='"+NONTARGET_USERID+"'");
                target_results.first();
                String after_salary_nontarget_salary = target_results.getString(1);
                
                System.out.println("After running query, salary for nontarget userid " + NONTARGET_USERID + " = " + before_salary_nontarget_salary );
                
                if(!after_salary_nontarget_salary.equals(before_salary_nontarget_salary)) {
                    s.setMessage("You modified the salary for another userid, in order to succeed you must modify the salary of only userid " 
                            + TARGET_USERID + ".");
                } else {
                    if(!after_salary_target_salary.equals(before_salary_target_salary)) {
                        makeSuccess(s);
                    }
                }
                
            } catch (SQLException sqle)
            {
                ec.addElement(new P().addElement(sqle.getMessage()));
                sqle.printStackTrace();
            }
        } catch (Exception e)
        {
            s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    

    protected Element makeAccountLine(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new P().addElement(WebGoatI18N.get("EnterUserid")));

        userid = s.getParser().getRawParameter(USERID, "jsmith");
        Input input = new Input(Input.TEXT, USERID, userid.toString());
        ec.addElement(input);

        Element b = ECSFactory.makeButton(WebGoatI18N.get("Go!"));
        ec.addElement(b);

        return ec;

    }

    /**
     * Gets the category attribute of the SqNumericInjection object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.INJECTION;
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
        List<String> hints = new ArrayList<String>();
        
        hints.add(WebGoatI18N.get("SqlModifyDataHint1"));
        hints.add(WebGoatI18N.get("SqlModifyDataHint2"));
        hints.add(WebGoatI18N.get("SqlModifyDataHint3"));
        hints.add(WebGoatI18N.get("SqlModifyDataHint4"));
        hints.add(WebGoatI18N.get("SqlModifyDataHint5"));

        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(77);

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
        return ("Modify Data with SQL Injection");
    }

    /**
     * Gets the instructions attribute of the SqlInjection object
     *  
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
    String instructions = "The form below allows a user to view salaries associated with a userid "
        + "(from the table named <b>salaries</b>).  This form" 
        + " is vulnerable to String SQL Injection.  In order to pass this lesson, use SQL Injection to "
        + "modify the salary for userid <b>"
        + TARGET_USERID + "</b>.";

    return (instructions);
    }
    
    /**
     * Constructor for the DatabaseFieldScreen object
     * 
     * @param s
     *            Description of the Parameter
     */
    public void handleRequest(WebSession s)
    {
        try
        {
            super.handleRequest(s);
        } catch (Exception e)
        {
            // System.out.println("Exception caught: " + e);
            e.printStackTrace(System.out);
        }
    }

}
