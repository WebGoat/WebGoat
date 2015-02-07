package org.owasp.webgoat.plugins;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class GlobalPropertiesTest {

    private Path tempDirectory;

    @Before
    public void createTmpDir() throws IOException {
        tempDirectory = Files.createTempDirectory(GlobalPropertiesTest.class.getSimpleName());
        tempDirectory.toFile().deleteOnExit();
    }

    @Test
    public void propertyFilesShouldBeLoaded() throws IOException {
        Path pluginDirectory = Files.createDirectory(Paths.get(tempDirectory.toString(), "plugins"));
        Path directory = Files.createDirectory(Paths.get(tempDirectory.toString(), "i18n"));
        Path globalProperties = Files.createFile(Paths.get(directory.toString(), "global.properties"));
        Files.write(globalProperties, Arrays.asList("test=label for test"), StandardCharsets.UTF_8);
        new GlobalProperties(pluginDirectory).loadProperties(directory);

        ClassLoader propertyFilesClassLoader =
            ResourceBundleClassLoader.createPropertyFilesClassLoader(this.getClass().getClassLoader());
        assertNotNull(propertyFilesClassLoader.getResourceAsStream("global.properties"));
    }

    @Test(expected = IllegalStateException.class)
    public void propertyFilesDirectoryNotFoundShouldRaiseError() throws IOException {
        Path pluginDirectory = Files.createDirectory(Paths.get(tempDirectory.toString(), "plugins"));
        Path directory = Files.createDirectory(Paths.get(tempDirectory.toString(), "i18n"));
        Files.delete(directory);

        new GlobalProperties(pluginDirectory).loadProperties(directory);
    }

}