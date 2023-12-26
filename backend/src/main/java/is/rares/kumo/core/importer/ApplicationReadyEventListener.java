package is.rares.kumo.core.importer;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private final InitializeValuesService initializeValuesService;

    public ApplicationReadyEventListener(InitializeValuesService initializeValuesService) {
        this.initializeValuesService = initializeValuesService;
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        initializeValuesService.initializeValues();
    }
}
