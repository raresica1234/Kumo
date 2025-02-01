package is.rares.kumo.service;

import org.springframework.stereotype.Service;

import is.rares.kumo.service.content.ThumbnailService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {
    private final ThumbnailService thumbnailService;

    public void runThumbnailGarbageCollection() {
        thumbnailService.runGarbageCollection();
    }
}
