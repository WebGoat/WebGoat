/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
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

package org.owasp.webgoat.lessons.deserialization;

import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;

@RestController
@AssignmentHints({"insecure-deserialization.hints.1", "insecure-deserialization.hints.2", "insecure-deserialization.hints.3"})
public class InsecureDeserializationTask extends AssignmentEndpoint {

    @PostMapping("/InsecureDeserialization/task")
    @ResponseBody
    public AttackResult completed(@RequestParam String token) throws IOException {
        String b64token;
        long before;
        long after;
        int delay;

        b64token = token.replace('-', '+').replace('_', '/');

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(b64token)))) {
            before = System.currentTimeMillis();
            Object o = ois.readObject();
            if (!(o instanceof VulnerableTaskHolder)) {
                if (o instanceof String) {
                    return failed(this).feedback("insecure-deserialization.stringobject").build();
                }
                return failed(this).feedback("insecure-deserialization.wrongobject").build();
            }
            after = System.currentTimeMillis();
        } catch (InvalidClassException e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        } catch (IllegalArgumentException e) {
            return failed(this).feedback("insecure-deserialization.expired").build();
        } catch (Exception e) {
            return failed(this).feedback("insecure-deserialization.invalidversion").build();
        }

        delay = (int) (after - before);
        if (delay > 7000) {
            return failed(this).build();
        }
        if (delay < 3000) {
            return failed(this).build();
        }
        return success(this).build();
    }
}
