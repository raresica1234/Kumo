package is.rares.kumo.controller.responses.admin;

import java.util.Map;

import is.rares.kumo.domain.content.ThumbnailSizeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThumbnailStatusResponse {
    private int totalThumbnailCount;
    private Map<ThumbnailSizeEnum, Integer> thumbnailCounts;
    private int redisThumbnailEntries;
    private long totalSpaceUsed;
    private Map<ThumbnailSizeEnum, Long> thumbnailSpace;
}
