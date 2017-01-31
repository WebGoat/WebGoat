/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
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
 */
package org.owasp.webgoat.plugins;

import com.google.common.primitives.Bytes;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Merges the main message.properties with the plugins WebGoatLabels
 */
public class MessagePropertiesMerger {

    private final File targetDirectory;

    public MessagePropertiesMerger(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @SneakyThrows
    public void mergeAllLanguage() {
        try(Stream<Path> paths = Files.walk(new File(targetDirectory, "plugin/i18n/").toPath())) {
            paths.filter(Files::isRegularFile).forEach(filePath -> merge(filePath));
        }
    }

    @SneakyThrows
    public void merge(Path propertyFile) {
        Properties messageProperties = new Properties();
        String messagePropertyFileName = propertyFile.getFileName().toString().replace("WebGoatLabels", "messages");
        messageProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("i18n/" + messagePropertyFileName));
        preparePropertyFile(propertyFile);
        messageProperties.load(new FileInputStream(propertyFile.toFile()));
        messageProperties.store(new FileOutputStream(new File(Thread.currentThread().getContextClassLoader().getResource("i18n/" + messagePropertyFileName).toURI())), "WebGoat message properties");
    }

    @SneakyThrows
    private void preparePropertyFile(Path propertyFile) {
        byte[] lines = Files.readAllBytes(propertyFile);
        lines = Bytes.concat(lines, System.lineSeparator().getBytes());
        Files.write(propertyFile, lines);
    }
}
