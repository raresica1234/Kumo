package is.rares.kumo.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import is.rares.kumo.KumoApplication;
import is.rares.kumo.security.annotation.Authenticated;
import is.rares.kumo.security.annotation.HasTokenType;
import is.rares.kumo.security.annotation.NotAuthenticated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
    packagesOf = KumoApplication.class,
    importOptions = {
      ImportOption.DoNotIncludeTests.class,
      ImportOption.DoNotIncludeArchives.class,
      ImportOption.DoNotIncludeJars.class
    })
public class ControllerArchitectureTests {
  @ArchTest
  public static final ArchRule controllersShouldNotAccessDomainObjects =
      noClasses()
          .that()
          .resideInAPackage("..controller..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..kumo..domain..");

  private static final ArchCondition<JavaMethod> methodAnnotatedWithRequestBodyShouldBeValid =
      new ArchCondition<>("Methods annotated with @RequestBody should be annotated with @Valid") {
        @Override
        public void check(JavaMethod item, ConditionEvents events) {
          System.out.println(item.getFullName());

          for (var parameter : item.getParameters())
            if (parameter.isAnnotatedWith(RequestBody.class)
                && !parameter.isAnnotatedWith(Valid.class))
              events.add(
                  SimpleConditionEvent.violated(
                      parameter,
                      "Parameters annotated with @RequestBody should be annotated with @Valid \n"
                          + item));
        }
      };

  @ArchTest
  public static final ArchRule methodsAnnotatedWithRequestBodyShouldHaveValid =
      methods()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage("..controller..")
          .and()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .should(methodAnnotatedWithRequestBodyShouldBeValid);

  @ArchTest
  public static final ArchRule
      controllerMethodShouldBeAnnotatedWithEitherAuthenticatedOrNotAuthenticated =
          methods()
              .that()
              .areDeclaredInClassesThat()
              .resideInAPackage("..controller..")
              .and()
              .areDeclaredInClassesThat()
              .areAnnotatedWith(RestController.class)
              .should()
              .beAnnotatedWith(Authenticated.class)
              .orShould()
              .beAnnotatedWith(NotAuthenticated.class);

  @ArchTest
  public static final ArchRule
      methodsThatAreAnnotatedWithAuthenticatedShouldHaveTokenTypeAnnotation =
          methods()
              .that()
              .areDeclaredInClassesThat()
              .resideInAPackage("..controller..")
              .and()
              .areDeclaredInClassesThat()
              .areAnnotatedWith(RestController.class)
              .and()
              .areAnnotatedWith(Authenticated.class)
              .should()
              .beAnnotatedWith(HasTokenType.class);
}
