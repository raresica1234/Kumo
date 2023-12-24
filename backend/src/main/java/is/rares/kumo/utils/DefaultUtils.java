package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Paths;

@UtilityClass
public class DefaultUtils {
    public <T> T firstOrNull(T[] elements) {
        if (elements == null)
            return null;
        if (elements.length >= 1)
            return elements[0];
        return null;
    }

    public String combineNullablePaths(String path1, String path2) {
        if (path1 != null && path2 != null)
            return Paths.get(path1, path2).toString();
        if (path1 != null)
            return path1;
        if (path2 != null)
            return path2;
        return null;
    }
}
