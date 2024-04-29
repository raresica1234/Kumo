package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class StringUtils {
    private final SecureRandom randomInstance = new SecureRandom();

    public String generateRandomString(int length) {
        int leftLimit = 48; // '0'
        int rightLimit = 90; // 'Z'

        return randomInstance.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && i <= 90)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
