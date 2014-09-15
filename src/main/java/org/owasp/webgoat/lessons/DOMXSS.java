
package org.owasp.webgoat.lessons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Script;
import org.owasp.webgoat.session.*;


public class DOMXSS extends SequentialLessonAdapter
{

    public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
            .addElement(
                        new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
                                .setVspace(0));

    private final static String PERSON = "person";

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        return super.createStagedContent(s);
    }

    protected Element doStage1(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer attackString = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

        ec.addElement(mainContent(s));

        if (attackString.toString().toLowerCase().indexOf("img") != -1
                && attackString.toString().toLowerCase().indexOf("images/logos/owasp.jpg") != -1)
        {
            getLessonTracker(s).setStage(2);
            s.setMessage("Stage 1 completed. ");
        }

        return (ec);
    }

    protected Element doStage2(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer attackString = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

        ec.addElement(mainContent(s));

        if (attackString.toString().toLowerCase().indexOf("img") != -1
                && attackString.toString().toLowerCase().indexOf("onerror") != -1
                && attackString.toString().toLowerCase().indexOf("alert") != -1)
        {
            getLessonTracker(s).setStage(3);
            s.setMessage("Stage 2 completed. ");
        } 
        else
        {
            s.setMessage("Only &lt;img onerror...  attacks are recognized for success criteria");
        }

        return (ec);
    }

    protected Element doStage3(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer attackString = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

        ec.addElement(mainContent(s));

        if (attackString.toString().toLowerCase().indexOf("iframe") != -1
                && attackString.toString().toLowerCase().indexOf("javascript:alert") != -1)
        {
            getLessonTracker(s).setStage(4);
            s.setMessage("Stage 3 completed.");
        } else  if (attackString.toString().toLowerCase().indexOf("iframe") != -1
                && attackString.toString().toLowerCase().indexOf("onload") != -1
                && attackString.toString().toLowerCase().indexOf("alert") != -1)
        {
            getLessonTracker(s).setStage(3);
            s.setMessage("Stage 3 completed. ");
        }
        else
        {
            s.setMessage("Only &lt;iframe javascript/onload...  attacks are recognized for success criteria");
        }
        return (ec);
    }

    protected Element doStage4(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer attackString = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

        ec.addElement(mainContent(s));

        if (attackString.toString().toLowerCase().indexOf("please enter your password:") != -1
                && attackString.toString().toLowerCase().indexOf("javascript:alert") != -1)
        {
            getLessonTracker(s).setStage(5);
            s.setMessage("Stage 4 completed.");
        }

        return (ec);
    }

    protected Element doStage5(WebSession s) throws Exception
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(mainContent(s));

        /**
         * They pass iff:
         * 
         * 1. If the DOMXSS.js file contains the lines "escapeHTML(name)"
         */
        String file = s.getWebResource("lessonJS/DOMXSS.js");
        String content = getFileContent(file);

        if (content.indexOf("escapeHTML(name)") != -1)
        {
            makeSuccess(s);
        }

        return ec;
    }

    protected ElementContainer mainContent(WebSession s)
    {
        StringBuffer attackString = null;

        ElementContainer ec = new ElementContainer();
        try
        {

            ec.addElement(new Script().setSrc("lessonJS/DOMXSS.js"));

            ec.addElement(new Script().setSrc("lessonJS/escape.js"));

            ec.addElement(new H1().setID("greeting"));

            ec.addElement(new StringElement("Enter your name: "));

            attackString = new StringBuffer(s.getParser().getStringParameter(PERSON, ""));

            Input input = new Input(Input.TEXT, PERSON, attackString.toString());
            input.setOnKeyUp("displayGreeting(" + PERSON + ".value)");
            ec.addElement(input);
            ec.addElement(new BR());
            ec.addElement(new BR());

            Element b = ECSFactory.makeButton("Submit Solution");
            ec.addElement(b);
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }
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

        hints.add("Stage 1: Try entering the following: " + "&lt;IMG SRC=\"images/logos/owasp.jpg\"/&gt;");

        hints.add("Stage 2: Try entering the following: " + "&lt;img src=x onerror=;;alert('XSS') /&gt;");

        hints.add("Stage 3: Try entering the following: "
                + "&lt;IFRAME SRC=\"javascript:alert('XSS');\"&gt;&lt;/IFRAME&gt;");

        hints
                .add("Stage 4: Try entering the following: "
                        + "Please enter your password:&lt;BR&gt;&lt;input type = \"password\" name=\"pass\"/&gt;&lt;button "
                        + "onClick=\"javascript:alert('I have your password: ' + pass.value);\"&gt;Submit&lt;/button&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;");

        hints
                .add("Stage 5: You will find the JavaScripts in tomcat\\webapps\\WebGoat\\javascript (Standart Version) or in WebContent\\javascript (Developer Version).");
        // Attack Strings:

        // <IMG SRC="images/logos/owasp.jpg"/>

        // <img src=x onerror=;;alert('XSS') />

        // <IFRAME SRC="javascript:alert('XSS');"></IFRAME>

        // Please enter your password:<BR><input type = "password" name="pass"/><button
        // onClick="javascript:alert('I
        // have your password: ' +
        // pass.value);
        // ">Submit</button><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>

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
        return Category.AJAX_SECURITY;
    }

    /**
     * Gets the title attribute of the HelloScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("LAB: DOM-Based cross-site scripting");
    }

    public String getInstructions(WebSession s)
    {
        String instructions = "";

        if (getLessonTracker(s).getStage() == 1)
        {
            instructions = "STAGE 1:\tFor this exercise, your mission is to deface this website using the image at the following location: <a href = '/WebGoat/images/logos/owasp.jpg'>OWASP IMAGE</a>";
        }
        else if (getLessonTracker(s).getStage() == 2)
        {
            instructions = "STAGE 2:\tNow, try to create a JavaScript alert using the image tag";
        }
        else if (getLessonTracker(s).getStage() == 3)
        {
            instructions = "STAGE 3:\tNext, try to create a JavaScript alert using the IFRAME tag.";
        }
        else if (getLessonTracker(s).getStage() == 4)
        {
            instructions = "STAGE 4:\tUse the following to create a fake login form:<br><br>"
                    + "Please enter your password:&lt;BR&gt;&lt;input type = \"password\" name=\"pass\"/&gt;&lt;button "
                    + "onClick=\"javascript:alert('I have your password: ' + pass.value);\"&gt;Submit&lt;/button&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;&lt;BR&gt;";
        }
        else if (getLessonTracker(s).getStage() == 5)
        {
            instructions = "STAGE 5:\tPerform client-side HTML entity encoding to mitigate the DOM XSS vulnerability. A utility method is provided for you in escape.js.";
        }
        return (instructions);
    }

    private String getFileContent(String content)
    {
        BufferedReader is = null;
        StringBuffer sb = new StringBuffer();

        try
        {
            is = new BufferedReader(new FileReader(new File(content)));
            String s = null;

            while ((s = is.readLine()) != null)
            {
                sb.append(s);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException ioe)
                {

                }
            }
        }

        return sb.toString();
    }

    public Element getCredits()
    {
        return super.getCustomCredits("", ASPECT_LOGO);
    }
}
