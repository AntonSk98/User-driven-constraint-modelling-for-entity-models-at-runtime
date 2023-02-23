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

import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;
import ansk.development.eumcf.service.api.InstanceService;
import ansk.development.exception.instance.InstanceOperationException;
import ansk.development.mapper.InstanceMapper;
import lombok.SneakyThrows;
import modicio.core.DeepInstance;
import modicio.core.Registry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static ansk.development.eumcf.util.ScalaToJavaMapper.*;

@Service
public class InstanceServiceImpl implements InstanceService {

    private final Registry registry;

    private final InstanceMapper<DeepInstance> instanceMapper;

    public InstanceServiceImpl(Registry registry, InstanceMapper<DeepInstance> instanceMapper) {
        this.registry = registry;
        this.instanceMapper = instanceMapper;
    }

    @Override
    public boolean removeInstancesByType(String type) {
        try {
            set(future(registry.getAll(type))
                    .get())
                    .stream()
                    .map(DeepInstance::instanceId)
                    .forEach(registry::autoRemove);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public boolean deleteInstanceById(String uuid) {
        try {
            future(registry.autoRemove(uuid)).get();
            validateReferences();
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    private void validateReferences() {
        try {
            Set<String> types = set(future(registry.getAllTypes()).get());
            for (String type : types) {
                set(future(registry.getAll(type)).get()).forEach(this::validateLinks);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateLinks(DeepInstance deepInstance) {
        try {
            future(deepInstance.unfold()).get();
            for (var association : set(deepInstance.getDeepAssociations())) {
                if (future(registry.get(association.targetInstanceId())).get().isEmpty()) {
                    deepInstance.removeAssociation(association.id());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createInstance(String instanceOf, List<Slot> slots, List<Link> links) {
        try {
            DeepInstance deepInstance = future(registry.instanceFactory().newInstance(instanceOf)).get();
            commonOperation(deepInstance, slots, links);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public boolean updateInstance(String uuid, List<Slot> slots, List<Link> links) {
        try {
            DeepInstance toBeUpdated = future(registry.get(uuid)).get().get();
            set(toBeUpdated.getDeepAssociations()).forEach(associationData -> toBeUpdated.removeAssociation(associationData.id()));
            commonOperation(toBeUpdated, slots, links);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public InstanceElement getInstanceByUuid(String uuid) {
        try {
            DeepInstance deepInstance = future(registry.get(uuid)).get().get();
            future(deepInstance.unfold()).get();
            return instanceMapper.mapToInstanceElement(deepInstance);
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    public Map<String, List<InstanceElement>> getTypeToInstancesMap() {
        try {
            Map<String, List<InstanceElement>> instanceElementMap = new HashMap<>();
            for (String type : set(future(registry.getAllTypes()).get())) {
                List<InstanceElement> instanceElements = new ArrayList<>();
                for (DeepInstance instanceElement : set(future(registry.getAll(type)).get())) {
                    if (!instanceElement.getTypeHandle().getIsTemplate()) {
                        instanceElements.add(instanceMapper.mapToInstanceElement(instanceElement));
                    }
                }
                if (instanceElements.size() > 0) {
                    instanceElementMap.put(type, instanceElements);
                }
            }
            return instanceElementMap;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }

    }

    @Override
    public InstanceElement withAssociations(InstanceElement instanceElement) {
        try {
            DeepInstance deepInstance = future(registry.get(instanceElement.getUuid())).get().get();
            future(deepInstance.unfold()).get();
            List<Link> listOfLinks = new ArrayList<>(instanceElement.getLinks());
            listOfLinks.addAll(map(deepInstance.associationRuleMap())
                    .keySet()
                    .stream()
                    .filter(association -> instanceElement.getLinks().stream().noneMatch(link -> link.getName().equals(association)))
                    .map(association -> {
                        Link link = new Link();
                        link.setName(association);
                        return link;
                    })
                    .collect(Collectors.toList()));
            instanceElement.setLinks(listOfLinks);
            return instanceElement;
        } catch (ExecutionException | InterruptedException e) {
            throw new InstanceOperationException(e);
        }
    }

    @Override
    @SneakyThrows
    public List<InstanceElement> getRequiredSubgraph(String instanceUuid, Set<Set<String>> requiredSubgraphElements) {
        Set<InstanceElement> instanceElements = new HashSet<>();
        requiredSubgraphElements.forEach(modelElementsForConstraintFunction -> {
            List<DeepInstance> nodesToVisit = new ArrayList<>();
            try {
                DeepInstance contextInstance = future(registry.get(instanceUuid)).get().get();
                nodesToVisit.add(contextInstance);
                instanceElements.add(instanceMapper.mapToInstanceElement(contextInstance));
                for (String modelElement : modelElementsForConstraintFunction) {
                    if (modelElement.equals(contextInstance.getTypeHandle().getTypeName())) {
                        continue;
                    }
                    List<DeepInstance> temp = new ArrayList<>();
                    for (DeepInstance node : nodesToVisit) {
                        future(node.unfold()).get();
                        temp.addAll(set(node.getDeepAssociations())
                                .stream()
                                .map(associationData -> {
                                    try {
                                        return future(registry.get(associationData.targetInstanceId())).get().get();
                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .filter(deepInstance -> deepInstance.getTypeHandle().getTypeName().equals(modelElement))
                                .collect(Collectors.toSet()));
                    }
                    instanceElements.addAll(temp.stream().map(instanceMapper::mapToInstanceElement).collect(Collectors.toList()));
                    nodesToVisit.clear();
                    nodesToVisit.addAll(temp);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(String.format("Error occurred while trying to get a subgraph for instance %s", instanceUuid), e);
            }
        });
        return new ArrayList<>(instanceElements);
    }

    private void commonOperation(DeepInstance deepInstance, List<Slot> slots, List<Link> links) throws ExecutionException, InterruptedException {
        future(deepInstance.unfold()).get();

        for (Slot slot : slots) {
            deepInstance.assignDeepValue(slot.getKey(), slot.getValue());
        }

        for (Link link : links) {
            if (StringUtils.isBlank(link.getTargetInstanceUuid())) {
                continue;
            }
            DeepInstance to = future(registry.get(link.getTargetInstanceUuid().trim())).get().get();
            future(to.unfold()).get();
            deepInstance.associate(to, to.typeHandle().getTypeName(), link.getName());
        }
    }
}
