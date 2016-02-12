
package org.owasp.webgoat.util;

import java.util.HashMap;
import java.util.Map;


/**
 *************************************************************************************************
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
 * @version $Id: $Id
 * @author dm
 */
public class HtmlEncoder
{

	static Map<String, Integer> e2i = new HashMap<String, Integer>();

	static Map<Integer, String> i2e = new HashMap<Integer, String>();

	// html entity list
	private static Object[][] entities = { { "quot", new Integer(34) }, // " - double-quote
			{ "amp", new Integer(38) }, //  - ampersand
			{ "lt", new Integer(60) }, //  - less-than
			{ "gt", new Integer(62) }, //  - greater-than
			{ "nbsp", new Integer(160) }, // non-breaking space
			{ "copy", new Integer(169) }, //  - copyright
			{ "reg", new Integer(174) }, // - registered trademark
			{ "Agrave", new Integer(192) }, //  - uppercase A, grave accent
			{ "Aacute", new Integer(193) }, //  - uppercase A, acute accent
			{ "Acirc", new Integer(194) }, //  - uppercase A, circumflex accent
			{ "Atilde", new Integer(195) }, //  - uppercase A, tilde
			{ "Auml", new Integer(196) }, //  - uppercase A, umlaut
			{ "Aring", new Integer(197) }, //  - uppercase A, ring
			{ "AElig", new Integer(198) }, //  - uppercase AE
			{ "Ccedil", new Integer(199) }, //  - uppercase C, cedilla
			{ "Egrave", new Integer(200) }, //  - uppercase E, grave accent
			{ "Eacute", new Integer(201) }, //  - uppercase E, acute accent
			{ "Ecirc", new Integer(202) }, //  - uppercase E, circumflex accent
			{ "Euml", new Integer(203) }, //  - uppercase E, umlaut
			{ "Igrave", new Integer(204) }, //  - uppercase I, grave accent
			{ "Iacute", new Integer(205) }, //  - uppercase I, acute accent
			{ "Icirc", new Integer(206) }, //  - uppercase I, circumflex accent
			{ "Iuml", new Integer(207) }, //  - uppercase I, umlaut
			{ "ETH", new Integer(208) }, //  - uppercase Eth, Icelandic
			{ "Ntilde", new Integer(209) }, //  - uppercase N, tilde
			{ "Ograve", new Integer(210) }, //  - uppercase O, grave accent
			{ "Oacute", new Integer(211) }, //  - uppercase O, acute accent
			{ "Ocirc", new Integer(212) }, //  - uppercase O, circumflex accent
			{ "Otilde", new Integer(213) }, //  - uppercase O, tilde
			{ "Ouml", new Integer(214) }, //  - uppercase O, umlaut
			{ "Oslash", new Integer(216) }, //  - uppercase O, slash
			{ "Ugrave", new Integer(217) }, //  - uppercase U, grave accent
			{ "Uacute", new Integer(218) }, //  - uppercase U, acute accent
			{ "Ucirc", new Integer(219) }, //  - uppercase U, circumflex accent
			{ "Uuml", new Integer(220) }, //  - uppercase U, umlaut
			{ "Yacute", new Integer(221) }, //  - uppercase Y, acute accent
			{ "THORN", new Integer(222) }, //  - uppercase THORN, Icelandic
			{ "szlig", new Integer(223) }, //  - lowercase sharps, German
			{ "agrave", new Integer(224) }, //  - lowercase a, grave accent
			{ "aacute", new Integer(225) }, //  - lowercase a, acute accent
			{ "acirc", new Integer(226) }, //  - lowercase a, circumflex accent
			{ "atilde", new Integer(227) }, //  - lowercase a, tilde
			{ "auml", new Integer(228) }, //  - lowercase a, umlaut
			{ "aring", new Integer(229) }, //  - lowercase a, ring
			{ "aelig", new Integer(230) }, //  - lowercase ae
			{ "ccedil", new Integer(231) }, //  - lowercase c, cedilla
			{ "egrave", new Integer(232) }, //  - lowercase e, grave accent
			{ "eacute", new Integer(233) }, //  - lowercase e, acute accent
			{ "ecirc", new Integer(234) }, //  - lowercase e, circumflex accent
			{ "euml", new Integer(235) }, //  - lowercase e, umlaut
			{ "igrave", new Integer(236) }, //  - lowercase i, grave accent
			{ "iacute", new Integer(237) }, //  - lowercase i, acute accent
			{ "icirc", new Integer(238) }, //  - lowercase i, circumflex accent
			{ "iuml", new Integer(239) }, //  - lowercase i, umlaut
			{ "igrave", new Integer(236) }, //  - lowercase i, grave accent
			{ "iacute", new Integer(237) }, //  - lowercase i, acute accent
			{ "icirc", new Integer(238) }, //  - lowercase i, circumflex accent
			{ "iuml", new Integer(239) }, //  - lowercase i, umlaut
			{ "eth", new Integer(240) }, //  - lowercase eth, Icelandic
			{ "ntilde", new Integer(241) }, //  - lowercase n, tilde
			{ "ograve", new Integer(242) }, //  - lowercase o, grave accent
			{ "oacute", new Integer(243) }, //  - lowercase o, acute accent
			{ "ocirc", new Integer(244) }, //  - lowercase o, circumflex accent
			{ "otilde", new Integer(245) }, //  - lowercase o, tilde
			{ "ouml", new Integer(246) }, //  - lowercase o, umlaut
			{ "oslash", new Integer(248) }, //  - lowercase o, slash
			{ "ugrave", new Integer(249) }, //  - lowercase u, grave accent
			{ "uacute", new Integer(250) }, //  - lowercase u, acute accent
			{ "ucirc", new Integer(251) }, //  - lowercase u, circumflex accent
			{ "uuml", new Integer(252) }, //  - lowercase u, umlaut
			{ "yacute", new Integer(253) }, //  - lowercase y, acute accent
			{ "thorn", new Integer(254) }, //  - lowercase thorn, Icelandic
			{ "yuml", new Integer(255) }, //  - lowercase y, umlaut
			{ "euro", new Integer(8364) },// Euro symbol
	};

