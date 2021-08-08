/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.spoofcookie.encoders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Hex;

/***
 *
 * @author Angel Olle Blazquez
 *
 */

public class EncDec {

    // PoC: weak encoding method

    private static final String SALT = RandomStringUtils.randomAlphabetic(10);

    private EncDec() {

    }

    public static String encode(final String value) {
        if (value == null) {
            return null;
        }

        String encoded = value.toLowerCase() + SALT;
        encoded = revert(encoded);
        encoded = hexEncode(encoded);
        return base64Encode(encoded);
    }

    public static String decode(final String encodedValue) throws IllegalArgumentException {
        if (encodedValue == null) {
            return null;
        }

        String decoded = base64Decode(encodedValue);
        decoded = hexDecode(decoded);
        decoded = revert(decoded);
        return decoded.substring(0, decoded.length() - SALT.length());
    }

    private static String revert(final String value) {
        return new StringBuilder(value)
            .reverse()
            .toString();
    }

    private static String hexEncode(final String value) {
        char[] encoded = Hex.encode(value.getBytes(StandardCharsets.UTF_8));
        return new String(encoded);
    }

    private static String hexDecode(final String value) {
        byte[] decoded = Hex.decode(value);
        return new String(decoded);
    }

    private static String base64Encode(final String value) {
        return Base64
            .getEncoder()
            .encodeToString(value.getBytes());
    }

    private static String base64Decode(final String value) {
        byte[] decoded = Base64
            .getDecoder()
            .decode(value.getBytes());
        return new String(decoded);
    }

}
