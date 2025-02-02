package is.rares.kumo.service;

import org.springframework.stereotype.Service;

import is.rares.kumo.controller.responses.admin.ThumbnailStatusResponse;
import is.rares.kumo.service.content.ThumbnailService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {
    private final ThumbnailService thumbnailService;

    public void runThumbnailGarbageCollection() {
        thumbnailService.runGarbageCollection();
    }

    public ThumbnailStatusResponse getThumbnailStatus() {
        return thumbnailService.getThumbnailStatus();
    }
}
