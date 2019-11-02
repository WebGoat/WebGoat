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

package org.owasp.webgoat.client_side_filtering;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Salaries { // {extends Endpoint {

    @Value("${webgoat.user.directory}")
    private String webGoatHomeDirectory;

    @PostConstruct
    public void copyFiles() {
        ClassPathResource classPathResource = new ClassPathResource("employees.xml");
        File targetDirectory = new File(webGoatHomeDirectory, "/ClientSideFiltering");
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        try {
            FileCopyUtils.copy(classPathResource.getInputStream(), new FileOutputStream(new File(targetDirectory, "employees.xml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> invoke() throws ServletException, IOException {
        NodeList nodes = null;
        File d = new File(webGoatHomeDirectory, "ClientSideFiltering/employees.xml");
        XPathFactory factory = XPathFactory.newInstance();
        XPath path = factory.newXPath();
        InputSource inputSource = new InputSource(new FileInputStream(d));

        StringBuffer sb = new StringBuffer();

        sb.append("/Employees/Employee/UserID | ");
        sb.append("/Employees/Employee/FirstName | ");
        sb.append("/Employees/Employee/LastName | ");
        sb.append("/Employees/Employee/SSN | ");
        sb.append("/Employees/Employee/Salary ");

        String expression = sb.toString();

        try {
            nodes = (NodeList) path.evaluate(expression, inputSource, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        int columns = 5;
        List json = new ArrayList();
        java.util.Map<String, Object> employeeJson = new HashMap<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (i % columns == 0) {
                employeeJson = new HashMap<>();
                json.add(employeeJson);
            }
            Node node = nodes.item(i);
            employeeJson.put(node.getNodeName(), node.getTextContent());
        }
        return json;
    }
}
