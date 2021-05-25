/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
 * Source for this application is maintained at
 * https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.session.LabelDebugger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>LabelDebugService class.</p>
 *
 * @author nbaars
 * @version $Id: $Id
 */
@Controller
@Slf4j
@AllArgsConstructor
public class LabelDebugService {

    private static final String URL_DEBUG_LABELS_MVC = "/service/debug/labels.mvc";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_SUCCESS = "success";

    private LabelDebugger labelDebugger;

    /**
     * Checks if debugging of labels is enabled or disabled
     *
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @RequestMapping(path = URL_DEBUG_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> checkDebuggingStatus() {
        log.debug("Checking label debugging, it is {}", labelDebugger.isEnabled());
        Map<String, Object> result = createResponse(labelDebugger.isEnabled());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Sets the enabled flag on the label debugger to the given parameter
     * @param enabled {@link org.owasp.webgoat.session.LabelDebugger} object
     * @throws Exception unhandled exception
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @RequestMapping(value = URL_DEBUG_LABELS_MVC, produces = MediaType.APPLICATION_JSON_VALUE, params = KEY_ENABLED)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> setDebuggingStatus(@RequestParam("enabled") Boolean enabled) throws Exception {
        log.debug("Setting label debugging to {} ", labelDebugger.isEnabled());
        Map<String, Object> result = createResponse(enabled);
        labelDebugger.setEnabled(enabled);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * @param enabled {@link org.owasp.webgoat.session.LabelDebugger} object
     * @return a {@link java.util.Map} object.
     */
    private Map<String, Object> createResponse(Boolean enabled) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(KEY_SUCCESS, Boolean.TRUE);
        result.put(KEY_ENABLED, enabled);
        return result;
    }
}
