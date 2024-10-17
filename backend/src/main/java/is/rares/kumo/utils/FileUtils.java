package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class FileUtils {

    public boolean directoryExists(String path) {
        return Files.exists(Paths.get(path));
    }
}

