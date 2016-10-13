package org.owasp.webgoat.plugin;

/**
 *
 */

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.LessonEndpoint;
import org.owasp.webgoat.lessons.LessonEndpointMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@LessonEndpointMapping
public class Salaries extends LessonEndpoint {

    @RequestMapping(method = RequestMethod.GET)
    public void invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        NodeList nodes = null;
        File d = new File(getPluginDirectory(), "ClientSideFiltering/html/employees.xml");
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
            nodes = (NodeList) xPath.evaluate(expression, inputSource,
                    XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        int nodesLength = nodes.getLength();


        TR tr;

        int COLUMNS = 5;

        Table t2 = null;
        if (nodesLength > 0) {
            t2 = new Table().setCellSpacing(0).setCellPadding(0)
                    .setBorder(1).setWidth("90%").setAlign("center");
            tr = new TR();
            tr.addElement(new TD().addElement("UserID"));
            tr.addElement(new TD().addElement("First Name"));
            tr.addElement(new TD().addElement("Last Name"));
            tr.addElement(new TD().addElement("SSN"));
            tr.addElement(new TD().addElement("Salary"));
            t2.addElement(tr);
        }

        tr = new TR();

        for (int i = 0; i < nodesLength; i++) {
            Node node = nodes.item(i);

            if (i % COLUMNS == 0) {
                tr = new TR();
                tr.setID(node.getTextContent());
                //tr.setStyle("display: none");
            }

            tr.addElement(new TD().addElement(node.getTextContent()));

            if (i % COLUMNS == (COLUMNS - 1)) {
                t2.addElement(tr);
            }
        }

        if (t2 != null) {
            resp.getWriter().println(t2.toString());
        } else {
            resp.getWriter().println("No Results");
        }
    }

    @Override
    public String getPath() {
        return "/clientSideFiltering/salaries";
    }


}