	/**
	 * Initialises the mappings between entities and characters
	 */
	static {
	    for (int i = 0; i < entities.length; i++)
            e2i.put((String) entities[i][0], (Integer) entities[i][1]);
        for (int i = 0; i < entities.length; i++)
            i2e.put((Integer) entities[i][1], (String) entities[i][0]);
	}

	/**
	 * Turns funky characters into HTML entity equivalents
	 *
	 * e.g. {@code "bread" & "butter"} = {@code &amp;quot;bread&amp;quot; &amp;amp;
	 *  &amp;quot;butter&amp;quot;}. Update: supports nearly all HTML entities, including funky
	 * accents. See the source code for more detail. Adapted from
	 * http://www.purpletech.com/code/src/com/purpletech/util/Utils.java.
	 *
	 * @param s1
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String encode(String s1)
	{
		StringBuffer buf = new StringBuffer();

		int i;
		for (i = 0; i < s1.length(); ++i)
		{
			char ch = s1.charAt(i);

			String entity = i2e.get(new Integer((int) ch));

			if (entity == null)
			{
				if (((int) ch) > 128)
				{
					buf.append("&#" + ((int) ch) + ";");
				}
				else
				{
					buf.append(ch);
				}
			}
			else
			{
				buf.append("&" + entity + ";");
			}
		}

		return buf.toString();
	}

	/**
	 * Given a string containing entity escapes, returns a string containing the actual Unicode
	 * characters corresponding to the escapes. Adapted from
	 * http://www.purpletech.com/code/src/com/purpletech/util/Utils.java.
	 *
	 * @param s1
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String decode(String s1)
	{
		StringBuffer buf = new StringBuffer();

		int i;
		for (i = 0; i < s1.length(); ++i)
		{
			char ch = s1.charAt(i);

			if (ch == '&')
			{
				int semi = s1.indexOf(';', i + 1);
				if (semi == -1)
				{
					buf.append(ch);
					continue;
				}
				String entity = s1.substring(i + 1, semi);
				Integer iso;
				if (entity.charAt(0) == '#')
				{
					iso = new Integer(entity.substring(1));
				}
				else
				{
					iso = e2i.get(entity);
				}
				if (iso == null)
				{
					buf.append("&" + entity + ";");
				}
				else
				{
					buf.append((char) (iso.intValue()));
				}
				i = semi;
			}
			else
			{
				buf.append(ch);
			}
		}

		return buf.toString();
	}
}
