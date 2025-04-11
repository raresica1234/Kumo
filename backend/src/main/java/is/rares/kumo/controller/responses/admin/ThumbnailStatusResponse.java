package is.rares.kumo.controller.responses.admin;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThumbnailStatusResponse {
    private int totalThumbnailCount;
    private Map<String, Integer> thumbnailCounts;
    private int redisThumbnailEntries;
    private long totalSpaceUsed;
    private Map<String, Long> thumbnailSpace;
}
