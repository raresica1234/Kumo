package is.rares.kumo.service;

import org.springframework.stereotype.Service;

import is.rares.kumo.service.content.ThumbnailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {
    private final ThumbnailService thumbnailService;

    public void runThumbnailGarbageCollection() {
        thumbnailService.runGarbageCollection();
    }
}
