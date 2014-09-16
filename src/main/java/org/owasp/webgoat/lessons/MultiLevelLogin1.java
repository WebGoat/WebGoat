
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.xhtml.style;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;


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
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Reto Lippuner, Marcel Wirth
 * @created April 7, 2008
 */

public class MultiLevelLogin1 extends SequentialLessonAdapter
{
    private final static String USER = "user";
    private final static String PASSWORD = "pass";
    private final static String HIDDEN_TAN = "hidden_tan";
    private final static String TAN = "tan";

    private final static String LOGGEDIN = "loggedin";
    private final static String CORRECTTAN = "correctTan";
    private final static String LOGGEDINUSER = "loggedInUser";

    /**
     * Creates Staged WebContent
     * 
     * @param s
     */
    protected Element createContent(WebSession s)
    {
        return super.createStagedContent(s);
    }

    /**
     * See if the user has logged in correctly
     * 
     * @param s
     * @return true if loggedIn
     */
    private boolean loggedIn(WebSession s)
    {
        try
        {
            return s.get(LOGGEDIN).equals("true");
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * See if the user had used a valid tan
     * 
     * @param s
     * @return true if correctTan
     */
    private boolean correctTan(WebSession s)
    {
        try
        {
            return s.get(CORRECTTAN).equals("true");
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Get the logged in user
     * 
     * @param s
     * @return the logged in user
     */
    private String getLoggedInUser(WebSession s)
    {
        try
        {
            String user = (String) s.get(LOGGEDINUSER);
            return user;
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Creation of the main content
     * 
     * @param s
     * @return Element
     */
    protected Element createMainContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        try
        {
            style sty = new style();

            sty
                    .addElement("#lesson_wrapper {height: 435px;width: "
                            + "500px;}#lesson_header {background-image: "
                            + "url(lessons/DBSQLInjection/images/lesson1_header.jpg);width:"
                            + " 490px;padding-right: 10px;padding-top: 60px;background-repeat: no-repeat;}.lesson_workspace "
                            + "{background-image: url(lessons/DBSQLInjection/images/lesson1_workspace.jpg);width: 489px;height: "
                            + "325px;padding-left: 10px;padding-top: 10px;background-repeat: no-repeat;}        "
                            + ".lesson_text {height: 240px;width: 460px;padding-top: 5px;}          "
                            + "#lesson_buttons_bottom {height: 20px;width: 460px;}          "
                            + "#lesson_b_b_left {width: 300px;float: left;}         "
                            + "#lesson_b_b_right input {width: 100px;float: right;}         "
                            + ".lesson_title_box {height: 20px;width: 420px;padding-left: 30px;}            "
                            + ".lesson_workspace { }            "
                            + ".lesson_txt_10 {font-family: Arial, Helvetica, sans-serif;font-size: 10px;}          "
                            + ".lesson_text_db {color: #0066FF}         "
                            + "#lesson_login {background-image: url(lessons/DBSQLInjection/images/lesson1_loginWindow.jpg);height: "
                            + "124px;width: 311px;background-repeat: no-repeat;padding-top: 30px;margin-left: 80px;margin-top:"
                            + " 50px;text-align: center;}           #lesson_login_txt {font-family: Arial, Helvetica, sans-serif;font-size: "
                            + "12px;text-align: center;}            #lesson_search {background-image: "
                            + "url(lessons/DBSQLInjection/images/lesson1_SearchWindow.jpg);height: 124px;width: 311px;background-repeat: "
                            + "no-repeat;padding-top: 30px;margin-left: 80px;margin-top: 50px;text-align: center;}");
            ec.addElement(sty);

            Div wrapperDiv = new Div();
            wrapperDiv.setID("lesson_wrapper");

            Div headerDiv = new Div();
            headerDiv.setID("lesson_header");

            Div workspaceDiv = new Div();
            workspaceDiv.setClass("lesson_workspace");

            wrapperDiv.addElement(headerDiv);
            wrapperDiv.addElement(workspaceDiv);

            ec.addElement(wrapperDiv);

            workspaceDiv.addElement(createWorkspaceContent(s));

        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    /**
     * Creation of the content of the workspace
     * 
     * @param s
     * @return Element
     */
    private Element createWorkspaceContent(WebSession s)
    {
        String user = "";
        user = s.getParser().getStringParameter(USER, "");
        String password = "";
        password = s.getParser().getStringParameter(PASSWORD, "");
        String tan = "";
        tan = s.getParser().getStringParameter(TAN, "");
        String hiddenTan = s.getParser().getStringParameter(HIDDEN_TAN, "");

        ElementContainer ec = new ElementContainer();

        // verify that tan is correct and user is logged in
        if (loggedIn(s) && correctTan(getLoggedInUser(s), tan, hiddenTan, s))
        {
            s.add(CORRECTTAN, "true");
        }
        // user is loggedIn but enters wrong tan
        else if (loggedIn(s) && !correctTan(getLoggedInUser(s), tan, hiddenTan, s))
        {
            s.add(LOGGEDIN, "false");
        }

        // verify the password
        if (correctLogin(user, password, s))
        {
            s.add(LOGGEDIN, "true");
            s.add(LOGGEDINUSER, user);
        }

        // if restart link is clicked owe have to reset log in
        if (!s.getParser().getStringParameter("Restart", "").equals(""))
        {
            s.add(LOGGEDIN, "false");
            s.add(CORRECTTAN, "false");
            resetTans(s);
        }
        // Logout Button is pressed
        if (s.getParser().getRawParameter("logout", "").equals("true"))
        {
            s.add(LOGGEDIN, "false");
            s.add(CORRECTTAN, "false");

        }
        if (loggedIn(s) && correctTan(s))
        {
            s.add(LOGGEDIN, "false");
            s.add(CORRECTTAN, "false");

            createSuccessfulLoginContent(s, ec);
            if (getLessonTracker(s).getStage() == 2)
            {
                if (hiddenTan.equals("1"))
                {
                    makeSuccess(s);
                }
            }
            else
            {
                getLessonTracker(s).setStage(2);
                s.setMessage("Stage 1 completed.");
            }
        }

        else if (loggedIn(s))
        {
            int tanNr = getTanPosition(getLoggedInUser(s), s);
            if (tanNr == 0)
            {
                createNoTanLeftContent(ec);

            }
            else
            {
                createAskForTanContent(s, ec, tanNr);
            }

        }
        else
        {
            String errorMessage = "";

            if (!(user + password).equals(""))
            {
                errorMessage = "Login failed! Make sure " + "that user name and password is correct.";
            }
            else if (!tan.equals(""))
            {
                errorMessage = "Login failed. Tan is " + "incorrect.";
            }

            createLogInContent(ec, errorMessage);
        }

        return ec;
    }

    /**
     * Create content for logging in
     * 
     * @param ec
     */
    private void createLogInContent(ElementContainer ec, String errorMessage)
    {
        Div loginDiv = new Div();
        loginDiv.setID("lesson_login");

        Table table = new Table();
        table.addAttribute("align='center'", 0);
        TR tr1 = new TR();
        TD td1 = new TD();
        TD td2 = new TD();
        td1.addElement(new StringElement("Enter your name: "));
        td2.addElement(new Input(Input.TEXT, USER));
        tr1.addElement(td1);
        tr1.addElement(td2);

        TR tr2 = new TR();
        TD td3 = new TD();
        TD td4 = new TD();
        td3.addElement(new StringElement("Enter your password: "));
        td4.addElement(new Input(Input.PASSWORD, PASSWORD));
        tr2.addElement(td3);
        tr2.addElement(td4);

        TR tr3 = new TR();
        TD td5 = new TD();
        td5.setColSpan(2);
        td5.setAlign("center");

        td5.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
        tr3.addElement(td5);

        table.addElement(tr1);
        table.addElement(tr2);
        table.addElement(tr3);
        loginDiv.addElement(table);
        ec.addElement(loginDiv);

        H2 errorTag = new H2(errorMessage);
        errorTag.addAttribute("align", "center");
        errorTag.addAttribute("class", "info");
        ec.addElement(errorTag);
    }

    /**
     * Create content in which the tan is asked
     * 
     * @param s
     * @param ec
     * @param tanNr
     */
    private void createAskForTanContent(WebSession s, ElementContainer ec, int tanNr)
    {

        Div loginDiv = new Div();
        loginDiv.setID("lesson_login");

        Table table = new Table();
        table.addAttribute("align='center'", 0);
        TR tr1 = new TR();
        TD td1 = new TD();
        TD td2 = new TD();
        td1.addElement(new StringElement("Enter TAN  #" + tanNr + ": "));
        td2.addElement(new Input(Input.TEXT, TAN));
        tr1.addElement(td1);
        tr1.addElement(td2);

        TR tr2 = new TR();
        TD td3 = new TD();
        td3.setColSpan(2);
        td3.setAlign("center");

        td3.addElement(new Input(Input.SUBMIT, "Submit", "Submit"));
        tr2.addElement(td3);

        table.addElement(tr1);
        table.addElement(tr2);

        ec.addElement(new Input(Input.HIDDEN, HIDDEN_TAN, tanNr));
        loginDiv.addElement(table);
        ec.addElement(loginDiv);
        ec.addElement(createLogoutLink());

    }

    /**
     * Create content if there is no tan left
     * 
     * @param ec
     */
    private void createNoTanLeftContent(ElementContainer ec)
    {

        ec.addElement(new BR());
        ec.addElement(new BR());
        ec.addElement(new BR());
        ec.addElement(new BR());
        H1 h = new H1("<center>No tan is left! Please contact the admin. </center>");
        ec.addElement(h);
        ec.addElement(createLogoutLink());
    }

    /**
     * Create content after a successful login
     * 
     * @param s
     * @param ec
     */
    private void createSuccessfulLoginContent(WebSession s, ElementContainer ec)
    {

        updateTan(getLoggedInUser(s), s);
        String userDataStyle = "margin-top:50px;";

        Div userDataDiv = new Div();
        userDataDiv.setStyle(userDataStyle);
        userDataDiv.addAttribute("align", "center");
        Table table = new Table();
        table.addAttribute("cellspacing", 10);
        table.addAttribute("cellpadding", 5);

        table.addAttribute("align", "center");
        TR tr1 = new TR();
        TR tr2 = new TR();
        TR tr3 = new TR();
        TR tr4 = new TR();
        tr1.addElement(new TD("<b>Firstname:</b>"));
        tr1.addElement(new TD(getLoggedInUser(s)));

        try
        {
            ResultSet results = getUser(getLoggedInUser(s), s);
            results.first();

            tr2.addElement(new TD("<b>Lastname:</b>"));
            tr2.addElement(new TD(results.getString("last_name")));

            tr3.addElement(new TD("<b>Credit Card Type:</b>"));
            tr3.addElement(new TD(results.getString("cc_type")));

            tr4.addElement(new TD("<b>Credit Card Number:</b>"));
            tr4.addElement(new TD(results.getString("cc_number")));

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        table.addElement(tr1);
        table.addElement(tr2);
        table.addElement(tr3);
        table.addElement(tr4);

        userDataDiv.addElement(table);
        ec.addElement(userDataDiv);
        ec.addElement(createLogoutLink());
    }

    /**
     * Create a link for logging out
     * 
     * @return Element
     */
    private Element createLogoutLink()
    {
        A logoutLink = new A();
        logoutLink.addAttribute("href", getLink() + "&logout=true");
        logoutLink.addElement("Logout");

        String logoutStyle = "margin-right:50px; mrgin-top:30px";
        Div logoutDiv = new Div();
        logoutDiv.addAttribute("align", "right");
        logoutDiv.addElement(logoutLink);
        logoutDiv.setStyle(logoutStyle);

        return logoutDiv;
    }

    /**
     * Update the tan. Every tan should be used only once.
     * 
     * @param user
     * @param s
     */
    private void updateTan(String user, WebSession s)
    {
        int tanNr = getTanPosition(user, s);

        Connection connection = null;

        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "UPDATE user_data_tan SET login_count = ? WHERE first_name = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.setInt(1, tanNr);
            prepStatement.setString(2, user);
            prepStatement.execute();

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * If lesson is reseted the tans should be resetted too
     * 
     * @param s
     */
    private void resetTans(WebSession s)
    {
        Connection connection = null;
        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "UPDATE user_data_tan SET login_count = 0 WHERE login_count > 0";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.execute();

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * Get the count of the tan
     * 
     * @param user
     * @param s
     * @return tanPosition
     */
    private int getTanPosition(String user, WebSession s)
    {
        int tanNr = 0;
        Connection connection = null;
        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "SELECT login_count FROM user_data_tan WHERE first_name = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.setString(1, user);
            ResultSet results = prepStatement.executeQuery();

            if ((results != null) && (results.first() == true))
            {

                tanNr = results.getInt(results.getRow());
                tanNr = tanNr + 1;
                if (tanNr > 5)
                {
                    tanNr = 0;
                }
                // make sure you don't get the first tan in stage 2
                if (getLessonTracker(s).getStage() == 2 && tanNr == 1)
                {
                    ++tanNr;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return tanNr;
    }

    /**
     * Get a user by its name
     * 
     * @param user
     * @param s
     * @return ResultSet containing the user
     */
    private ResultSet getUser(String user, WebSession s)
    {
        Connection connection = null;
        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "SELECT * FROM user_data_tan WHERE first_name = ? ";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.setString(1, user);

            ResultSet results = prepStatement.executeQuery();

            return results;

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * See if the tan is correct
     * 
     * @param user
     * @param tan
     * @param tanPosition
     * @param s
     * @return true if the tan is correct
     */
    private boolean correctTan(String user, String tan, String tanPosition, WebSession s)
    {
        if (tan.equals("")) { return false; }
        Connection connection = null;
        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "SELECT user_data_tan.userid FROM user_data_tan, tan WHERE user_data_tan.first_name = ? "
                    + "AND user_data_tan.userid = tan.userid AND tan.tanValue = ? AND tan.tanNr = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.setString(1, user);
            prepStatement.setString(2, tan);
            prepStatement.setString(3, tanPosition);

            ResultSet results = prepStatement.executeQuery();

            if ((results != null) && (results.first() == true)) {

            return true;

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * See if the password and corresponding user is valid
     * 
     * @param userName
     * @param password
     * @param s
     * @return true if the password was correct
     */
    private boolean correctLogin(String userName, String password, WebSession s)
    {
        Connection connection = null;
        try
        {
            connection = DatabaseUtilities.getConnection(s);
            String query = "SELECT * FROM user_data_tan WHERE first_name = ? AND password = ?";
            PreparedStatement prepStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                            ResultSet.CONCUR_READ_ONLY);
            prepStatement.setString(1, userName);
            prepStatement.setString(2, password);

            ResultSet results = prepStatement.executeQuery();

            if ((results != null) && (results.first() == true)) {

            return true;

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;

    }

    /**
     * Gets the category attribute of the RoleBasedAccessControl object
     * 
     * @return The category value
     */
    protected ElementContainer doStage1(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(createMainContent(s));
        return ec;

    }

    /**
     * After finishing succesful stage1 this function is called
     */
    protected Element doStage2(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(createMainContent(s));
        return ec;
    }

    /**
     * Get the category
     * 
     * @return the category
     */
    protected Category getDefaultCategory()
    {
        return Category.AUTHENTICATION;
    }

    /**
     * Gets the hints attribute of the RoleBasedAccessControl object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();

        hints.add("Stage 1: Just do a regular login");
        hints.add("Stage 2: How does the server know which TAN has to be used?");
        hints.add("Stage 2: Maybe taking a look at the source code helps");
        hints.add("Stage 2: Watch out for hidden fields");
        hints.add("Stage 2: Manipulate the hidden field 'hidden_tan'");

        return hints;

    }

    /**
     * Get the instructions for the user
     */
    public String getInstructions(WebSession s)
    {
        String instructions = "";
        if (getLessonTracker(s).getStage() == 1)
        {
            instructions = "STAGE 1:\t This stage is just to show how a classic multi login works. "
                    + "Your goal is to do a regular login as <b>Jane</b> with password <b>tarzan</b>. "
                    + "You have following TANs: <br>" + "Tan #1 = 15648<br>" + "Tan #2 = 92156<br>"
                    + "Tan #3 = 4879<br>" + "Tan #4 = 9458<br>" + "Tan #5 = 4879<br>";

        }
        else if (getLessonTracker(s).getStage() == 2)
        {
            instructions = "STAGE 2:\tNow you are a hacker who " + "already has stolen some information from Jane by "
                    + "a phishing mail. " + "You have the password which is tarzan and "
                    + "the Tan #1 which is 15648 <br>" + "The problem is that the first tan is already "
                    + "used... try to break into the system anyway. ";
        }

        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(110);

    /**
     * Get the ranking for the hirarchy of lessons
     */
    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Get the title of the Lesson
     */
    public String getTitle()
    {
        return ("Multi Level Login 1");
    }

    public Element getCredits()
    {
        return super.getCustomCredits("Created by: Reto Lippuner, Marcel Wirth", new StringElement(""));
    }
}
