package anton.skripin.development.eumcf;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ViolationLevel;
import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import anton.skripin.development.domain.constraint.functions.LogicalFunction;
import anton.skripin.development.domain.constraint.functions.StringBasedFunction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import modicio.core.ModelElement;
import modicio.core.TimeIdentity;
import modicio.core.TypeHandle;
import modicio.core.api.*;
import modicio.core.rules.api.AttributeRuleJ;
import modicio.nativelang.defaults.api.SimpleDefinitionVerifierJ;
import modicio.nativelang.defaults.api.SimpleMapRegistryJ;
import modicio.nativelang.defaults.api.SimpleModelVerifierJ;
import modicio.verification.api.DefinitionVerifierJ;
import modicio.verification.api.ModelVerifierJ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.Option;
import scala.concurrent.Future;
import scala.jdk.javaapi.FutureConverters;
import scala.jdk.javaapi.OptionConverters;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class EndUserModelingConstraintFrameworkApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(EndUserModelingConstraintFrameworkApplication.class, args);
    }

}
