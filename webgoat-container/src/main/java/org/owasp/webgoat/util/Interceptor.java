/**
 * 
 */

package org.owasp.webgoat.util;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.net.Socket;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


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
 * @author sherif koussa - Macadamian Technologies
 * 
 */
public class Interceptor implements Filter
{

	private static final String OSG_SERVER_NAME = "OSGServerName";

	private static final String OSG_SERVER_PORT = "OSGServerPort";

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{

		HttpServletRequest req = (HttpServletRequest) request;

		Socket osgSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String osgServerName = req.getSession().getServletContext().getInitParameter(OSG_SERVER_NAME);
		String osgServerPort = req.getSession().getServletContext().getInitParameter(OSG_SERVER_PORT);

		try
		{
			// If these parameters are not defined then no communication will happen with OSG
			if (osgServerName != null && osgServerName.length() != 0 && osgServerPort != null
					&& osgServerPort.length() != 0)
			{
				osgSocket = new Socket(osgServerName, Integer.parseInt(osgServerPort));
				if (osgSocket != null)
				{
					out = new PrintWriter(osgSocket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(osgSocket.getInputStream()));
					// String message =
					// "HTTPRECEIVEHTTPREQUEST,-,DataValidation_SqlInjection_Basic.aspx";
					// out.println(message);

					// System.out.println(in.readLine());
				}
			}

		} catch (UnknownHostException e)
		{
			e.printStackTrace();

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (out != null)
			{
				out.close();
			}
			if (in != null)
			{
				in.close();
			}
			if (osgSocket != null)
			{
				osgSocket.close();
			}
		}

		String url = req.getRequestURL().toString();

		RequestDispatcher disp = req.getRequestDispatcher(url.substring(url.lastIndexOf(req.getContextPath() + "/")
				+ req.getContextPath().length()));

		disp.forward(request, response);

	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}
