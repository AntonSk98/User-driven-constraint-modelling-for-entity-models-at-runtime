package anton.skripin.development.eumcf.configuration;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import modicio.core.ModelElement;
import modicio.core.TimeIdentity;
import modicio.core.TypeHandle;
import modicio.core.api.*;
import modicio.core.rules.ConnectionInterface;
import modicio.core.rules.Slot;
import modicio.core.rules.api.AssociationRuleJ;
import modicio.core.rules.api.AttributeRuleJ;
import modicio.core.rules.api.ParentRelationRuleJ;
import org.springframework.stereotype.Component;
import scala.Option;
import scala.concurrent.Future;
import scala.jdk.javaapi.CollectionConverters;
import scala.jdk.javaapi.FutureConverters;
import scala.jdk.javaapi.OptionConverters;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

//@Profile("generate-modicio-data")
@Component
public class TestDataGenerator {

    private final String[] NON_TEMPLATE_MODEL_ELEMENT_NAMES = new String[]{
            "SoftwareEngineer",
            "Project",
            "Spring"
    };

    private final RegistryJ registryJ;
    private final TypeFactoryJ typeFactoryJ;
    private final InstanceFactoryJ instanceFactoryJ;

    public TestDataGenerator(RegistryJ registryJ, TypeFactoryJ typeFactoryJ, InstanceFactoryJ instanceFactoryJ) {
        this.registryJ = registryJ;
        this.typeFactoryJ = typeFactoryJ;
        this.instanceFactoryJ = instanceFactoryJ;
    }

    @PostConstruct
    public void generateTestDate() throws ExecutionException, InterruptedException {
        generateRoot();
        generateModelElements();
        generateInstanceElements();
    }

    private void generateRoot() throws ExecutionException, InterruptedException {
        CompletableFuture<TypeHandleJ> root = typeFactoryJ.newTypeJ(
                ModelElement.ROOT_NAME(),
                ModelElement.REFERENCE_IDENTITY(),
                true,
                Optional.of(TimeIdentity.create()));
        registryJ.setType(root.get()).get();
    }

    private void generateModelElements() throws ExecutionException, InterruptedException {
        for (ModelElementInner modelElementInner : getTestModelElements()) {
            CompletableFuture<TypeHandleJ> type = typeFactoryJ.newTypeJ(modelElementInner.getName(), modelElementInner.getId(), modelElementInner.isTemplate);
            registryJ.setType(type.get()).get();
            TypeHandle typeHandle = toOptional(toCompletableFuture(registryJ.getRegistry()
                    .getType(modelElementInner.getName(), modelElementInner.getId()))
                    .get())
                    .get();
            typeHandle = toCompletableFuture(typeHandle.unfold()).get();
            for (Attribute attribute : modelElementInner.getAttributes()) {
                typeHandle.applyRule(AttributeRuleJ.create(attribute.getName(), attribute.getDatatype(), attribute.isNonEmpty(), Option.empty()));
                toCompletableFuture(typeHandle.commit(false)).get();
            }
            for (Association association : modelElementInner.getAssociations()) {
                typeHandle.applyRule(AssociationRuleJ
                        .create(association.getName(),
                                association.getTarget(),
                                association.getMultiplicity(),
                                new ConnectionInterface(CollectionConverters.asScala(List.of(new Slot(association.getTarget(), ">0")))),
                                Option.empty()));
            }
            for (ParentRelation parentRelation : modelElementInner.getParentRelations()) {
                typeHandle.applyRule(ParentRelationRuleJ.create(parentRelation.getName(), parentRelation.getId(), Option.empty()));
            }
        }
    }

    private List<ModelElementInner> getTestModelElements() {
        ModelElementInner namedElement = new ModelElementInner(
                ModelElement.REFERENCE_IDENTITY(),
                "NamedElement",
                true,
                List.of(
                        new ParentRelation(ModelElement.REFERENCE_IDENTITY(), ModelElement.ROOT_NAME())
                ),
                List.of(
                        new Attribute("name", "String", false)
                ),
                Collections.emptyList()
        );

        ModelElementInner person = new ModelElementInner(
                ModelElement.REFERENCE_IDENTITY(),
                "Person",
                true,
                List.of(
                        new ParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                List.of(
                        new Attribute("age", "String", false)
                ),
                Collections.emptyList()
        );

        ModelElementInner softwareEngineer = new ModelElementInner(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[0],
                false,
                List.of(
                        new ParentRelation(ModelElement.REFERENCE_IDENTITY(), "Person")
                ),
                List.of(
                        new Attribute("salary", "String", false)
                ),
                List.of(
                        new Association("works_on", "Project", "*"),
                        new Association("takes_part_in", "Sprint", "*")
                )
        );

        ModelElementInner project = new ModelElementInner(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[1],
                false,
                List.of(
                        new ParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                List.of(
                        new Attribute("responsible", "String", false),
                        new Attribute("started", "String", false)
                ),
                List.of(
                        new Association("consists_of", "Sprint", "*"),
                        new Association("participates", "SoftwareEngineer", "*")
                )
        );

        ModelElementInner sprint = new ModelElementInner(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[2],
                false,
                List.of(
                        new ParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );

        return List.of(namedElement, person, softwareEngineer, project, sprint);
    }

    private void generateInstanceElements() throws ExecutionException, InterruptedException {
        String anton = createTestSwEngineer("Anton", "24", "10000");
        String egor = createTestSwEngineer("Egor", "23", "8000");
        String thesis = createTestProject("Thesis", "Anton", "true");


        var a = registryJ.getJ(thesis).get().get();
//        a.unfoldJ().get();
        System.out.println(a.getDeepAttributes());


    }

    @SneakyThrows
    private String createTestSwEngineer(String name, String age, String salary) {
        String sw = NON_TEMPLATE_MODEL_ELEMENT_NAMES[0];
        String id = instanceFactoryJ.newInstanceJ(sw).get().getInstanceIdJ();
        Optional<DeepInstanceJ> deepInstanceOptional = registryJ.getJ(id).get();
        CompletableFuture<DeepInstanceJ> deepInstanceJFuture = deepInstanceOptional.get().unfoldJ();
        DeepInstanceJ deepInstanceJ = deepInstanceJFuture.get();
        deepInstanceJ.assignValue("salary", salary);
        deepInstanceJ.assignDeepValue("name", name);
        deepInstanceJ.assignDeepValue("age", age);
        deepInstanceJ.commitJ().get();
        return id;
    }

    @SneakyThrows
    private String createTestProject(String name, String responsible, String started) {
        String project = NON_TEMPLATE_MODEL_ELEMENT_NAMES[1];
        String createdInstanceId = instanceFactoryJ.newInstanceJ(project).get().getInstanceIdJ();

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
        return createdInstanceId;
    }

    private <T> CompletableFuture<T> toCompletableFuture(Future<T> future) {
        return FutureConverters.asJava(future).toCompletableFuture();
    }

    private <T> Optional<T> toOptional(Option<T> option) {
        return OptionConverters.toJava(option);
    }

    @Getter
    @AllArgsConstructor
    class ModelElementInner {
        String id;
        String name;
        boolean isTemplate;
        List<ParentRelation> parentRelations;
        List<Attribute> attributes;
        List<Association> associations;
    }

    @Getter
    @AllArgsConstructor
    private class ParentRelation {
        String id;
        String name;
    }

    @Getter
    @AllArgsConstructor
    private class Attribute {
        String name;
        String datatype;
        boolean nonEmpty;
    }

    @Getter
    @AllArgsConstructor
    private class Association {
        String name;
        String target;
        String multiplicity;
    }
}
