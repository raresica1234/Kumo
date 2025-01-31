package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class FileUtils {

    public boolean directoryExists(String path) {
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }


    public boolean createDirectories(String path) {
        if (directoryExists(path))
            return true;

        return new File(path).mkdirs();
    }

    public String getRealPath(String rootPath, String path) {
        if (path.startsWith(rootPath.substring(1))) return "/" + path;
        else return path;
    }
}

