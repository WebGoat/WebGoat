package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PluginClassLoader extends ClassLoader {

    private final List<Class<?>> classes = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(Plugin.class);

    public Class<?> loadClass(String nameOfClass, byte[] classFile) {
        Class<?> clazz = defineClass(nameOfClass, classFile, 0, classFile.length);
        classes.add(clazz);
        return clazz;
    }

    public PluginClassLoader(ClassLoader contextClassLoader) {
        super(contextClassLoader);
    }

    public Class findClass(final String name) throws ClassNotFoundException {
        logger.debug("Finding class " + name);
        Optional<Class<?>> foundClass = FluentIterable.from(classes)
                .firstMatch(new Predicate<Class<?>>() {
                    @Override
                    public boolean apply(Class<?> clazz) {
                        return clazz.getName().equals(name);
                    }
                });
        if (foundClass.isPresent()) {
            return foundClass.get();
        }
        throw new ClassNotFoundException("Class " + name + " not found");
    }

}

