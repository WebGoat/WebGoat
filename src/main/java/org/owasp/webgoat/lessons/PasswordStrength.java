
package org.owasp.webgoat.lessons;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.LI;
import org.apache.ecs.html.OL;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
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

public class PasswordStrength extends LessonAdapter
{
    private Map<String, Password> passwords = new TreeMap<String, Password>() {{
        put("pass1", new Password("123456", "seconds", "0", "dictionary based, in top 10 most used passwords"));
        put("pass2", new Password("abzfezd", "seconds", "2", "26 chars on 7 positions, 8 billion possible combinations"));
        put("pass3", new Password("a9z1ezd", "seconds", "19", "26 + 10 chars on 7 positions = 78 billion possible combinations"));
        put("pass4", new Password("aB8fEzDq", "hours", "15", "26 + 26 + 10 chars on 8 positions = 218 trillion possible combinations"));
        put("pass5", new Password("z8!E?7D$", "days", "20", "96 chars on 8 positions = 66 quintillion possible combinations"));
        put("pass6", new Password("My1stPassword!:Redd", "quintillion years", "364", "96 chars on 19 positions = 46 undecillion possible combinations"));
    }};
    
    private class Password {
        
        String password;
        String timeUnit;
        String answer;
        private String explanation;
        
        public Password(String password, String timeUnit, String answer, String explanation) {
            this.password = password;
            this.timeUnit = timeUnit;
            this.answer = answer;
            this.explanation = explanation;
        }
    }
    
    private boolean checkSolution(WebSession s) throws ParameterNotFoundException {
        boolean allCorrect = true;
        for ( int i = 1; i <= passwords.size(); i++ ) {
            String key = "pass" + i;
            allCorrect = allCorrect && s.getParser().getStringParameter(key, "").equals(passwords.get(key).answer);
        }
        return allCorrect;
    }

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
            if (checkSolution(s))
            {
                makeSuccess(s);
                ec.addElement(new BR());
                ec.addElement(new StringElement("As a guideline not bound to a single solution."));
                ec.addElement(new BR());
                ec.addElement(new StringElement("Assuming the calculations per second 4 billion: "));
                ec.addElement(new BR());
                OL ol = new OL();
                for ( Password password : passwords.values()) {
                    ol.addElement(new LI(String.format("%s - %s %s (%s)", password.password, password.answer, password.timeUnit, password.explanation)));
                }
                ec.addElement(ol);
            } else
            {
                ec.addElement(new BR());
                ec.addElement(new StringElement("How much time would a desktop PC take to crack these passwords?"));
                ec.addElement(new BR());
                ec.addElement(new BR());
                Table table = new Table();
                for ( Entry<String, Password> entry : passwords.entrySet()) {
                    TR tr = new TR();
                    TD td1 = new TD();
                    TD td2 = new TD();
                    Input input1 = new Input(Input.TEXT, entry.getKey(), "");
                    td1.addElement(new StringElement("Password = " + entry.getValue().password));
                    td1.setWidth("50%");
                    td2.addElement(input1);
                    td2.addElement(new StringElement("  " + entry.getValue().timeUnit));
                    tr.addElement(td1);
                    tr.addElement(td2);
                    table.addElement(tr);
                }
                ec.addElement(table);
                ec.addElement(new BR());
                ec.addElement(new BR());
                Div div = new Div();
                div.addAttribute("align", "center");
                Element b = ECSFactory.makeButton("Go!");
                div.addElement(b);
                ec.addElement(div);
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }


        return (ec);
    }

    /**
     * Gets the hints attribute of the HelloScreen object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Copy the passwords into the code checker.");
        return hints;
    }

    /**
     * Gets the ranking attribute of the HelloScreen object
     * 
     * @return The ranking value
     */
    private final static Integer DEFAULT_RANKING = new Integer(6);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    protected Category getDefaultCategory()
    {
        return Category.AUTHENTICATION;
    }

    public String getInstructions(WebSession s)
    {
        String instructions = "The accounts of your web application are only as save as the passwords. "
                + "For this exercise, your job is to test several passwords on <a href=\"https://howsecureismypassword.net\" target=\"_blank\">https://howsecureismypassword.net</a>. "
                + " You must test all 6 passwords at the same time...<br>"
                + "<b> On your applications you should set good password requirements! </b>";
        return (instructions);
    }

    /**
     * Gets the title attribute of the HelloScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Password Strength");
    }

    public Element getCredits()
    {
        return super.getCustomCredits("Created by: Reto Lippuner, Marcel Wirth", new StringElement(""));
    }
}
