package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultUtils {
    public <T> T firstOrNull(T[] elements) {
        if (elements == null)
            return null;
        if (elements.length >= 1)
            return elements[0];
        return null;
    }

    public String combineNullableURLs(String path1, String path2) {
        if (path1 != null && path2 != null)
            return combineTwoPaths(path1, path2);
        if (path1 != null)
            return path1;
        return path2;
    }

    public String combineTwoPaths(String path1, String path2) {
        if (!path1.endsWith("/"))
            path1 = path1.concat("/");
        if (path2.startsWith("/"))
            path2 = path2.substring(1);

        return path1 + path2;
    }

}
