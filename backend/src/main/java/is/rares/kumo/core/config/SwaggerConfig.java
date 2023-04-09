package is.rares.kumo.core.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//public class SwaggerConfig implements WebMvcConfigurer {
//    public static final String CONTROLLER_PACKAGE = "is.rares.kumo.controller";

//    @Bean
//    public GroupedOpenApi api() {
//        return GroupedOpenApi.builder()
//                .group("api")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
//                .pathsToMatch("/api/**")
//                .build();
//    }
//}
