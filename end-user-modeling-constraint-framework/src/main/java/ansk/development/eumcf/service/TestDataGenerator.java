/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development.eumcf.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import modicio.core.*;
import modicio.core.rules.ConnectionInterface;
import modicio.core.rules.Slot;
import modicio.core.rules.api.AssociationRuleJ;
import modicio.core.rules.api.AttributeRuleJ;
import modicio.core.rules.api.ParentRelationRuleJ;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import scala.Option;
import scala.Some;
import scala.jdk.javaapi.CollectionConverters;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ansk.development.eumcf.util.ScalaToJavaMapper.future;

/**
 * Generates test model elements and instances for them.
 */
@Component
@Profile("testdata")
public class TestDataGenerator {

    private final String[] NON_TEMPLATE_MODEL_ELEMENT_NAMES = new String[]{
            "SoftwareEngineer",
            "Project",
            "Sprint"
    };

    private final Registry registry;
    private final TypeFactory typeFactory;
    private final InstanceFactory instanceFactory;

    /**
     * Constructor.
     *
     * @param registry        {@link Registry}
     * @param typeFactory     {@link TypeFactory}
     * @param instanceFactory {@link InstanceFactory}
     */
    public TestDataGenerator(Registry registry, TypeFactory typeFactory, InstanceFactory instanceFactory) {
        this.registry = registry;
        this.typeFactory = typeFactory;
        this.instanceFactory = instanceFactory;
    }

    /**
     * Initialization class for bootstrapping test data.
     */
    @PostConstruct
    public void generateTestDate() {
        generateRoot();
        generateModelElements();
        generateInstanceElements();
    }

    /**
     * Generates a root element required by Modicio setup.
     */
    @SneakyThrows
    private void generateRoot() {
        CompletableFuture<TypeHandle> root = future(typeFactory.newType(
                ModelElement.ROOT_NAME(),
                ModelElement.REFERENCE_IDENTITY(),
                true,
                new Some<>((TimeIdentity.create()))));
        CompletableFuture<Object> typeFuture = future(registry.setType(root.get(), false));
        typeFuture.get();
    }

    /**
     * Generates test model elements.
     */
    @SneakyThrows
    private void generateModelElements() {
        for (ModicioModelElement modelElementInner : getTestModelElements()) {
            CompletableFuture<TypeHandle> type = future(typeFactory.newType(modelElementInner.getName(), modelElementInner.getId(), modelElementInner.isTemplate(), Option.empty()));
            future(registry.setType(type.get(), false)).get();
            TypeHandle typeHandle = future(registry
                    .getType(modelElementInner.getName(), modelElementInner.getId()))
                    .get()
                    .get();
            typeHandle = future(typeHandle.unfold()).get();
            for (ModicioAttribute attribute : modelElementInner.getAttributes()) {
                typeHandle.applyRule(AttributeRuleJ.create(attribute.getName(), attribute.getDatatype(), attribute.isNonEmpty(), Option.empty()));
            }
            for (ModicioAssociation association : modelElementInner.getAssociations()) {
                typeHandle.applyRule(AssociationRuleJ
                        .create(association.getName(),
                                association.getTarget(),
                                association.getMultiplicity(),
                                new ConnectionInterface(CollectionConverters.asScala(List.of(new Slot(association.getTarget(), ">0")))),
                                Option.empty()));
            }
            for (ModicioParentRelation parentRelation : modelElementInner.getParentRelations()) {
                typeHandle.applyRule(ParentRelationRuleJ.create(parentRelation.getName(), parentRelation.getId(), Option.empty()));
            }
        }
    }

    /**
     * Generates instance elements
     */
    private void generateInstanceElements() {
        var worksOnAssociation = "works_on";
        var participatesAssociation = "participates";
        var takesPartInAssociation = "takes_part_in";
        var consistsOfAssociation = "consists_of";

        var swEngineer = NON_TEMPLATE_MODEL_ELEMENT_NAMES[0];
        var project = NON_TEMPLATE_MODEL_ELEMENT_NAMES[1];
        var sprint = NON_TEMPLATE_MODEL_ELEMENT_NAMES[2];

        String john = createTestSwEngineer("John", "24", "10000");
        String egor = createTestSwEngineer("Egor", "23", "8000");
        String thesis = createTestProject("Thesis", "John", "true");
        String designProcess = createTestSprint("Design process");
        String architectureProcess = createTestSprint("Architecture process");
        String implementationProcess = createTestSprint("Implementation process");

        associateTwoInstances(john, worksOnAssociation, thesis, project);
        associateTwoInstances(egor, worksOnAssociation, thesis, project);

        associateTwoInstances(thesis, participatesAssociation, john, swEngineer);
        associateTwoInstances(thesis, participatesAssociation, egor, swEngineer);

        associateTwoInstances(john, takesPartInAssociation, designProcess, sprint);
        associateTwoInstances(john, takesPartInAssociation, architectureProcess, sprint);
        associateTwoInstances(john, takesPartInAssociation, implementationProcess, sprint);
        associateTwoInstances(egor, takesPartInAssociation, implementationProcess, sprint);

        associateTwoInstances(thesis, consistsOfAssociation, designProcess, sprint);
        associateTwoInstances(thesis, consistsOfAssociation, architectureProcess, sprint);
        associateTwoInstances(thesis, consistsOfAssociation, implementationProcess, sprint);


    }

