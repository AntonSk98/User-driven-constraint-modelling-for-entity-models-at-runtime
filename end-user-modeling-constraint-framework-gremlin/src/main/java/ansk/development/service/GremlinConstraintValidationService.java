package ansk.development.service;

import ansk.development.dsl.ConstraintGraphTraversalSource;
import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.constraint.ConstraintValidationReport;
import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.exception.constraint.ConstraintValidationException;
import anton.skripin.development.service.AbstractConstraintValidationService;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.List;

public class GremlinConstraintValidationService extends AbstractConstraintValidationService {

    private static final GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();

    @Override
    public ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        try(
                ConstraintGraphTraversalSource gremlinGraph = constraintMapper.mapToPlatformSpecificGraph(subgraphForValidation);
                GraphTraversal<?, Boolean> gremlinConstraint = constraintMapper.mapToPlatformSpecificConstraint(uuid, constraint)) {
                return new ConstraintValidationReport(
                        constraint.getName(),
                        constraint.getModelElementType(),
                        gremlinGraph.isValid(gremlinConstraint),
                        constraint.getViolationLevel(),
                        constraint.getViolationMessage());
        } catch (Exception e) {
            throw new ConstraintValidationException(e);
        }
    }
}
