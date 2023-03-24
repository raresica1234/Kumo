package is.rares.kumo.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import is.rares.kumo.KumoApplication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = KumoApplication.class)
public class ArchitectureTests {
    @ArchTest
    public static final ArchRule controllersShouldNotAccessDomainObjects = noClasses()
            .that().resideInAnyPackage("..controller..")
            .should().dependOnClassesThat().resideInAnyPackage("..domain..");

    @ArchTest
    public static final ArchRule methodsAnnotatedWithRequestBodyShouldHaveValid = methods()
            .that().areAnnotatedWith(RequestBody.class)
            .should().beAnnotatedWith(Valid.class);
}
