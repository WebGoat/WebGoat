
package org.owasp.webgoat.lessons;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
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
 * @author Contributed by <a href="http://www.partnet.com">PartNet.</a>
 * 
 */
public class CsrfTokenByPass extends CsrfPromptByPass
{
    protected static final String TRANSFER_FUNDS_PARAMETER = "transferFunds";
    private static final String CSRFTOKEN = "CSRFToken";
    private static final int INVALID_TOKEN = 0;
    private final Random random;
    
    public CsrfTokenByPass(){
        super();
        random = new SecureRandom();
    }
    /**
     * if TRANSFER_FUND_PARAMETER is a parameter, them doTransfer is invoked.  doTranser presents the 
     * web content to confirm and then execute a simulated transfer of funds.  An initial request
     * should have a dollar amount specified.  The amount will be stored and a confirmation form is presented.  
     * The confirmation can be canceled or confirmed.  Confirming the transfer will mark this lesson as completed.
     * 
     * @param s
     * @return Element will appropriate web content for a transfer of funds.
     */
    protected Element doTransfer(WebSession s) {
        String transferFunds = HtmlEncoder.encode(s.getParser().getRawParameter(TRANSFER_FUNDS_PARAMETER, ""));
        String passedInTokenString = HtmlEncoder.encode(s.getParser().getRawParameter(CSRFTOKEN, ""));
        ElementContainer ec = new ElementContainer();
        
        if (transferFunds.length() != 0)
        {   
            HttpSession httpSession = s.getRequest().getSession();
            
            //get tokens to validate
            Integer sessionToken = (Integer) httpSession.getAttribute(CSRFTOKEN);
            Integer passedInToken = s.getParser().getIntParameter(CSRFTOKEN, INVALID_TOKEN);
            
            if (transferFunds.equalsIgnoreCase(TRANSFER_FUNDS_PAGE)){
                
                //generate new random token:
                int token = INVALID_TOKEN;
                while (token == INVALID_TOKEN){
                    token = random.nextInt();
                }
                httpSession.setAttribute(CSRFTOKEN, token);
                
                //present transfer form
                ec.addElement(new H1("Electronic Transfer:"));
                String action = getLink();
                Form form = new Form(action, Form.POST);
                form.addAttribute("id", "transferForm");
                form.addElement( new Input(Input.text, TRANSFER_FUNDS_PARAMETER, "0"));
                form.addElement( new Input(Input.hidden, CSRFTOKEN, token));
                form.addElement( new Input(Input.submit));
                ec.addElement(form);
                //present transfer funds form
                
            } else if (transferFunds.length() > 0 && sessionToken != null && sessionToken.equals(passedInToken)){
                
                //transfer is confirmed
                ec.addElement(new H1("Electronic Transfer Complete"));
                ec.addElement(new StringElement("Amount Transfered: "+transferFunds));
                makeSuccess(s);
                
            } 
            //white space
            ec.addElement(new BR());
            ec.addElement(new BR());
            ec.addElement(new BR());
        }
        return ec;
    }
    
    
    private final static Integer DEFAULT_RANKING = new Integer(123);
    
    @Override
    protected Integer getDefaultRanking()
    {

        return DEFAULT_RANKING;
    }

    @Override
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Add 'transferFunds=main' to the URL and inspect the form that is returned");
        hints.add("The forged request needs both a token and the transfer funds parameter");
        hints.add("Find the token in the page with transferFunds=main. Can you script a way to get the token?");
        
        return hints;
    }

    /**
     * Gets the title attribute of the MessageBoardScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("CSRF Token By-Pass");
    }

    public Element getCredits()
    {
        A partnet = new A("http://www.partnet.com");
        partnet.setPrettyPrint(false);
        partnet.addElement(new StringElement("PART"));
        partnet.addElement(new B().addElement(new StringElement("NET")).setPrettyPrint(false));
        partnet.setStyle("background-color:midnightblue;color:white");
        
        ElementContainer credits = new ElementContainer();
        credits.addElement(new StringElement("Contributed by "));
        credits.addElement(partnet);
        return credits;
    }
}
