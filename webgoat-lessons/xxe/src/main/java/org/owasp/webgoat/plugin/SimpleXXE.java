package org.owasp.webgoat.plugin;

import org.apache.commons.exec.OS;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

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
 * @since November 17, 2016
 */
public class SimpleXXE extends Assignment {

    private final static String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "opt", "var"};
    private final static String[] DEFAULT_WINDOWS_DIRECTORIES = {"Windows", "Program Files (x86)", "Program Files"};

    @Override
    public String getPath() {
        return "XXE/simple";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult createNewUser(@RequestBody String userInfo) throws Exception {
        User user = parseXml(userInfo);
        if (checkSolution(user)) {
          return AttackResult.success(String.format("Congratulation, welcome %s", user.getUsername()));
        }
        return AttackResult.failed("Try again!");
    }

    public static User parseXml(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(User.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (User) unmarshaller.unmarshal(xsr);
    }

    public static boolean checkSolution(User userInfo) {
        String[] directoriesToCheck = OS.isFamilyUnix() ? DEFAULT_LINUX_DIRECTORIES : DEFAULT_WINDOWS_DIRECTORIES;
        boolean success = true;
        for (String directory : directoriesToCheck) {
            success &= userInfo.getUsername().contains(directory);
        }
        return success;
    }


}
