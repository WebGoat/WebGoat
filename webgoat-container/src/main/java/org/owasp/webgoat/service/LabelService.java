/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 */
package org.owasp.webgoat.service;

import org.owasp.webgoat.i18n.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;


/**
 * <p>LabelService class.</p>
 *
 * @author zupzup
 */

@Controller
public class LabelService {

    private static final String URL_LABELS_MVC = "/service/labels.mvc";

    private static final Logger logger = LoggerFactory.getLogger(LabelService.class);

    @Autowired
    private LabelProvider labelProvider;

    /**
     * Fetches labels for given language
     * If no language is provided, the language is determined from the request headers
     * Otherwise, fall back to default language
     *
     * @param lang the language to fetch labels for (optional)
     * @return a map of labels
     * @throws Exception
     */
    @RequestMapping(path = URL_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Map<String, String>> fetchLabels(@RequestParam(value = "lang", required = false) String lang, HttpServletRequest request) throws Exception {
        Locale locale;
        if (StringUtils.isEmpty(lang)) {
            logger.debug("No language provided, determining from request headers");
            locale = request.getLocale();
            if (locale != null) {
                logger.debug("Locale set to {}", locale);
            }
        } else {
            locale = Locale.forLanguageTag(lang);
            logger.debug("Language provided: {} leads to Locale: {}", lang, locale);
        }
        return new ResponseEntity<>(labelProvider.getLabels(locale), HttpStatus.OK);
    }
}
