package ansk.development.service;

import ansk.development.domain.ShaclConstraintData;
import ansk.development.domain.ShaclConstraintShape;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.mapper.ShaclConstraintMapper;
import ansk.development.service.api.ConstraintValidationService;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;

import java.util.List;

/**
 * Implenentation of {@link ConstraintValidationService}.
 */
public class ShaclConstraintValidationService extends AbstractConstraintValidationService {

    private static final ShaclConstraintMapper shaclConstraintMapper = new ShaclConstraintMapper();

    @Override
    public ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        ShaclConstraintData dataGraph = shaclConstraintMapper.mapToPlatformSpecificGraph(subgraphForValidation);
        ShaclConstraintShape shaclConstraint = shaclConstraintMapper.mapToPlatformSpecificConstraint(uuid, constraint);
        Shapes shapes = Shapes.parse(shaclConstraint.getGraph());
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph.getGraph());
        return new ConstraintValidationReport(
                constraint.getName(),
                constraint.getModelElementType(),
                report.conforms(),
                constraint.getViolationLevel(),
                constraint.getViolationMessage());
    }
}
