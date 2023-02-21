package ansk.development.eumcf.configuration;

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

/**
 * Encapsulates all necessary class definition for Modicio.
 * This showcase project uses Modicio as a domain layer for entity modeling and managing at runtime
 */
@Configuration
public class ModicioConfiguration {

    /**
     * See {@link DefinitionVerifier}.
     *
     * @return {@link DefinitionVerifier}
     */
    @Bean
    public DefinitionVerifier definitionVerifier() {
        return new SimpleDefinitionVerifier();
    }

    /**
     * See {@link  ModelVerifier}.
     *
     * @return {@link ModelVerifier}
     */
    @Bean
    public ModelVerifier modelVerifier() {
        return new SimpleModelVerifier();
    }

    /**
     * See {@link TypeFactory}.
     *
     * @param definitionVerifier {@link DefinitionVerifier}
     * @param modelVerifier      {@link ModelVerifier}
     * @return {@link TypeFactory}
     */
    @Bean
    public TypeFactory typeFactory(DefinitionVerifier definitionVerifier, ModelVerifier modelVerifier) {
        return new TypeFactory(definitionVerifier, modelVerifier);
    }

    /**
     * See {@link InstanceFactory}.
     *
     * @param definitionVerifier {@link DefinitionVerifier}
     * @param modelVerifier      {@link ModelVerifier}
     * @return {@link InstanceFactory}
     */
    @Bean
    public InstanceFactory instanceFactory(DefinitionVerifier definitionVerifier, ModelVerifier modelVerifier) {
        return new InstanceFactory(definitionVerifier, modelVerifier);
    }

    /**
     * See {@link Registry}.
     *
     * @param typeFactory     {@link TypeFactory}
     * @param instanceFactory {@link InstanceFactory}
     * @return {@link Registry}
     */
    @Bean
    public Registry registry(TypeFactory typeFactory, InstanceFactory instanceFactory) {
        SimpleMapRegistry registry = new SimpleMapRegistry(typeFactory, instanceFactory);
        typeFactory.setRegistry(registry);
        instanceFactory.setRegistry(registry);
        return registry;
    }
}
