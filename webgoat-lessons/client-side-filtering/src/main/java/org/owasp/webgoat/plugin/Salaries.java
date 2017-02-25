package org.owasp.webgoat.plugin;

/**
 *
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.owasp.webgoat.assignments.Endpoint;
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
import java.util.List;
import java.util.Map;

public class Salaries extends Endpoint {

    @Value("${webgoat.user.directory}")
    private String webGoatHomeDirectory;

    @PostConstruct
    @SneakyThrows
    public void copyFiles() {
        ClassPathResource classPathResource = new ClassPathResource("employees.xml");
        File targetDirectory = new File(webGoatHomeDirectory, "/ClientSideFiltering");
        if (!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }
        FileCopyUtils.copy(classPathResource.getInputStream(), new FileOutputStream(new File(targetDirectory, "employees.xml")));
    }

    @RequestMapping(produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> invoke() throws ServletException, IOException {
        NodeList nodes = null;
        File d = new File(webGoatHomeDirectory, "ClientSideFiltering/employees.xml");
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        InputSource inputSource = new InputSource(new FileInputStream(d));

        StringBuffer sb = new StringBuffer();

        sb.append("/Employees/Employee/UserID | ");
        sb.append("/Employees/Employee/FirstName | ");
        sb.append("/Employees/Employee/LastName | ");
        sb.append("/Employees/Employee/SSN | ");
        sb.append("/Employees/Employee/Salary ");

        String expression = sb.toString();

        try {
            nodes = (NodeList) xPath.evaluate(expression, inputSource, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        int COLUMNS = 5;
        List json = Lists.newArrayList();
        java.util.Map<String, Object> employeeJson = Maps.newHashMap();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (i % COLUMNS == 0) {
                employeeJson = Maps.newHashMap();
                json.add(employeeJson);
            }
            Node node = nodes.item(i);
            employeeJson.put(node.getNodeName(), node.getTextContent());
        }
        return json;
    }

    @Override
    public String getPath() {
        return "/clientSideFiltering/salaries";
    }


}
