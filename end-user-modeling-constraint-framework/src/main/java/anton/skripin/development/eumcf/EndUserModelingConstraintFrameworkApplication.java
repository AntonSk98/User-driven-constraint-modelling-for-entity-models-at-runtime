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
        testModicio();
//        SpringApplication.run(EndUserModelingConstraintFrameworkApplication.class, args);
    }

    @SneakyThrows
    public static void testModicio() {
        DefinitionVerifierJ definitionVerifierJ = new SimpleDefinitionVerifierJ();
        ModelVerifierJ modelVerifierJ = new SimpleModelVerifierJ();
        TypeFactoryJ typeFactoryJ = new TypeFactoryJ(definitionVerifierJ, modelVerifierJ);
        InstanceFactoryJ instanceFactoryJ = new InstanceFactoryJ(definitionVerifierJ, modelVerifierJ);
        RegistryJ registryJ = new SimpleMapRegistryJ(typeFactoryJ, instanceFactoryJ);

        typeFactoryJ.setRegistryJ(registryJ);
        instanceFactoryJ.setRegistryJ(registryJ);

        { // create root element
            CompletableFuture<TypeHandleJ> root = typeFactoryJ.newTypeJ(
                    ModelElement.ROOT_NAME(),
                    ModelElement.REFERENCE_IDENTITY(),
                    true,
                    Optional.of(TimeIdentity.create()));
            CompletableFuture<Object> typeFuture = registryJ.setType(root.get());
            typeFuture.get();
        }

        String projectName = "Project";
        { // create Project model element with attributes
            CompletableFuture<TypeHandleJ> type = typeFactoryJ.newTypeJ(projectName, ModelElement.REFERENCE_IDENTITY(), false);
            // enforce synchronous execution
            registryJ.setType(type.get()).get();
            TypeHandle typeHandle = toCompletableFuture(registryJ.getRegistry().getType(projectName, ModelElement.REFERENCE_IDENTITY())).get().get();
            toCompletableFuture(typeHandle.unfold()).get();
            typeHandle.applyRule(AttributeRuleJ.create("name", "String", false, Option.empty()));
            typeHandle.applyRule(AttributeRuleJ.create("age", "String", false, Option.empty()));
            typeHandle.applyRule(AttributeRuleJ.create("salary", "String", false, Option.empty()));
        }

        {
            // create Instance of a Project
            String createdInstanceId = instanceFactoryJ.newInstanceJ(projectName).get().getInstanceIdJ();
            DeepInstanceJ deepInstance = registryJ.getJ(createdInstanceId).get().get();
            deepInstance.unfoldJ().get();
            boolean isNameAttrFound = deepInstance.assignDeepValue("name", "Parent attribute - will be saved");
            boolean isResponsibleAttrFound = deepInstance.assignDeepValue("responsible", "Local attribute - will not be saved");
            boolean isStateAttrFound = deepInstance.assignDeepValue("started", "Local attribute - will not be saved");
            deepInstance.commitJ().get();

            System.out.println("Attribute values updated...");

            //checking it
            DeepInstanceJ deepInstanceJ = registryJ.getJ(createdInstanceId).get().get();
            deepInstanceJ.unfoldJ().get();
            var attributeValuesMap = deepInstanceJ.getDeepAttributes();
            System.out.println(attributeValuesMap);
        }

    }

    public static void modicioTest() throws ExecutionException, InterruptedException {
        DefinitionVerifierJ definitionVerifierJ = new SimpleDefinitionVerifierJ();
        ModelVerifierJ modelVerifierJ = new SimpleModelVerifierJ();
        TypeFactoryJ typeFactoryJ = new TypeFactoryJ(definitionVerifierJ, modelVerifierJ);
        InstanceFactoryJ instanceFactoryJ = new InstanceFactoryJ(definitionVerifierJ, modelVerifierJ);
        RegistryJ registryJ = new SimpleMapRegistryJ(typeFactoryJ, instanceFactoryJ);
        typeFactoryJ.setRegistryJ(registryJ);
        instanceFactoryJ.setRegistryJ(registryJ);

        CompletableFuture<TypeHandleJ> root = typeFactoryJ.newTypeJ(
                ModelElement.ROOT_NAME(),
                ModelElement.REFERENCE_IDENTITY(),
                true,
                Optional.of(TimeIdentity.create()));
        CompletableFuture<Object> typeFuture = registryJ.setType(root.get());
        typeFuture.get();

        String myType = "Todo";
        CompletableFuture<TypeHandleJ> todoType = typeFactoryJ.newTypeJ(myType, ModelElement.REFERENCE_IDENTITY(), false);
        registryJ.setType(todoType.get()).get();
        CompletableFuture<DeepInstanceJ> instanceFuture = instanceFactoryJ.newInstanceJ("Todo");
        Optional<DeepInstanceJ> savedInstanceOption = registryJ.getJ(instanceFuture.get().instanceId()).get();
        if(savedInstanceOption.isPresent()) {
            DeepInstanceJ myTodo = savedInstanceOption.get();
            System.out.println("Hooray!! id: " + myTodo.getInstanceIdJ());
        }else{
            System.out.println("This did not work...");
        }
    }

    public static void test() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Constraint constraint = new Constraint();
        constraint.setUuid(UUID.randomUUID().toString());
        constraint.setViolationLevel(ViolationLevel.WARN);
        constraint.setViolationMessage("Test");


        ConstraintFunction lessThanAge = new StringBasedFunction("LESS_THAN", "2", "age", Map.of("lessThan", "16"));
        ConstraintFunction greaterThanAge = new StringBasedFunction("GREATER_THAN", "2", "age", Map.of("greaterThan", "10"));
        ConstraintFunction andNested = new LogicalFunction("AND", List.of(lessThanAge, greaterThanAge));
        ConstraintFunction greaterThanSalary = new StringBasedFunction("GREATER_THAN", "1", "salary", Map.of("greaterThan", "25000"));
        ConstraintFunction or = new LogicalFunction("OR", List.of(greaterThanSalary, andNested));

        constraint.setConstraintFunction(or);

        String json = objectMapper.writeValueAsString(constraint);
        Constraint fromString = objectMapper.readValue(json, Constraint.class);
        System.out.println(fromString);
    }

    private static  <T> CompletableFuture<T> toCompletableFuture(Future<T> future) {
        return FutureConverters.asJava(future).toCompletableFuture();
    }

    private static  <T> Optional<T> toOptional(Option<T> option) {
        return OptionConverters.toJava(option);
    }

}
