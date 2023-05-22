package is.rares.kumo.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
