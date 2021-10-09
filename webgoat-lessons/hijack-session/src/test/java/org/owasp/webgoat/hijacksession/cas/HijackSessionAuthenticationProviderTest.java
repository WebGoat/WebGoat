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
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.hijacksession.cas;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.owasp.webgoat.hijacksession.cas.Authentication.AuthenticationBuilder;

/***
 *
 * @author Angel Olle Blazquez
 *
 */

class HijackSessionAuthenticationProviderTest {

    HijackSessionAuthenticationProvider provider = new HijackSessionAuthenticationProvider();

    @ParameterizedTest
    @DisplayName("Provider authentication test")
    @MethodSource("authenticationForCookieValues")
    void testProviderAuthenticationGeneratesCookie(Authentication authentication) {
        Authentication auth = provider.authenticate(authentication);
        assertThat(auth.getId(), not(StringUtils.isEmpty(auth.getId())));
    }

    @Test
    void testAuthenticated() {
        String id = "anyId";
        provider.addSession(id);

        Authentication auth = provider.authenticate(Authentication.builder().id(id).build());

        assertThat(auth.getId(), is(id));
        assertThat(auth.isAuthenticated(), is(true));

        auth = provider.authenticate(Authentication.builder().id("otherId").build());

        assertThat(auth.getId(), is("otherId"));
        assertThat(auth.isAuthenticated(), is(false));
    }

    @Test
    void testAuthenticationToString() {
        AuthenticationBuilder authBuilder = Authentication.builder()
            .name("expectedName")
            .credentials("expectedCredentials")
            .id("expectedId");

        Authentication auth = authBuilder.build();

        String expected = "Authentication.AuthenticationBuilder("
                + "name=" + auth.getName()
                + ", credentials=" + auth.getCredentials()
                + ", id=" + auth.getId() + ")";

        assertThat(authBuilder.toString(), is(expected));

        expected = "Authentication(authenticated=" + auth.isAuthenticated()
                + ", name=" + auth.getName()
                + ", credentials=" + auth.getCredentials()
                + ", id=" + auth.getId() + ")";

        assertThat(auth.toString(), is(expected));

    }

    @Test
    void testMaxSessions() {
        for (int i = 0; i <= HijackSessionAuthenticationProvider.MAX_SESSIONS + 1; i++) {
            provider.authorizedUserAutoLogin();
            provider.addSession(null);
        }

        assertThat(provider.getSessionsSize(), is(HijackSessionAuthenticationProvider.MAX_SESSIONS));
    }

    private static Stream<Arguments> authenticationForCookieValues() {
        return Stream.of(
            Arguments.of((Object) null),
            Arguments.of(Authentication.builder().name("any").credentials("any").build()),
            Arguments.of(Authentication.builder().id("any").build()));
    }

}
