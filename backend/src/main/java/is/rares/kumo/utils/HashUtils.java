package is.rares.kumo.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.springframework.data.redis.core.script.DigestUtils;

import jakarta.xml.bind.annotation.adapters.HexBinaryAdapter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HashUtils {

    public String hashString(String value) {
        return DigestUtils.sha1DigestAsHex(value);
    }

    public String hashFileContent(String path) throws Exception {
        File file = new File(path);
        if (!file.exists())
            throw new FileNotFoundException(path);

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        try (InputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest()).toLowerCase();
        }
    }
}
