package org.owasp.webgoat;

import com.github.ryenus.rop.OptionParser;
import com.github.ryenus.rop.OptionParser.Option;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.AbstractProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;

import static com.github.ryenus.rop.OptionParser.Command;

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
 * @author WebGoat
 * @version $Id: $Id
 * @since July 24, 2016
 */
@Command(name = "webgoat", descriptions = "Starting WebGoat")
public class Main {

    private final Logger logger = LoggerFactory.getLogger(Main.class);

    @Option(opt = {"-p", "--port"}, description = "HTTP port to use")
    int port = 6047;

    @Option(opt = {"-a", "--address"}, description = "Server address to use")
    String address = "localhost";

    void run() throws Exception {
        String webappDirLocation = "webgoat-container/src/main/webapp/";
        Tomcat tomcat = new Tomcat();
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/WebGoat", new File(webappDirLocation).getAbsolutePath());

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(port);

        if (connector.getProtocolHandler() instanceof AbstractProtocol) {
            AbstractProtocol<?> protocol = (AbstractProtocol<?>) connector.getProtocolHandler();
            protocol.setAddress(InetAddress.getByName(address));
            protocol.setPort(port);
        }
        tomcat.getService().addConnector(connector);
        tomcat.start();
        logger.info("Browse to http://{}:{}/WebGoat and happy hacking!", address, port);
        tomcat.getServer().await();
    }

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser(Main.class);
        parser.parse(args);
    }
}