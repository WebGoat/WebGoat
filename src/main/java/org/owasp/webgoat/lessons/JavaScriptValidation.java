
package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TextArea;
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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */

public class JavaScriptValidation extends LessonAdapter
{
    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));

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

        // Regular expressions in Java and JavaScript compatible form

        // Note: if you want to use the regex=new RegExp(\"" + regex + "\");" syntax

        // you'll have to use \\\\d to indicate a digit for example -- one escaping for Java and one
        // for JavaScript

        String regex1 = "^[a-z]{3}$";// any three lowercase letters
        String regex2 = "^[0-9]{3}$";// any three digits
        String regex3 = "^[a-zA-Z0-9 ]*$";// alphanumerics and space without punctuation
        String regex4 = "^(one|two|three|four|five|six|seven|eight|nine)$";// enumeration of
        // numbers
        String regex5 = "^\\d{5}$";// simple zip code
        String regex6 = "^\\d{5}(-\\d{4})?$";// zip with optional dash-four
        String regex7 = "^[2-9]\\d{2}-?\\d{3}-?\\d{4}$";// US phone number with or without dashes
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        Pattern pattern3 = Pattern.compile(regex3);
        Pattern pattern4 = Pattern.compile(regex4);
        Pattern pattern5 = Pattern.compile(regex5);
        Pattern pattern6 = Pattern.compile(regex6);
        Pattern pattern7 = Pattern.compile(regex7);
        String lineSep = System.getProperty("line.separator");
        String script = "<SCRIPT>" + lineSep + "regex1=/" + regex1 + "/;" + lineSep + "regex2=/" + regex2 + "/;"
                + lineSep + "regex3=/" + regex3 + "/;" + lineSep + "regex4=/" + regex4 + "/;" + lineSep + "regex5=/"
                + regex5 + "/;" + lineSep + "regex6=/" + regex6 + "/;" + lineSep + "regex7=/" + regex7 + "/;" + lineSep
                + "function validate() { " + lineSep + "msg='JavaScript found form errors'; err=0; " + lineSep
                + "if (!regex1.test(document.form.field1.value)) {err+=1; msg+='\\n  bad field1';}" + lineSep
                + "if (!regex2.test(document.form.field2.value)) {err+=1; msg+='\\n  bad field2';}" + lineSep
                + "if (!regex3.test(document.form.field3.value)) {err+=1; msg+='\\n  bad field3';}" + lineSep
                + "if (!regex4.test(document.form.field4.value)) {err+=1; msg+='\\n  bad field4';}" + lineSep
                + "if (!regex5.test(document.form.field5.value)) {err+=1; msg+='\\n  bad field5';}" + lineSep
                + "if (!regex6.test(document.form.field6.value)) {err+=1; msg+='\\n  bad field6';}" + lineSep
                + "if (!regex7.test(document.form.field7.value)) {err+=1; msg+='\\n  bad field7';}" + lineSep
                + "if ( err > 0 ) alert(msg);" + lineSep + "else document.form.submit();" + lineSep + "} " + lineSep
                + "</SCRIPT>" + lineSep;
        try
        {
            String param1 = s.getParser().getRawParameter("field1", "abc");
            String param2 = s.getParser().getRawParameter("field2", "123");
            String param3 = s.getParser().getRawParameter("field3", "abc 123 ABC");
            String param4 = s.getParser().getRawParameter("field4", "seven");
            String param5 = s.getParser().getRawParameter("field5", "90210");
            String param6 = s.getParser().getRawParameter("field6", "90210-1111");
            String param7 = s.getParser().getRawParameter("field7", "301-604-4882");
            ec.addElement(new StringElement(script));
            TextArea input1 = new TextArea("field1", 1, 25).addElement(param1);
            TextArea input2 = new TextArea("field2", 1, 25).addElement(param2);
            TextArea input3 = new TextArea("field3", 1, 25).addElement(param3);
            TextArea input4 = new TextArea("field4", 1, 25).addElement(param4);
            TextArea input5 = new TextArea("field5", 1, 25).addElement(param5);
            TextArea input6 = new TextArea("field6", 1, 25).addElement(param6);
            TextArea input7 = new TextArea("field7", 1, 25).addElement(param7);

            Input b = new Input();
            b.setType(Input.BUTTON);
            b.setValue("Submit");
            b.addAttribute("onclick", "validate();");
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("3LowerCase")+"("
                    + regex1 + ")")));
            ec.addElement(new Div().addElement(input1));
            ec.addElement(new P());
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("Exactly3Digits")+"(" + regex2 + ")")));
            ec.addElement(new Div().addElement(input2));
            ec.addElement(new P());
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("LettersNumbersSpaceOnly")+"(" + regex3
                    + ")")));
            ec.addElement(new Div().addElement(input3));
            ec.addElement(new P());
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("EnumerationOfNumbers")+" (" + regex4 + ")")));
            ec.addElement(new Div().addElement(input4));
            ec.addElement(new P());
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("SimpleZipCode")+ " (" + regex5 + ")")));
            ec.addElement(new Div().addElement(input5));
            ec.addElement(new P());
            ec.addElement(new Div()
                    .addElement(new StringElement(WebGoatI18N.get("ZIPDashFour")+" (" + regex6 + ")")));
            ec.addElement(new Div().addElement(input6));
            ec.addElement(new P());
            ec.addElement(new Div().addElement(new StringElement(WebGoatI18N.get("USPhoneNumber")+ " ("
                    + regex7 + ")")));
            ec.addElement(new Div().addElement(input7));
            ec.addElement(new P());
            ec.addElement(b);

            // Check the patterns on the server -- and note the errors in the response
            // these should never match unless the client side pattern script doesn't work

            int err = 0;
            String msg = "";

            if (!pattern1.matcher(param1).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+" Field1.";
            }

            if (!pattern2.matcher(param2).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+" Field2.";
            }

            if (!pattern3.matcher(param3).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+"Field3.";
            }

            if (!pattern4.matcher(param4).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+"Field4.";
            }

            if (!pattern5.matcher(param5).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+"Field5.";
            }

            if (!pattern6.matcher(param6).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+"Field6.";
            }

            if (!pattern7.matcher(param7).matches())
            {
                err++;
                msg += "<BR>"+WebGoatI18N.get("ServerSideValidationViolation")+"Field7.";
            }

            if (err > 0)
            {
                s.setMessage(msg);
            }
            if (err >= 7)
            {
                // This means they defeated all the client side checks
                makeSuccess(s);
            }
        }

        catch (Exception e)
        {
            s.setMessage(WebGoatI18N.get("ErrorGenerating") + this.getClass().getName());
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
        return Category.PARAMETER_TAMPERING;
    }

    /**
     * Gets the hints attribute of the AccessControlScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add(WebGoatI18N.get("JavaScriptValidationHint1"));
        hints.add(WebGoatI18N.get("JavaScriptValidationHint2"));
        hints.add(WebGoatI18N.get("JavaScriptValidationHint3"));
        

        return hints;
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
        return ("Bypass Client Side JavaScript Validation");
    }

    public Element getCredits()
    {
        return super.getCustomCredits("", ASPECT_LOGO);
    }
}
