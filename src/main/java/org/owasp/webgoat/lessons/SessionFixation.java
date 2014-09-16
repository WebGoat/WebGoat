
package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
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
 * @created April 8, 2008
 */

public class SessionFixation extends SequentialLessonAdapter
{

    private final String mailTo = "jane.plane@owasp.org";
    private final String mailFrom = "admin@webgoatfinancial.com";
    private final String mailTitel = "Check your account";
    private final String MAILCONTENTNAME = "mailContent";

    private final static String USER = "user3";
    private final static String PASSWORD = "pass3";
    private final static String LOGGEDIN = "loggedin3";
    private final static String LOGGEDINUSER = "loggedInUser3";
    private final static Random random = new Random(System.currentTimeMillis());
    private String sid = "";

    /**
     * Creates Staged WebContent
     * 
     * @param s
     */
    protected Element createContent(WebSession s)
    {
        if (sid.equals("") && getLessonTracker(s).getStage() > 2)
        {
            getLessonTracker(s).setStage(1);
        }
        String sid = s.getParser().getStringParameter("SID", "");
        if (!sid.equals(""))
        {
            this.sid = sid;
        }
        if (!s.getParser().getStringParameter("Restart", "").equals(""))
        {
            s.add(LOGGEDIN, "false");
            s.add("SID", "");
            this.sid = "";
        }
        if (getLessonTracker(s).getStage() == 3)
        {
            s.add("SID", sid);
            if (!sid.equals(""))
            {
                s.add("SID", sid);
            }
            else
            {
                String randomSid = randomSIDGenerator();
                s.add("SID", randomSid);
                this.sid = randomSid;
            }

            String name = s.getParser().getStringParameter(USER, "");
            String password = s.getParser().getStringParameter(PASSWORD, "");
            if (correctLogin(name, password, s))
            {
                getLessonTracker(s).setStage(4);
                sid = "";
                s.add(LOGGEDIN, "true");
                s.add(LOGGEDINUSER, name);
                s.setMessage("You completed stage 3!");
            }

        }
        if (getLessonTracker(s).getStage() == 4)
        {

            if (sid.equals("NOVALIDSESSION"))
            {
                // System.out.println("STAGE 5");
                getLessonTracker(s).setStage(5);
            }

        }

        if (getLessonTracker(s).getStage() == 2)
        {
            if (!sid.equals(""))
            {
                s.add("SID", sid);
                getLessonTracker(s).setStage(3);
                s.setMessage("You completed stage 2!");
            }
        }

        String mailContent = s.getParser().getRawParameter(MAILCONTENTNAME, "");
        if (!mailContent.equals(""))
        {
            s.add(MAILCONTENTNAME, mailContent);
        }
        if ((mailContent.contains("&SID=") || mailContent.contains("?SID=")) && getLessonTracker(s).getStage() == 1)
        {
            getLessonTracker(s).setStage(2);
            s.setMessage("You completed stage 1!");
        }

        return super.createStagedContent(s);
    }

    /**
     * Gets the category attribute of the RoleBasedAccessControl object
     * 
     * @return The category value
     */
    protected ElementContainer doStage1(WebSession s)
    {

        ElementContainer ec = new ElementContainer();
        ec.addElement(createStage1Content(s));
        return ec;

    }

    @Override
    protected Element doStage2(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(createStage2Content(s));
        return ec;
    }

    private Element createStage2Content(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        String mailHeader = "<b>Mail From:</b> &nbsp;&nbsp;admin@webgoatfinancial.com<br><br>";
        String mailContent = (String) s.get(MAILCONTENTNAME);

        // Reset Lesson if server was shut down
        if (mailContent == null)
        {
            getLessonTracker(s).setStage(1);
            return createStage1Content(s);
        }

        ec.addElement(mailHeader + mailContent);

        return ec;

    }

    @Override
    protected Element doStage3(WebSession s) throws Exception
    {
        return createStage3Content(s);
    }

    @Override
    protected Element doStage4(WebSession s) throws Exception
    {
        return createStage4Content(s);
    }

    @Override
    protected Element doStage5(WebSession s) throws Exception
    {
        // System.out.println("Doing stage 5");
        return createStage5Content(s);
    }

