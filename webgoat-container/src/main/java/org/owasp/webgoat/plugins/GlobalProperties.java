package org.owasp.webgoat.plugins;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public final class GlobalProperties {

    private final Path pluginDirectory;

    public GlobalProperties(Path pluginDirectory) {
        this.pluginDirectory = Objects.requireNonNull(pluginDirectory, "pluginDirectory cannot be null");
    }

    public void loadProperties(Path globalPropertiesPath) {
        try {
            PluginFileUtils.createDirsIfNotExists(pluginDirectory);
            List<Path> filesInDirectory = PluginFileUtils.getFilesInDirectory(globalPropertiesPath);
            new Plugin(pluginDirectory).loadFiles(filesInDirectory, true);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load global properties, check your installation for the directory i18n: " + globalPropertiesPath.toString(), e);
        }
    }

}
