package is.rares.kumo.core.importer;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InitializeValuesService {

    private final OwnerImporterService ownerImporterService;


    public void initializeValues() {
        ownerImporterService.registerOwner();
    }


}