    @SneakyThrows
    private String createTestSwEngineer(String name, String age, String salary) {
        String sw = NON_TEMPLATE_MODEL_ELEMENT_NAMES[0];
        String id = future(instanceFactory.newInstance(sw)).get().getInstanceId();
        DeepInstance deepInstance = future(registry.get(id)).get().get();
        future(deepInstance.unfold()).get();
        deepInstance.assignValue("salary", salary);
        deepInstance.assignDeepValue("name", name);
        deepInstance.assignDeepValue("age", age);
        future(deepInstance.commit()).get();
        return id;
    }

    @SneakyThrows
    private String createTestProject(String name, String responsible, String started) {
        String project = NON_TEMPLATE_MODEL_ELEMENT_NAMES[1];
        String id = future(instanceFactory.newInstance(project)).get().getInstanceId();
        DeepInstance deepInstance = future(registry.get(id)).get().get();
        future(deepInstance.unfold()).get();
        deepInstance.assignDeepValue("name", name);
        deepInstance.assignDeepValue("responsible", responsible);
        deepInstance.assignDeepValue("started", started);
        future(deepInstance.commit()).get();
        return id;
    }

    @SneakyThrows
    private String createTestSprint(String name) {
        String sprint = NON_TEMPLATE_MODEL_ELEMENT_NAMES[2];
        String id = future(instanceFactory.newInstance(sprint)).get().getInstanceId();
        DeepInstance deepInstance = future(registry.get(id)).get().get();
        future(deepInstance.unfold()).get();
        deepInstance.assignDeepValue("name", name);
        future(deepInstance.commit()).get();
        return id;
    }

    @SneakyThrows
    private void associateTwoInstances(String sourceInstanceId, String byRelation, String targetInstanceId, String asType) {
        DeepInstance sourceInstance = future(registry.get(sourceInstanceId)).get().get();
        DeepInstance targetInstance = future(registry.get(targetInstanceId)).get().get();

        future(sourceInstance.unfold()).get();
        future(targetInstance.unfold()).get();

        sourceInstance.associate(targetInstance, asType, byRelation);
    }

    /**
     * Only for local checks...
     *
     * @param instanceId instance id
     */
    @SneakyThrows
    private void checkInstance(String instanceId) {
        //checking
        DeepInstance deepInstance = future(registry.get(instanceId)).get().get();
        future(deepInstance.unfold()).get();
        System.out.println(deepInstance.getDeepAssociations());
    }

    private List<ModicioModelElement> getTestModelElements() {
        ModicioModelElement namedElement = new ModicioModelElement(
                ModelElement.REFERENCE_IDENTITY(),
                "NamedElement",
                true,
                List.of(
                        new ModicioParentRelation(ModelElement.REFERENCE_IDENTITY(), ModelElement.ROOT_NAME())
                ),
                List.of(
                        new ModicioAttribute("name", "String", false)
                ),
                Collections.emptyList()
        );

        ModicioModelElement person = new ModicioModelElement(
                ModelElement.REFERENCE_IDENTITY(),
                "Person",
                true,
                List.of(
                        new ModicioParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                List.of(
                        new ModicioAttribute("age", "String", false)
                ),
                Collections.emptyList()
        );

        ModicioModelElement softwareEngineer = new ModicioModelElement(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[0],
                false,
                List.of(
                        new ModicioParentRelation(ModelElement.REFERENCE_IDENTITY(), "Person")
                ),
                List.of(
                        new ModicioAttribute("salary", "String", false)
                ),
                List.of(
                        new ModicioAssociation("works_on", "Project", "*"),
                        new ModicioAssociation("takes_part_in", "Sprint", "*")
                )
        );

        ModicioModelElement project = new ModicioModelElement(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[1],
                false,
                List.of(
                        new ModicioParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                List.of(
                        new ModicioAttribute("responsible", "String", false),
                        new ModicioAttribute("started", "String", false)
                ),
                List.of(
                        new ModicioAssociation("consists_of", "Sprint", "*"),
                        new ModicioAssociation("participates", "SoftwareEngineer", "*")
                )
        );

        ModicioModelElement sprint = new ModicioModelElement(
                ModelElement.REFERENCE_IDENTITY(),
                NON_TEMPLATE_MODEL_ELEMENT_NAMES[2],
                false,
                List.of(
                        new ModicioParentRelation(ModelElement.REFERENCE_IDENTITY(), "NamedElement")
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );

        return List.of(namedElement, person, softwareEngineer, project, sprint);
    }

    @Getter
    @AllArgsConstructor
    private static class ModicioAssociation {
        String name;
        String target;
        String multiplicity;
    }

    @Getter
    @AllArgsConstructor
    private static class ModicioAttribute {
        String name;
        String datatype;
        boolean nonEmpty;
    }

    @Getter
    @AllArgsConstructor
    private static class ModicioModelElement {
        String id;
        String name;
        boolean isTemplate;
        List<ModicioParentRelation> parentRelations;
        List<ModicioAttribute> attributes;
        List<ModicioAssociation> associations;
    }

    @Getter
    @AllArgsConstructor
    private static class ModicioParentRelation {
        String id;
        String name;
    }


}
