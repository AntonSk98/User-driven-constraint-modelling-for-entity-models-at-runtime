package anton.skripin.development.eumcf.configuration;

import modicio.core.api.InstanceFactoryJ;
import modicio.core.api.RegistryJ;
import modicio.core.api.TypeFactoryJ;
import modicio.nativelang.defaults.api.SimpleDefinitionVerifierJ;
import modicio.nativelang.defaults.api.SimpleMapRegistryJ;
import modicio.nativelang.defaults.api.SimpleModelVerifierJ;
import modicio.verification.api.DefinitionVerifierJ;
import modicio.verification.api.ModelVerifierJ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModicioSetup {
    @Bean
    public DefinitionVerifierJ definitionVerifierJ() {
        return new SimpleDefinitionVerifierJ();
    }

    @Bean
    public ModelVerifierJ modelVerifierJ() {
        return new SimpleModelVerifierJ();
    }

    @Bean
    public TypeFactoryJ typeFactoryJ(DefinitionVerifierJ definitionVerifierJ, ModelVerifierJ modelVerifierJ) {
        return new TypeFactoryJ(definitionVerifierJ, modelVerifierJ);
    }

    @Bean
    public InstanceFactoryJ instanceFactoryJ(DefinitionVerifierJ definitionVerifierJ, ModelVerifierJ modelVerifierJ) {
        return new InstanceFactoryJ(definitionVerifierJ, modelVerifierJ);
    }

    @Bean
    public RegistryJ registryJ(TypeFactoryJ typeFactoryJ, InstanceFactoryJ instanceFactoryJ) {
        SimpleMapRegistryJ registryJ = new SimpleMapRegistryJ(typeFactoryJ, instanceFactoryJ);
        typeFactoryJ.setRegistryJ(registryJ);
        instanceFactoryJ.setRegistryJ(registryJ);
        return registryJ;
    }
}
