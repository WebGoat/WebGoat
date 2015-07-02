package org.owasp.webgoat.plugins;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PluginTestHelper {

    private static Path tempDirectory;

    public static Path createTmpDir() throws IOException {
        tempDirectory = Files.createTempDirectory(PluginTestHelper.class.getSimpleName());
        tempDirectory.toFile().deleteOnExit();
        return tempDirectory;
    }

    public static Path pathForLoading() throws IOException, URISyntaxException {
        Path path = Paths.get(PluginTestHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return Paths.get(path.toString(), "org/owasp/webgoat/plugins");
    }

//    public static Plugin createPluginFor(Class pluginClass) throws Exception {
//        Path pluginTargetPath = Files.createDirectory(Paths.get(tempDirectory.toString(), "pluginTargetPath"));
//        Map<String, byte[]> classes = new HashMap<>();
//        classes.put(pluginClass.getName(), Files.readAllBytes(Paths.get(pathForLoading().toString(), pluginClass.getSimpleName() + ".class")));
//        Plugin plugin = new Plugin(pluginTargetPath, classes);
//        return plugin;
//    }
}
