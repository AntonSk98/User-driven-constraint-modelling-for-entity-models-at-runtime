package ansk.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.service.AbstractConstraintValidationService;

import java.util.List;

public class GremlinConstraintValidationService extends AbstractConstraintValidationService {

    @Override
    public Object validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        return super.validateConstraint(uuid, subgraphForValidation, constraint);
    }
}
