package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class StringUtils {
    private final Random randomInstance = new Random();

    // TODO: Use secure random
    public String generateRandomString(int length) {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'

        return randomInstance.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
