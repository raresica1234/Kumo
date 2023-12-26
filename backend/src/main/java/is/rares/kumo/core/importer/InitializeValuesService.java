package is.rares.kumo.core.importer;

import org.springframework.stereotype.Service;

@Service
public class InitializeValuesService {

    private final OwnerImporterService ownerImporterService;

    public InitializeValuesService(OwnerImporterService ownerImporterService) {
        this.ownerImporterService = ownerImporterService;
    }

    public void initializeValues() {
        ownerImporterService.registerOwner();
    }


}
