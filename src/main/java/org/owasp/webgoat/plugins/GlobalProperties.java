package org.owasp.webgoat.plugins;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class GlobalProperties {

    private final Plugin plugin;

    public GlobalProperties(Path pluginDirectory) {
        this.plugin = new Plugin(pluginDirectory);
    }

    public void loadProperties(Path globalPropertiesPath) {
        try {
            List<Path> filesInDirectory = PluginFileUtils.getFilesInDirectory(globalPropertiesPath);
            this.plugin.loadFiles(filesInDirectory, true);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load global properties, check your installation for the directory i18n: " + globalPropertiesPath.toString(), e);
        }
    }

}