    private Element createStage5Content(WebSession s)
    {

        return createMainLoginContent(s);
    }

    private Element createStage3Content(WebSession s)
    {

        return createMainLoginContent(s);
    }

    private Element createStage4Content(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement("<h2>Jane has logged into her account. Go and grab her session!"
                + " Use Following link to reach the login screen of the bank:</h2><br><br>" + "<a href="
                + super.getLink() + "&SID=NOVALIDSESSION><center> Goat Hills Financial </center></a><br><br><br><br>");
        return ec;
        // return createMainLoginContent(s);
    }

    private Element createStage1Content(WebSession s)
    {

        String link = getLink();
        String mailText = "<b>Dear MS. Plane</b> <br><br>"
                + "During the last week we had a few problems with our database. "
                + "We have received many complaints regarding incorrect account details. "
                + "Please use the following link to verify your account "
                + "data:<br><br><center><a href=/webgoat/"
                + link
                + "> Goat Hills Financial</a></center><br><br>"
                + "We are sorry for the any inconvenience and thank you for your cooparation.<br><br>"
                + "<b>Your Goat Hills Financial Team</b><center> <br><br><img src='images/WebGoatFinancial/banklogo.jpg'></center>";

        ElementContainer ec = new ElementContainer();
        Table table = new Table();
        TR tr1 = new TR();
        TD td1 = new TD();
        TD td2 = new TD();

        TR tr2 = new TR();
        TD td3 = new TD();
        TD td4 = new TD();

        TR tr3 = new TR();
        TD td5 = new TD();
        TD td6 = new TD();

        TR tr4 = new TR();
        TD td7 = new TD();
        td7.addAttribute("colspan", 2);

        TR tr5 = new TR();
        TD td8 = new TD();
        td8.addAttribute("colspan", 2);
        td8.addAttribute("align", "center");

        table.addElement(tr1);
        table.addElement(tr2);
        table.addElement(tr3);
        table.addElement(tr4);
        table.addElement(tr5);

        tr1.addElement(td1);
        tr1.addElement(td2);
        tr2.addElement(td3);
        tr2.addElement(td4);
        tr3.addElement(td5);
        tr3.addElement(td6);
        tr4.addElement(td7);
        tr5.addElement(td8);

        ec.addElement(table);

        B b = new B();
        b.addElement("Mail To: ");
        td1.addElement(b);
        td2.addElement(mailTo);

        b = new B();
        b.addElement("Mail From: ");
        td3.addElement(b);
        td4.addElement(mailFrom);

        b = new B();
        b.addElement("Title: ");
        td5.addElement(b);
        Input titleField = new Input();
        titleField.setValue(mailTitel);
        titleField.addAttribute("size", 30);
        td6.addElement(titleField);

        TextArea mailContent = new TextArea();
        mailContent.addAttribute("cols", 67);
        mailContent.addAttribute("rows", 8);
        mailContent.addElement(mailText);
        mailContent.setName(MAILCONTENTNAME);
        td7.addElement(mailContent);

        td8.addElement(new Input(Input.SUBMIT, "SendMail", "Send Mail"));

        return ec;
    }

    /**
     * Creation of the main content
     * 
     * @param s
     * @return Element
     */
    protected Element createMainLoginContent(WebSession s)
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
        ElementContainer ec = new ElementContainer();
        String name = s.getParser().getStringParameter(USER, "");
        String password = s.getParser().getStringParameter(PASSWORD, "");

        try
        {
            // Logout Button is pressed
            if (s.getParser().getRawParameter("logout", "").equals("true"))
            {
                s.add(LOGGEDIN, "false");
                s.add("SID", "");
                this.sid = "";

            }
            if (correctLogin(name, password, s))
            {
                s.add(LOGGEDINUSER, name);
                s.add(LOGGEDIN, "true");
                createSuccessfulLoginContent(s, ec);
            }
            else if (sid.equals(s.get("SID")) && s.get(LOGGEDIN).equals("true"))
            {
                makeSuccess(s);
                createSuccessfulLoginContent(s, ec);
            }
            else
            {
                if ((name + password).equals(""))
                {
                    createLogInContent(ec, "");

                }
                else
                {
                    createLogInContent(ec, "Login Failed! Make sure user name and password is correct!");

                }
            }
        } catch (Exception e)
        {
            if ((name + password).equals(""))
            {
                createLogInContent(ec, "");

            }
            else
            {
                createLogInContent(ec, "Login Failed! Make sure user name and password is correct!");
            }
        }

