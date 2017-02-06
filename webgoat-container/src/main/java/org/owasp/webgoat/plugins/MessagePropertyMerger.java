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

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.google.common.io.Files.createParentDirs;

/**
 * Merges the main message.properties with the plugins WebGoatLabels
 */
public class MessagePropertyMerger {

    private final File targetDirectory;

    public MessagePropertyMerger(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @SneakyThrows
    public void merge(ZipFile zipFile, ZipEntry zipEntry) {
        Properties messageProperties = new Properties();
        try (InputStream zis = zipFile.getInputStream(zipEntry)) {
            messageProperties.load(zis);
        }

        Properties messagesFromHome = new Properties();
        File pluginMessageFiles = new File(targetDirectory, zipEntry.getName());
        if (pluginMessageFiles.exists()) {
            try (FileInputStream fis = new FileInputStream(pluginMessageFiles)) {
                messagesFromHome.load(fis);
            }
        }

        messageProperties.putAll(messagesFromHome);

        createParentDirs(pluginMessageFiles);
        try (FileOutputStream fos = new FileOutputStream(pluginMessageFiles)) {
            messageProperties.store(fos, "Plugin message properties");
        }
    }
}
