package is.rares.kumo.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import is.rares.kumo.KumoApplication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packagesOf = KumoApplication.class, importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeArchives.class,
        ImportOption.DoNotIncludeJars.class
})
public class ArchitectureTests {
    private static final String PACKAGE = "is.rares.kumo";
    @ArchTest
    public static final ArchRule controllersShouldNotAccessDomainObjects = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..domain..");

    @ArchTest
    public static final ArchRule methodsAnnotatedWithRequestBodyShouldHaveValid = methods()
            .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
            .and().areAnnotatedWith(RequestBody.class)
            .should().beAnnotatedWith(Valid.class);
}
