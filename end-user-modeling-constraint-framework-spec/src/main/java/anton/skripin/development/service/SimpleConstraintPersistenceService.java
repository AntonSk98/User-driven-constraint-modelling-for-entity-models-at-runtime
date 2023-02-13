package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ConstraintBackwardLink;
import anton.skripin.development.service.api.ConstraintPersistenceService;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleConstraintPersistenceService implements ConstraintPersistenceService {

    /**
     * key -> model element id
     * value -> list of associated constraints
     */
    private final Map<String, List<Constraint>> constraintSpace = new HashMap<>();

    /**
     * key -> instance element id
     * value -> list of associated constraint backward links
     */
    private final Map<String, List<ConstraintBackwardLink>> backwardLinksSpace = new HashMap<>();

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
        return true;
    }

    @Override
    public List<Constraint> getAllConstraints(String type) {
        assert type != null;
        return Objects.isNull(constraintSpace.get(type)) ? Collections.emptyList() : constraintSpace.get(type);
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
        return this.constraintSpace
                .entrySet()
                .stream()
                .filter(stringListEntry -> stringListEntry.getValue().stream().anyMatch(constraint -> constraint.getUuid().equals(uuid)))
                .map(Map.Entry::getKey)
                .anyMatch(type -> this.constraintSpace.get(type).removeIf(constraint -> constraint.getUuid().equals(uuid)));
    }

    @Override
    public List<ConstraintBackwardLink> getConstraintLinksByInstanceUuid(String instanceUuid) {
        assert instanceUuid != null;
        return this.backwardLinksSpace.get(instanceUuid);
    }

    @Override
    public boolean linkConstraintToInstance(String targetInstanceUuid, String contextInstanceUuid, String constraintUuid) {
        assert targetInstanceUuid != null;
        assert contextInstanceUuid != null;
        assert constraintUuid != null;
        var backwardLink = new ConstraintBackwardLink(targetInstanceUuid, contextInstanceUuid, constraintUuid);

        List<ConstraintBackwardLink> linkedConstraints = this.backwardLinksSpace.get(targetInstanceUuid);
        if (linkedConstraints == null) {
            linkedConstraints = new ArrayList<>();
            linkedConstraints.add(backwardLink);
            this.backwardLinksSpace.put(targetInstanceUuid, linkedConstraints);
        } else {
            linkedConstraints.add(backwardLink);
        }
        return true;
    }

    @Override
    public boolean removeConstraintLinkFromInstance(String targetInstanceUuid, String constraintUuid) {
        assert targetInstanceUuid != null;
        assert constraintUuid != null;
        List<ConstraintBackwardLink> linkedConstraints = this.backwardLinksSpace.get(targetInstanceUuid);
        if (linkedConstraints == null) {
            return false;
        } else {
            return linkedConstraints
                    .removeIf(constraintBackwardLink -> constraintUuid.equals(constraintBackwardLink.getConstraintUuid()));
        }
    }

    @Override
    public boolean doesConstraintLinkExist(String targetInstanceUuid, String constraintUuid) {
        assert targetInstanceUuid != null;
        assert constraintUuid != null;
        return this.backwardLinksSpace.get(targetInstanceUuid) != null && this.backwardLinksSpace
                .get(targetInstanceUuid)
                .stream()
                .anyMatch(constraintBackwardLink -> constraintUuid.equals(constraintBackwardLink.getConstraintUuid()));
    }
}
