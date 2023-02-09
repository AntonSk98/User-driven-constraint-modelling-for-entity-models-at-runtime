package ansk.development.service;

import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.service.AbstractConstraintValidationService;

import java.util.List;

public class GremlinConstraintValidationService extends AbstractConstraintValidationService {

    @Override
    public Object validateConstraint(List<InstanceElement> subgraphForValidation, Constraint constraint) {
        return super.validateConstraint(subgraphForValidation, constraint);
    }
}
