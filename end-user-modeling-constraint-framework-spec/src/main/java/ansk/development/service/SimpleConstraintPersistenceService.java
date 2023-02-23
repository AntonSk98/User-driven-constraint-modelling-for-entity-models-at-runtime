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

package ansk.development.service;

import ansk.development.domain.NavigationUtils;
import ansk.development.domain.ValidationUtils;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.backward_links.InstanceBackwardLink;
import ansk.development.domain.constraint.backward_links.ModelElementBackwardLink;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.service.api.ConstraintPersistenceService;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleConstraintPersistenceService implements ConstraintPersistenceService {

    /**
     * key -> model element id (model element name)
     * value -> list of associated constraints
     */
    private final Map<String, List<Constraint>> constraintSpace = new HashMap<>();

    /**
     * key -> instance element id
     * value -> list of associated constraint backward links
     */
    private final Map<String, List<InstanceBackwardLink>> instanceBackwardLinks = new HashMap<>();

    private final Map<String, List<ModelElementBackwardLink>> modelElementBackwardLinks = new HashMap<>();

    @Override
    public boolean saveConstraint(String id, Constraint constraint) {
        assert id != null;
        assert constraint != null;
        constraintSpace.computeIfPresent(id, (key, constraints) -> {
            constraints.add(constraint);
            return constraints;
        });
        constraintSpace.computeIfAbsent(id, key -> {
            List<Constraint> constraints = new ArrayList<>();
            constraints.add(constraint);
            return constraints;
        });
        resolveModelElementBackwardLinks(constraint, constraint.getConstraintFunction());
        return true;
    }

    @Override
    public void resolveModelElementBackwardLinks(Constraint constraint, ConstraintFunction constraintFunction) {
        constraintFunction.navigation().ifPresent(navigation -> {
            ValidationUtils.validateNavigation(navigation);
            List<String> navigationTypes = new ArrayList<>(NavigationUtils.getNavigationTypes(navigation));
            navigationTypes.forEach(navigationType -> addModelElementBackwardLink(navigationType, constraint.getModelElementType(), constraint.getUuid()));
        });
        constraintFunction
                .lambdaFunction()
                .ifPresent(lambdaFunction -> resolveModelElementBackwardLinks(constraint, lambdaFunction));
        constraintFunction
                .booleanFunctions()
                .ifPresent(booleanFunctions -> booleanFunctions
                        .forEach(booleanFunction -> resolveModelElementBackwardLinks(constraint, booleanFunction)));
    }

    @Override
    public boolean addModelElementBackwardLink(String targetModelElement, String contextModelElement, String constraintUuid) {
        if (modelElementBackwardLinks.containsKey(targetModelElement)) {
            modelElementBackwardLinks.get(targetModelElement).add(new ModelElementBackwardLink(targetModelElement, contextModelElement, constraintUuid));
        } else {
            List<ModelElementBackwardLink> modelElementBackwardLinkList = new ArrayList<>();
            modelElementBackwardLinkList.add(new ModelElementBackwardLink(targetModelElement, contextModelElement, constraintUuid));
            modelElementBackwardLinks.put(targetModelElement, modelElementBackwardLinkList);
        }
        return true;
    }


    @Override
    public List<Constraint> getAllConstraints(String type) {
        assert type != null;
        return Objects.isNull(constraintSpace.get(type)) ? Collections.emptyList() : constraintSpace.get(type);
    }

    @Override
    public List<Constraint> getAllBackwardLinkConstraints(String type) {
        if (modelElementBackwardLinks.containsKey(type)) {
            return modelElementBackwardLinks
                    .get(type)
                    .stream()
                    .map(ModelElementBackwardLink::getConstraintUuid)
                    .map(this::getConstraintByUuid)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Constraint> getAllConstraintsByName(String type, String name) {
        assert type != null;
        return constraintSpace
                .get(type)
                .stream()
                .filter(constraint -> constraint.getName().equals(name))
                .collect(Collectors.toList());
    }


    @Override
    public Constraint getConstraintByUuid(String uuid) {
        return constraintSpace
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(constraint -> constraint.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<String, List<Constraint>> getGroupConstraintsByTypeAnsSupertypes(Map<String, List<String>> typeToSupertypeMap) {
        assert typeToSupertypeMap != null;
        Map<String, List<Constraint>> groupedConstraints = new HashMap<>();
        typeToSupertypeMap.forEach((key, value) -> {
            List<Constraint> constraintList = new ArrayList<>();
            value.forEach(s -> constraintList.addAll(this.getAllConstraints(s)));
            if (!constraintList.isEmpty()) {
                groupedConstraints.put(key, constraintList);
            }
        });

        return groupedConstraints;
    }

    @Override
    public boolean removeConstraintByUuid(String uuid) {
        this.removeModelElementBackwardLinkByConstraintUuid(uuid);
        return this.constraintSpace
                .entrySet()
                .stream()
                .filter(stringListEntry -> stringListEntry.getValue().stream().anyMatch(constraint -> constraint.getUuid().equals(uuid)))
                .map(Map.Entry::getKey)
                .anyMatch(type -> this.constraintSpace.get(type).removeIf(constraint -> constraint.getUuid().equals(uuid)));
    }

    @Override
    public void removeModelElementBackwardLinkByConstraintUuid(String uuid) {
        modelElementBackwardLinks
                .entrySet()
                .stream()
                .filter(stringListEntry -> stringListEntry.getValue().stream().anyMatch(modelElementBackwardLink -> modelElementBackwardLink.getConstraintUuid().equals(uuid)))
                .map(Map.Entry::getKey)
                .forEach(type -> this.modelElementBackwardLinks.get(type).removeIf(modelElementBackwardLink -> modelElementBackwardLink.getConstraintUuid().equals(uuid)));
    }

    @Override
    public List<InstanceBackwardLink> getConstraintLinksByInstanceUuid(String instanceUuid) {
        assert instanceUuid != null;
        return this.instanceBackwardLinks.get(instanceUuid);
    }

    @Override
    public boolean addInstanceBackwardLink(String targetInstanceUuid, String contextInstanceUuid, String constraintUuid) {
        assert targetInstanceUuid != null;
        assert contextInstanceUuid != null;
        assert constraintUuid != null;
        var backwardLink = new InstanceBackwardLink(targetInstanceUuid, contextInstanceUuid, constraintUuid);

        List<InstanceBackwardLink> instanceBackwardLinks = this.instanceBackwardLinks.get(targetInstanceUuid);
        if (instanceBackwardLinks == null) {
            instanceBackwardLinks = new ArrayList<>();
            instanceBackwardLinks.add(backwardLink);
            this.instanceBackwardLinks.put(targetInstanceUuid, instanceBackwardLinks);
        } else {
            instanceBackwardLinks.add(backwardLink);
        }
        return true;
    }

    @Override
    public void removeInstanceBackwardLinkByConstraintUuid(String constraintUuid) {
        assert constraintUuid != null;
        this.instanceBackwardLinks
                .values()
                .forEach(instanceBackwardLinksList -> instanceBackwardLinksList
                        .removeIf(instanceBackwardLink -> constraintUuid
                                .equals(instanceBackwardLink.getConstraintUuid())));
    }

    @Override
    public boolean removeInstanceBackwardLink(String targetInstanceUuid) {
        assert targetInstanceUuid != null;
        List<InstanceBackwardLink> linkedConstraints = this.instanceBackwardLinks.get(targetInstanceUuid);
        if (linkedConstraints == null) {
            return false;
        } else {
            return linkedConstraints
                    .removeIf(instanceBackwardLink -> targetInstanceUuid.equals(instanceBackwardLink.getTargetInstanceUuid()));
        }
    }

    @Override
    public boolean doesConstraintLinkExist(String targetInstanceUuid, String constraintUuid) {
        assert targetInstanceUuid != null;
        assert constraintUuid != null;
        return this.instanceBackwardLinks.get(targetInstanceUuid) != null && this.instanceBackwardLinks
                .get(targetInstanceUuid)
                .stream()
                .anyMatch(instanceBackwardLink -> constraintUuid.equals(instanceBackwardLink.getConstraintUuid()));
    }
}
