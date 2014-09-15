
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
 * For details, please see http://webgoat.github.io
 * 
 * @author Eric Sheridan, Aspect Security <a href="http://www.aspectsecurity.com"/>
 * @created October 28, 2003
 */

public class DangerousEval extends LessonAdapter
{
    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));

    public final static String PASSED = "__DANGEROUS_EVAL_PASS";

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
        String regex1 = "^[0-9]{3}$";// any three digits
        Pattern pattern1 = Pattern.compile(regex1);

        try
        {
            checkSuccess(s);

            String param1 = s.getParser().getRawParameter("field1", "111");
            // String param2 = HtmlEncoder.encode(s.getParser().getRawParameter("field2", "4128 3214
            // 0002 1999"));
            float quantity = 1.0f;
            float total = 0.0f;
            float runningTotal = 0.0f;

            // FIXME: encode output of field2, then s.setMessage( field2 );
            ec.addElement("<script src='lessonJS/eval.js'> </script>");
            // <script src='javascript/sameOrigin.js' language='JavaScript'></script>
            ec.addElement(new HR().setWidth("90%"));
            ec.addElement(new Center().addElement(new H1().addElement("Shopping Cart ")));
            Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

            if (s.isColor())
            {
                t.setBorder(1);
            }

            TR tr = new TR();
            tr.addElement(new TH().addElement("Shopping Cart Items -- To Buy Now").setWidth("80%"));
            tr.addElement(new TH().addElement("Price").setWidth("10%"));
            tr.addElement(new TH().addElement("Quantity").setWidth("3%"));
            tr.addElement(new TH().addElement("Total").setWidth("7%"));
            t.addElement(tr);

            tr = new TR();
            tr.addElement(new TD().addElement("Studio RTA - Laptop/Reading Cart with Tilting Surface - Cherry "));
            tr.addElement(new TD().addElement("69.99").setAlign("right"));
            tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY1", s.getParser().getStringParameter("QTY1", "1"))).setAlign("right"));
            quantity = s.getParser().getFloatParameter("QTY1", 0.0f);
            total = quantity * 69.99f;
            runningTotal += total;
            tr.addElement(new TD().addElement("$" + total));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("Dynex - Traditional Notebook Case"));
            tr.addElement(new TD().addElement("27.99").setAlign("right"));
            tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY2", s.getParser().getStringParameter("QTY2", "1"))).setAlign("right"));
            quantity = s.getParser().getFloatParameter("QTY2", 0.0f);
            total = quantity * 27.99f;
            runningTotal += total;
            tr.addElement(new TD().addElement("$" + total));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("Hewlett-Packard - Pavilion Notebook with Intel� Centrino�"));
            tr.addElement(new TD().addElement("1599.99").setAlign("right"));
            tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY3", s.getParser().getStringParameter("QTY3", "1"))).setAlign("right"));
            quantity = s.getParser().getFloatParameter("QTY3", 0.0f);
            total = quantity * 1599.99f;
            runningTotal += total;
            tr.addElement(new TD().addElement("$" + total));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("3 - Year Performance Service Plan $1000 and Over "));
            tr.addElement(new TD().addElement("299.99").setAlign("right"));

            tr.addElement(new TD().addElement(new Input(Input.TEXT, "QTY4", s.getParser().getStringParameter("QTY4", "1"))).setAlign("right"));
            quantity = s.getParser().getFloatParameter("QTY4", 0.0f);
            total = quantity * 299.99f;
            runningTotal += total;
            tr.addElement(new TD().addElement("$" + total));
            t.addElement(tr);

            ec.addElement(t);

            t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

            if (s.isColor())
            {
                t.setBorder(1);
            }

            ec.addElement(new BR());

            tr = new TR();
            tr.addElement(new TD().addElement("The total charged to your credit card:"));
            tr.addElement(new TD().addElement("$" + runningTotal));

            Input b = new Input();
            b.setType(Input.BUTTON);
            b.setValue("Update Cart");
            b.addAttribute("onclick", "purchase('lessons/Ajax/eval.jsp');");

            tr.addElement(new TD().addElement(b));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("Enter your credit card number:"));
            tr.addElement(new TD()
                    .addElement("<input id='field2' name='field2' type='TEXT' value='4128 3214 0002 1999'>"));
            t.addElement(tr);
            tr = new TR();
            tr.addElement(new TD().addElement("Enter your three digit access code:"));
            tr.addElement(new TD().addElement("<input id='field1' name='field1' type='TEXT' value='123'>"));
            // tr.addElement(new TD().addElement(new Input(Input.TEXT, "field1",param1)));
            t.addElement(tr);

            b = new Input();
            b.setType(Input.BUTTON);
            b.setValue("Purchase");
            b.addAttribute("onclick", "purchase('lessons/Ajax/eval.jsp');");

            tr = new TR();
            tr.addElement(new TD().addElement(b).setColSpan(2).setAlign("right"));
            t.addElement(tr);

            ec.addElement(t);
            ec.addElement(new BR());
            ec.addElement(new HR().setWidth("90%"));

        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }
        return (ec);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected Category getDefaultCategory()
    {
        return Category.AJAX_SECURITY;
    }

    /**
     * Gets the hints attribute of the AccessControlScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("The lesson is similar to the standard reflected cross-site scripting lesson.");
        hints.add("The access code parameter is vulnerable to a reflected cross-site scripting problem.");
        hints.add("The usual &lt;SCRIPT&gt;alert(document.cookie);&lt;/SCRIPT&gt; will not work in this lesson. Why?");
        hints.add("User-supplied data is landing in the Javascript eval() function. Your attack will not require the &lt; and &gt; characters.");
        hints.add("In order to pass this lesson, you must 'alert' the document.cookie.");
        hints.add("Try 123');alert(document.cookie);('");
        return hints;
    }


    // <script type="text/javascript">if ( navigator.appName.indexOf("Microsoft") !=-1) 
    // {var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");xmlHttp.open("TRACE", "./", false); 
    // xmlHttp.send();str1=xmlHttp.responseText;document.write(str1);}</script>
    /**
     * Gets the instructions attribute of the WeakAccessControl object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        String instructions = "For this exercise, your mission is to come up with some input containing a script. You have to try to get this page to reflect that input back to your browser, which will execute the script. In order to pass this lesson, you must 'alert()' document.cookie.";
        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(120);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the AccessControlScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return "Dangerous Use of Eval";
    }

    public Element getCredits()
    {
        return super.getCustomCredits("", ASPECT_LOGO);
    }

    /**
     * Check to see if JSP says they passed the lesson.
     * 
     * @param s
     */
    private void checkSuccess(WebSession s)
    {
        javax.servlet.http.HttpSession session = s.getRequest().getSession();

        if (session.getAttribute(PASSED) != null)
        {
            makeSuccess(s);

            session.removeAttribute(PASSED);
        }
    }
}
