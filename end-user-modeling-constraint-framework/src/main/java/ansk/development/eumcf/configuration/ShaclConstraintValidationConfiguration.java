package ansk.development.eumcf.configuration;

import ansk.development.service.AbstractConstraintValidationService;
import ansk.development.service.ShaclConstraintValidationService;
import ansk.development.service.api.ConstraintValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("shacl")
public class ShaclConstraintValidationConfiguration {
    /**
     * Provides a services to validate constraints.
     *
     * @return {@link AbstractConstraintValidationService}
     */
    @Bean
    public ConstraintValidationService shaclConstraintValidationService() {
        return new ShaclConstraintValidationService();
    }
}