        return ec;
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
     * Create content for logging in
     * 
     * @param ec
     */
    private void createLogInContent(ElementContainer ec, String errorMessage)
    {
        Div loginDiv = new Div();
        loginDiv.setID("lesson_login");

        Table table = new Table();
        // table.setStyle(tableStyle);
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

        td5.addElement(new Input(Input.SUBMIT, "Submit", "Login"));
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
     * Create content after a successful login
     * 
     * @param s
     * @param ec
     */
    private void createSuccessfulLoginContent(WebSession s, ElementContainer ec)
    {
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
     * Get the category
     * 
     * @return the category
     */
    protected Category getDefaultCategory()
    {
        return Category.SESSION_MANAGEMENT;
    }

    /**
     * Gets the hints attribute of the RoleBasedAccessControl object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();

        hints.add("Stage 1: Where is the link in the mail?");
        hints.add("Stage 1: Add a SID to the link");
        hints.add("Stage 1: A SID could looke something like this: SID=Whatever");
        hints.add("Stage 1: Alter the link in the mail to: href=" + getLink() + "&SID=Whatever");
        hints.add("Stage 2: Click on the link!");
        hints.add("Stage 3: Log in as Jane with user name jane and password tarzan.");
        hints.add("Stage 4: Click on the link provided");
        hints.add("Stage 4: What is your actual SID?");
        hints.add("Stage 4: Change the SID (NOVALIDSESSION) to the choosen one in the mail");

        return hints;

    }

    /**
     * Get the instructions for the user
     */
    public String getInstructions(WebSession s)
    {
        int stage = getLessonTracker(s).getStage();
        if (stage > 4)
        {
            stage = 4;
        }
        String instructions = "STAGE " + stage + ": ";
        if (stage == 1)
        {
            instructions += "You are Hacker Joe and " + "you want to steal the session from Jane. "
                    + "Send a prepared email to the victim " + "which looks like an official email from the bank.  "
                    + "A template message is prepared below, you will need to add "
                    + "a Session ID (SID) in the link inside the email. Alter "
                    + "the link to include a SID.<br><br><b>You are: Hacker Joe</b>";
        }
        else if (stage == 2)
        {
            instructions += "Now you are the victim Jane who received the email below. "
                    + "If you point on the link with your mouse you will see that there is a SID included. "
                    + "Click on it to see what happens.<br><br><b>You are: Victim Jane</b> ";
        }
        else if (stage == 3)
        {
            instructions += "The bank has asked you to verfy your data. Log in to see if your details are "
                    + "correct. Your user name is <b>Jane</b> and your password is <b>tarzan</b>. <br><br><b>You are: Victim Jane</b> ";
        }
        else if (stage == 4)
        {
            instructions += "It is time to steal the session now. Use following link to reach Goat Hills "
                    + "Financial.<br><br><b>You are: Hacker Joe</b> ";
        }

        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(222);

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
        return ("Session Fixation");
    }

    @Override
    public void handleRequest(WebSession s)
    {
        Form form = new Form();
        form.addElement(createContent(s));
        form.setAction(getFormAction());
        form.setMethod(Form.POST);
        form.setName("form");
        form.setEncType("");
        setContent(form);
    }

    @Override
    public String getLink()
    {

        if (sid.equals("")) { return super.getLink(); }
        return super.getLink() + "&SID=" + sid;
    }

    private String randomSIDGenerator()
    {
        String sid = "";

        sid = String.valueOf(Math.abs(random.nextInt() % 100000));
        return sid;
    }

    public Element getCredits()
    {
        return super.getCustomCredits("Created by: Reto Lippuner, Marcel Wirth", new StringElement(""));
    }

}
