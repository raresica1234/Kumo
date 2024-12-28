package is.rares.kumo.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@UtilityClass
@Slf4j
public class ReflectionUtils {
    public Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            log.error("Class {} from {} not found", className, packageName);
            return null;
        }
    }

    public Set<Class<?>> findClassesAnnotatedWith(String packageName, Class<? extends Annotation> annotation)
    {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotation));

        Set<Class<?>> classes = new HashSet<>();

        Set<BeanDefinition> beanDefs = provider.findCandidateComponents(packageName);
        beanDefs.stream()
                .map(BeanDefinition::getBeanClassName)
                .map(name -> {
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        log.error("Class {} not found", name);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .forEach(classes::add);
        return classes;
    }
}

