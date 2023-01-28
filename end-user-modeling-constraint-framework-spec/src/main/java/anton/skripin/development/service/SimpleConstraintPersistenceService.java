package anton.skripin.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.service.api.ConstraintPersistenceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleConstraintPersistenceService implements ConstraintPersistenceService {

    private final List<Constraint> constraints = new ArrayList<>();

    @Override
    public boolean saveConstraint(Constraint constraint) {
        this.constraints.add(constraint);
        return true;
    }

    @Override
    public List<Constraint> getAllConstraints() {
        return constraints;
    }

    @Override
    public List<Constraint> getAllConstraintsByName(String name) {
        return constraints
                .stream()
                .filter(constraint -> constraint.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Constraint> getAllConstraintsByType(String type) {
        return constraints
                .stream()
                .filter(constraint -> constraint.getTargetModelElementName().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public Constraint getConstraintByUuid(String uuid) {
        return constraints
                .stream()
                .filter(constraint -> constraint.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<String, List<Constraint>> getGroupedConstraintsByName() {
        Map<String, List<Constraint>> groupedConstraints = new HashMap<>();
        this.constraints.forEach(constraint -> {
            if (groupedConstraints.containsKey(constraint.getTargetModelElementName())) {
                groupedConstraints.get(constraint.getTargetModelElementName()).add(constraint);
            } else {
                List<Constraint> list = new ArrayList<>();
                list.add(constraint);
                groupedConstraints.put(constraint.getTargetModelElementName(), list);
            }
        });

        return groupedConstraints;
    }

    @Override
    public Map<String, List<Constraint>> getGroupConstraintsByTypeAnsSupertypes(Map<String, List<String>> typeToSupertypeMap) {
        Map<String, List<Constraint>> groupedConstraints = new HashMap<>();
        typeToSupertypeMap.forEach((key, value) -> {
            List<Constraint> constraintList = new ArrayList<>();
            value.forEach(s -> constraintList.addAll(this.getAllConstraintsByType(s)));
            if (!constraintList.isEmpty()) {
                groupedConstraints.put(key, constraintList);
            }
        });

        return groupedConstraints;
    }

    @Override
    public boolean removeConstraintByUuid(String uuid) {
        return this.constraints.removeIf(constraint -> constraint.getUuid().equals(uuid));
    }
}
