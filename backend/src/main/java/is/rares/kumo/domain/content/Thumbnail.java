package is.rares.kumo.domain.content;

import lombok.Data;

@Data
public class Thumbnail {
    String path;
    String contentHash;
    long lastModifiedTimestamp; 
    int size;
    boolean originalImage = false;
}
