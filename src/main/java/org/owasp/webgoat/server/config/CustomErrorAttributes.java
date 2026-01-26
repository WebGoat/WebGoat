/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.server.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest,
            ErrorAttributeOptions options
    ) {
        Map<String, Object> errorAttributes =
                super.getErrorAttributes(webRequest, options);

        errorAttributes.put("webgoatVersion", "WebGoat");
        errorAttributes.put("javaVersion", System.getProperty("java.version"));
        errorAttributes.put("osName", System.getProperty("os.name"));
        errorAttributes.put("osVersion", System.getProperty("os.version"));

        return errorAttributes;
    }
}
