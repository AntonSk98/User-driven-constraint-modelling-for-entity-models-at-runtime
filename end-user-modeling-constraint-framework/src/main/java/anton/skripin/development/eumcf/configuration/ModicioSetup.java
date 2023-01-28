package anton.skripin.development.eumcf.configuration;

import modicio.core.InstanceFactory;
import modicio.core.Registry;
import modicio.core.TypeFactory;
import modicio.nativelang.defaults.SimpleDefinitionVerifier;
import modicio.nativelang.defaults.SimpleMapRegistry;
import modicio.nativelang.defaults.SimpleModelVerifier;
import modicio.verification.DefinitionVerifier;
import modicio.verification.ModelVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModicioSetup {
    @Bean
    public DefinitionVerifier definitionVerifier() {
        return new SimpleDefinitionVerifier();
    }

    @Bean
    public ModelVerifier modelVerifier() {
        return new SimpleModelVerifier();
    }

    @Bean
    public TypeFactory typeFactory(DefinitionVerifier definitionVerifier, ModelVerifier modelVerifier) {
        return new TypeFactory(definitionVerifier, modelVerifier);
    }

    @Bean
    public InstanceFactory instanceFactory(DefinitionVerifier definitionVerifier, ModelVerifier modelVerifier) {
        return new InstanceFactory(definitionVerifier, modelVerifier);
    }

    @Bean
    public Registry registry(TypeFactory typeFactory, InstanceFactory instanceFactory) {
        SimpleMapRegistry registry = new SimpleMapRegistry(typeFactory, instanceFactory);
        typeFactory.setRegistry(registry);
        instanceFactory.setRegistry(registry);
        return registry;
    }
}
