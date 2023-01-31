package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.service.api.ConstraintPersistenceService;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleConstraintPersistenceService implements ConstraintPersistenceService {

    private final Map<String, List<Constraint>> constraintSpace = new HashMap<>();

    private final Map<String, List<String>> backwardLinksSpace = new HashMap<>();

    @Override
    public boolean saveConstraint(String id, Constraint constraint) {
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
        return Objects.isNull(constraintSpace.get(type)) ? Collections.emptyList() : constraintSpace.get(type);
    }

    @Override
    public List<Constraint> getAllConstraintsByName(String type, String name) {
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
}
