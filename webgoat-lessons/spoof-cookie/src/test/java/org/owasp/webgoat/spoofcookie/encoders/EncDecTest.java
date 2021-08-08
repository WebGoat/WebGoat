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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/***
 *
 * @author Angel Olle Blazquez
 *
 */

class EncDecTest {

    @ParameterizedTest
    @DisplayName("Encode test")
    @MethodSource("providedForEncValues")
    void testEncode(String decoded, String encoded) {
        String result = EncDec.encode(decoded);

        assertTrue(result.endsWith(encoded));
    }

    @ParameterizedTest
    @DisplayName("Decode test")
    @MethodSource("providedForDecValues")
    void testDecode(String decoded, String encoded) {
        String result = EncDec.decode(encoded);

        assertThat(decoded, is(result));
    }

    @Test
    @DisplayName("null encode test")
    void testNullEncode() {
        assertNull(EncDec.encode(null));
    }

    @Test
    @DisplayName("null decode test")
    void testNullDecode() {
        assertNull(EncDec.decode(null));
    }

    private static Stream<Arguments> providedForEncValues() {
        return Stream.of(
            Arguments.of("webgoat", "YxNmY2NzYyNjU3Nw=="),
            Arguments.of("admin", "2ZTY5NmQ2NDYx"),
            Arguments.of("tom", "2ZDZmNzQ="));
    }

    private static Stream<Arguments> providedForDecValues() {
        return Stream.of(
            Arguments.of("webgoat", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ3NDYxNmY2NzYyNjU3Nw=="),
            Arguments.of("admin", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ2ZTY5NmQ2NDYx"),
            Arguments.of("tom", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ2ZDZmNzQ="));
    }
}
