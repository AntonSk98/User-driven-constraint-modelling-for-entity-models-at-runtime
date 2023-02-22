package ansk.development.service;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.dsl.ConstraintGraphTraversalSource;
import ansk.development.exception.constraint.ConstraintValidationException;
import ansk.development.mapper.GremlinConstraintMapper;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.util.List;

public class GremlinConstraintValidationService extends AbstractConstraintValidationService {

    private static final GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();

    @Override
    public ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        try (
                ConstraintGraphTraversalSource gremlinGraph = constraintMapper.mapToPlatformSpecificGraph(subgraphForValidation);
                GraphTraversal<?, Boolean> gremlinConstraint = constraintMapper.mapToPlatformSpecificConstraint(uuid, constraint)) {
            boolean isValid = gremlinGraph.isValid(gremlinConstraint);
            gremlinConstraint.close();
            gremlinConstraint.notifyClose();
            gremlinGraph.close();
            return new ConstraintValidationReport(
                    constraint.getName(),
                    constraint.getModelElementType(),
                    isValid,
                    constraint.getViolationLevel(),
                    constraint.getViolationMessage());
        } catch (Exception e) {
            throw new ConstraintValidationException(e);
        }
    }
}
