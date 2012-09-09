
package org.owasp.webgoat.lessons;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */

public class Encoding extends LessonAdapter
{
	public final static A ASPECT_LOGO = new A().setHref("http://www.aspectsecurity.com")
			.addElement(
						new IMG("images/logos/aspect.jpg").setAlt("Aspect Security").setBorder(0).setHspace(0)
								.setVspace(0));

	private final static String INPUT = "input";

	private final static String KEY = "key";

	// local encoders

	private static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	private static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

	// encryption constant

	private static byte[] salt = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00 };

	/**
	 * Returns the base 64 decoding of a string.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */

	public static String base64Decode(String str) throws IOException
	{

		byte[] b = decoder.decodeBuffer(str);

		return (new String(b));
	}

	/**
	 * Description of the Method
	 * 
	 * @param c
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception IOException
	 *                Description of the Exception
	 */

	public static String base64Decode(char[] c) throws IOException
	{

		return base64Decode(new String(c));
	}

	/**
	 * Description of the Method
	 * 
	 * @param c
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String base64Encode(char[] c)
	{

		return base64Encode(new String(c));
	}

	/**
	 * Returns the base 64 encoding of a string.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String base64Encode(String str)
	{

		byte[] b = str.getBytes();

		return (encoder.encode(b));
	}

	/**
	 * Description of the Method
	 * 
	 * @param b
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String base64Encode(byte[] b)
	{

		return (encoder.encode(b));
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

			String userInput = s.getParser().getRawParameter(INPUT, "");

			String userKey = s.getParser().getStringParameter(KEY, "");

			Table table = new Table();

			TR tr = new TR();

			tr.addElement(new TD("Enter a string: "));

			Input input = new Input(Input.TEXT, INPUT, userInput);

			tr.addElement(new TD().addElement(input));

			table.addElement(tr);

			tr = new TR();

			tr.addElement(new TD("Enter a password (optional): "));

			Input key = new Input(Input.TEXT, KEY, userKey);

			tr.addElement(new TD().addElement(key));

			table.addElement(tr);

			tr = new TR();

			Element b = ECSFactory.makeButton("Go!");

			tr.addElement(new TD().setAlign("center").setColSpan(2).addElement(b));

			table.addElement(tr);

			ec.addElement(table);

			ec.addElement(new P());

			Table t = new Table();

			t.setWidth("100%");

			t.setBorder(0);

			t.setCellSpacing(1);

			t.setCellPadding(4);

			String description;

			t.addElement(makeTitleRow("Description", "Encoded", "Decoded"));

			description = "Base64 encoding is a simple reversable encoding used to encode bytes into ASCII characters. Useful for making bytes into a printable string, but provides no security.";

			// t.addElement( makeDescriptionRow( description ) );
			t.addElement(makeRow(description, base64Encode(userInput), base64Decode(userInput)));
			// t.addElement( makeSpacerRow() );

			description = "Entity encoding uses special sequences like &amp;amp; for special characters. This prevents these characters from being interpreted by most interpreters.";

			t.addElement(makeRow(description, HtmlEncoder.encode(userInput), HtmlEncoder.decode(userInput)));

			description = "Password based encryption (PBE) is strong encryption with a text password. Cannot be decrypted without the password";

			t.addElement(makeRow(description, encryptString(userInput, userKey), decryptString(userInput, userKey)));
			description = "MD5 hash is a checksum that can be used to validate a string or byte array, but cannot be reversed to find the original string or bytes. For obscure cryptographic reasons, it is better to use SHA-256 if you have a choice.";

			t.addElement(makeRow(description, hashMD5(userInput), "Cannot reverse a hash"));

			description = "SHA-256 hash is a checksum that can be used to validate a string or byte array, but cannot be reversed to find the original string or bytes.";

			t.addElement(makeRow(description, hashSHA(userInput), "N/A"));

			description = "Unicode encoding is...";

			t.addElement(makeRow(description, "Not Implemented", "Not Implemented"));

			description = "URL encoding is...";

			t.addElement(makeRow(description, urlEncode(userInput), urlDecode(userInput)));

			description = "Hex encoding simply encodes bytes into %xx format.";

			t.addElement(makeRow(description, hexEncode(userInput), hexDecode(userInput)));

			description = "Rot13 encoding is a way to make text unreadable, but is easily reversed and provides no security.";

			t.addElement(makeRow(description, rot13(userInput), rot13(userInput)));

			description = "XOR with password encoding is a weak encryption scheme that mixes a password into data.";

			t.addElement(makeRow(description, xorEncode(userInput, userKey), xorDecode(userInput, userKey)));

			description = "Double unicode encoding is...";

			t.addElement(makeRow(description, "Not Implemented", "Not Implemented"));

			description = "Double URL encoding is...";

			t.addElement(makeRow(description, urlEncode(urlEncode(userInput)), urlDecode(urlDecode(userInput))));

			ec.addElement(t);

		}

		catch (Exception e)
		{

			s.setMessage("Error generating " + this.getClass().getName());

			e.printStackTrace();

		}

		if (getLessonTracker(s).getNumVisits() > 3)
		{
			makeSuccess(s);
		}

		return (ec);
	}

	/**
	 * Convenience method for encrypting a string.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @param pw
	 *            Description of the Parameter
	 * @return String the encrypted string.
	 */

	public static synchronized String decryptString(String str, String pw)
	{

		try
		{

			PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);

			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			Cipher passwordDecryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");

			char[] pass = pw.toCharArray();

			SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

			passwordDecryptCipher.init(Cipher.DECRYPT_MODE, k, ps);

			byte[] dec = decoder.decodeBuffer(str);

			byte[] utf8 = passwordDecryptCipher.doFinal(dec);

			return new String(utf8, "UTF-8");
		}

		catch (Exception e)
		{

			return ("This is not an encrypted string");
		}

	}

	/**
	 * Convenience method for encrypting a string.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @param pw
	 *            Description of the Parameter
	 * @return String the encrypted string.
	 * @exception SecurityException
	 *                Description of the Exception
	 */

	public static synchronized String encryptString(String str, String pw) throws SecurityException
	{

		try
		{

			PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);

			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			Cipher passwordEncryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");

			char[] pass = pw.toCharArray();

			SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

			passwordEncryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);

			byte[] utf8 = str.getBytes("UTF-8");

			byte[] enc = passwordEncryptCipher.doFinal(utf8);

			return encoder.encode(enc);
		}

		catch (Exception e)
		{

			return ("Encryption error");
		}

	}

	/**
	 * Gets the category attribute of the Encoding object
	 * 
	 * @return The category value
	 */

	protected Category getDefaultCategory()
	{
		return Category.INSECURE_STORAGE;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */

	public List<String> getHints(WebSession s)
	{

		List<String> hints = new ArrayList<String>();
		hints.add("Enter a string and press 'go'");
		hints.add("Enter 'abc' and notice the rot13 encoding is 'nop' ( increase each letter by 13 characters ).");
		hints.add("Enter 'a c' and notice the url encoding is 'a+c' ( ' ' is converted to '+' ).");
		return hints;
	}

	/**
	 * Gets the instructions attribute of the Encoding object
	 * 
	 * @return The instructions value
	 */

	public String getInstructions(WebSession s)
	{
		return "This lesson will familiarize the user with different encoding schemes.  ";
	}

	private final static Integer DEFAULT_RANKING = new Integer(15);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 * Gets the title attribute of the HelloScreen object
	 * 
	 * @return The title value
	 */

	public String getTitle()
	{
		return ("Encoding Basics");
	}

	/**
	 * Returns the MD5 hash of a String.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String hashMD5(String str)
	{

		byte[] b = str.getBytes();
		MessageDigest md = null;

		try
		{
			md = MessageDigest.getInstance("MD5");
			md.update(b);
		} catch (NoSuchAlgorithmException e)
		{
			// it's got to be there
			e.printStackTrace();
		}
		return (base64Encode(md.digest()));
	}

	/**
	 * Returns the SHA hash of a String.
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String hashSHA(String str)
	{
		byte[] b = str.getBytes();
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA-256");
			md.update(b);
		} catch (NoSuchAlgorithmException e)
		{
			// it's got to be there
			e.printStackTrace();
		}
		return (base64Encode(md.digest()));
	}

	/**
	 * Description of the Method
	 * 
	 * @param hexString
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String hexDecode(String hexString)
	{
		try
		{
			if ((hexString.length() % 3) != 0) { return ("String not comprised of Hex digit pairs."); }
			char[] chars = new char[hexString.length()];
			char[] convChars = new char[hexString.length() / 3];
			hexString.getChars(0, hexString.length(), chars, 0);
			for (int i = 1; i < hexString.length(); i += 3)
			{
				String hexToken = new String(chars, i, 2);
				convChars[i / 3] = (char) Integer.parseInt(hexToken, 16);
			}
			return new String(convChars);
		} catch (NumberFormatException nfe)
		{
			return ("String not comprised of Hex digits");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param asciiString
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String hexEncode(String asciiString)
	{
		char[] ascii = new char[asciiString.length()];
		asciiString.getChars(0, asciiString.length(), ascii, 0);
		StringBuffer hexBuff = new StringBuffer();
		for (int i = 0; i < asciiString.length(); i++)
		{
			hexBuff.append("%");
			hexBuff.append(Integer.toHexString(ascii[i]));
		}
		return hexBuff.toString().toUpperCase();
	}

	/**
	 * The main program for the Encoding class
	 * 
	 * @param args
	 *            The command line arguments
	 */

	public static void main(String[] args)
	{
		try
		{
			String userInput = args[0];
			String userKey = args[1];
			System.out.println("Working with: " + userInput);
			System.out.print("Base64 encoding: ");
			System.out.println(base64Encode(userInput) + " : " + base64Decode(userInput));
			System.out.print("Entity encoding: ");
			System.out.println(HtmlEncoder.encode(userInput) + " : " + HtmlEncoder.decode(userInput));
			System.out.print("Password based encryption (PBE): ");
			System.out.println(encryptString(userInput, userKey) + " : " + decryptString(userInput, userKey));
			System.out.print("MD5 hash: ");
			System.out.println(hashMD5(userInput) + " : " + "Cannot reverse a hash");
			System.out.print("SHA-256 hash: ");
			System.out.println(hashSHA(userInput) + " : " + "Cannot reverse a hash");
			System.out.print("Unicode encoding: ");
			System.out.println("Not Implemented" + " : " + "Not Implemented");
			System.out.print("URL encoding: ");
			System.out.println(urlEncode(userInput) + " : " + urlDecode(userInput));
			System.out.print("Hex encoding: ");
			System.out.println(hexEncode(userInput) + " : " + hexDecode(userInput));
			System.out.print("Rot13 encoding: ");
			System.out.println(rot13(userInput) + " : " + rot13(userInput));
			System.out.print("XOR with password: ");
			System.out.println(xorEncode(userInput, userKey) + " : " + xorDecode(userInput, userKey));
			System.out.print("Double unicode encoding is...");
			System.out.println("Not Implemented" + " : " + "Not Implemented");
			System.out.print("Double URL encoding: ");
			System.out.println(urlEncode(urlEncode(userInput)) + " : " + urlDecode(urlDecode(userInput)));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param value1
	 *            Description of the Parameter
	 * @param value2
	 *            Description of the Parameter
	 * @param description
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	private TR makeRow(String description, String value1, String value2)
	{

		TD desc = new TD().addElement(description).setBgColor("#bbbbbb");
		TD val1 = new TD()
				.addElement(new Div().addElement(value1).setStyle("overflow:auto; height:60px; width:100px;"))
				.setBgColor("#dddddd");
		TD val2 = new TD()
				.addElement(new Div().addElement(value2).setStyle("overflow:auto; height:60px; width:100px;"))
				.setBgColor("#dddddd");
		TR tr = new TR();

		tr.addElement(desc);
		tr.addElement(val1);
		tr.addElement(val2);

		return tr;
	}

	/**
	 * Description of the Method
	 * 
	 * @param value1
	 *            Description of the Parameter
	 * @param value2
	 *            Description of the Parameter
	 * @param description
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	private TR makeTitleRow(String description, String value1, String value2)
	{
		TD desc = new TD().addElement(new B().addElement(description));
		TD val1 = new TD().addElement(new B().addElement(value1));
		TD val2 = new TD().addElement(new B().addElement(value2));
		desc.setAlign("center");
		val1.setAlign("center");
		val2.setAlign("center");
		TR tr = new TR();
		tr.addElement(desc);
		tr.addElement(val1);
		tr.addElement(val2);
		return (tr);
	}

	/**
	 * Description of the Method
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static synchronized String rot13(String input)
	{
		StringBuffer output = new StringBuffer();
		if (input != null)
		{
			for (int i = 0; i < input.length(); i++)
			{
				char inChar = input.charAt(i);
				if ((inChar >= 'A') & (inChar <= 'Z'))
				{
					inChar += 13;
					if (inChar > 'Z')
					{
						inChar -= 26;
					}
				}
				if ((inChar >= 'a') & (inChar <= 'z'))
				{
					inChar += 13;
					if (inChar > 'z')
					{
						inChar -= 26;
					}
				}
				output.append(inChar);
			}
		}
		return output.toString();
	}

	/**
	 * Description of the Method
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String unicodeDecode(String str)
	{
		// FIXME: TOTALLY EXPERIMENTAL

		try
		{
			ByteBuffer bbuf = ByteBuffer.allocate(str.length());
			bbuf.put(str.getBytes());
			Charset charset = Charset.forName("ISO-8859-1");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer cbuf = decoder.decode(bbuf);
			return (cbuf.toString());
		} catch (Exception e)
		{
			return ("Encoding problem");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String unicodeEncode(String str)
	{
		// FIXME: TOTALLY EXPERIMENTAL
		try
		{
			Charset charset = Charset.forName("ISO-8859-1");
			CharsetEncoder encoder = charset.newEncoder();
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(str));
			return (new String(bbuf.array()));
		} catch (Exception e)
		{
			return ("Encoding problem");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String urlDecode(String str)
	{
		try
		{
			return (URLDecoder.decode(str, "UTF-8"));
		} catch (Exception e)
		{
			return ("Decoding error");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param str
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static String urlEncode(String str)
	{
		try
		{
			return (URLEncoder.encode(str, "UTF-8"));
		} catch (Exception e)
		{
			return ("Encoding error");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @param userKey
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static synchronized char[] xor(String input, String userKey)
	{
		if ((userKey == null) || (userKey.trim().length() == 0))
		{
			userKey = "Goober";
		}
		char[] xorChars = userKey.toCharArray();
		int keyLen = xorChars.length;
		char[] inputChars = null;
		char[] outputChars = null;
		if (input != null)
		{
			inputChars = input.toCharArray();
			outputChars = new char[inputChars.length];
			for (int i = 0; i < inputChars.length; i++)
			{
				outputChars[i] = (char) (inputChars[i] ^ xorChars[i % keyLen]);
			}
		}
		return outputChars;
	}

	/**
	 * Description of the Method
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @param userKey
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static synchronized String xorDecode(String input, String userKey)
	{
		try
		{
			String decoded = base64Decode(input);
			return new String(xor(decoded, userKey));
		} catch (Exception e)
		{
			return "String not XOR encoded.";
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @param userKey
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */

	public static synchronized String xorEncode(String input, String userKey)
	{
		return base64Encode(xor(input, userKey));
	}

	public Element getCredits()
	{
		return super.getCustomCredits("", ASPECT_LOGO);
	}
}
