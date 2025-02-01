package is.rares.kumo.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import is.rares.kumo.service.content.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableScheduling
@Slf4j
@RequiredArgsConstructor
@Service
public class JobService {
    private final ThumbnailService thumbnailService;

    @Scheduled(cron = "0 0 0 * * *")
    public void runThumbnailGarbageCollection() {
        log.info("Running midnight job");
        thumbnailService.runGarbageCollection();
    }
}
