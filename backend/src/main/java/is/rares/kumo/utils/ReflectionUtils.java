package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
@Slf4j
public class ReflectionUtils {
    public Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            log.error("Class {} from {} not found", className, packageName);
            return null;
        }
    }

    public Set<Class<?>> getAllClassesFromPackageRecursive(String packageName) {
        try (InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"))) {
            if (stream == null) {
                log.error("Stream is null when trying to open {}", packageName);
                return new HashSet<>();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Set<Class<?>> result = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".class"))
                    result.add(getClass(line, packageName));
                else
                    result.addAll(getAllClassesFromPackageRecursive(packageName + "." + line));
            }

            return result;
        } catch (IOException e) {
            log.error("Could not find classes in package {}", packageName);
            return new HashSet<>();
        }
    }
}

