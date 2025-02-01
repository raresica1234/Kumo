package is.rares.kumo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public List<String> getFileNamesInDirectoryRecursive(List<String> result, Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (path.toFile().isDirectory()) {
                    getFileNamesInDirectoryRecursive(result, path);
                } else {
                    result.add(path.getFileName().toString());
                }
            }
        }
        return result;
    }

    public boolean removeDirectoryIfEmpty(String path) {
        Path dir = Paths.get(path);

        try (Stream<Path> entries = Files.list(dir)) {
            if (entries.findAny().isEmpty()) {
                Files.delete(dir);
                return true;
            }
        } catch (IOException e) {
            log.debug("Could not remove directory {}", path, e);
        }

        return false;
    }
}

