package is.rares.kumo.architecture;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import is.rares.kumo.KumoApplication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = KumoApplication.class, importOptions = {
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeArchives.class,
        ImportOption.DoNotIncludeJars.class
})
public class ArchitectureTests {
    @ArchTest
    public static final ArchRule controllersShouldNotAccessDomainObjects = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..domain..");

    private static final ArchCondition<JavaMethod> methodAnnotatedWithRequestBodyShouldBeValid =
            new ArchCondition<>("Methods annotated with @RequestBody should be annotated with @Valid") {
                @Override
                public void check(JavaMethod item, ConditionEvents events) {
                    System.out.println(item.getFullName());

                    for (var parameter : item.getParameters())
                        if (parameter.isAnnotatedWith(RequestBody.class) && !parameter.isAnnotatedWith(Valid.class))
                            events.add(SimpleConditionEvent.violated(parameter, "Parameters annotated with @RequestBody should be annotated with @Valid \n" + item));
                }
            };

    @ArchTest
    public static final ArchRule methodsAnnotatedWithRequestBodyShouldHaveValid = methods()
            .that().areDeclaredInClassesThat().resideInAPackage("..controller..")
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should(methodAnnotatedWithRequestBodyShouldBeValid);

}
