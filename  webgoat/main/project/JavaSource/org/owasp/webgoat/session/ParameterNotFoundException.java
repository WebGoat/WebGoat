package org.owasp.webgoat.session;

/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of
 *  the Open Web Application Security Project (http://www.owasp.org) This
 *  software package org.owasp.webgoat.is published by OWASP under the GPL. You should read and
 *  accept the LICENSE before you use, modify and/or redistribute this software.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 */
public class ParameterNotFoundException extends Exception
{
    /**
     *  Constructs a new ParameterNotFoundException with no detail message.
     */
    public ParameterNotFoundException()
    {
        super();
    }


    /**
     *  Constructs a new ParameterNotFoundException with the specified detail
     *  message.
     *
     *@param  s  the detail message
     */
    public ParameterNotFoundException(String s)
    {
        super(s);
    }
}
