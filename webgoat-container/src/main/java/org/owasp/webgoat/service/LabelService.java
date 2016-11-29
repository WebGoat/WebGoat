/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 */
package org.owasp.webgoat.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.i18n.LabelProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;


/**
 * <p>LabelService class.</p>
 *
 * @author zupzup
 */

@RestController
@Slf4j
@AllArgsConstructor
public class LabelService {

    public static final String URL_LABELS_MVC = "/service/labels.mvc";
    private final LabelProvider labelProvider;

    /**
     * Fetches labels for given language
     * If no language is provided, the language is determined from the request headers
     * Otherwise, fall back to default language
     *
     * @param lang the language to fetch labels for (optional)
     * @return a map of labels
     * @throws Exception
     */
    @GetMapping(path = URL_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, String>> fetchLabels(@RequestParam(value = "lang", required = false) String lang, HttpServletRequest request) {
        Locale locale;
        if (StringUtils.isEmpty(lang)) {
            log.debug("No language provided, determining from request headers");
            locale = request.getLocale();
            if (locale != null) {
                log.debug("Locale set to {}", locale);
            }
        } else {
            locale = Locale.forLanguageTag(lang);
            log.debug("Language provided: {} leads to Locale: {}", lang, locale);
        }
        return new ResponseEntity<>(labelProvider.getLabels(locale), HttpStatus.OK);
    }
}
