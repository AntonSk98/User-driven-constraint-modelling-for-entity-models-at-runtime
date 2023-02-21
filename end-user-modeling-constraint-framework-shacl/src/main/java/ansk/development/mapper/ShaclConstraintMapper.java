package ansk.development.mapper;

import ansk.development.domain.*;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.instance.InstanceElement;
import org.apache.jena.rdf.model.Resource;

import java.util.List;
import java.util.Objects;

import static ansk.development.domain.constraint.functions.FunctionType.RUNTIME_FUNCTION;

/**
 * Implementation of {@link AbstractToPSConstraintMapper}.
 */
public class ShaclConstraintMapper implements AbstractToPSConstraintMapper<ShaclConstraintData, ShaclConstraintShape> {


    @Override
    public ShaclConstraintShape mapToPlatformSpecificConstraint(String instanceUuid, Constraint constraint) {
        ConstraintFunction constraintFunction = constraint.getConstraintFunction();
        Resource shaclConstraint = mapFunction(instanceUuid, constraintFunction, true, new ShaclConstraintShape());
        return (ShaclConstraintShape) shaclConstraint.getModel();
    }

    private ShaclConstraint initializeConstraintContext(String instanceUuid) {
        ShaclConstraint shaclConstraint = new ShaclConstraint();
        ShaclConstraintShape shaclConstraintShape = new ShaclConstraintShape();
        shaclConstraint.setNested(false);
        shaclConstraintShape.getTargetInstance(instanceUuid);
        shaclConstraint.setContext(shaclConstraintShape);
        return shaclConstraint;
    }

    private Resource mapFunction(String instanceUuid, ConstraintFunction constraintFunction, boolean initial, ShaclConstraintShape shaclConstraintShape) {
        ShaclConstraint shaclConstraint = new ShaclConstraint();
        shaclConstraint.setNested(!initial);
        shaclConstraint.setContext(shaclConstraintShape);
        if (initial) {
            shaclConstraintShape.getTargetInstance(instanceUuid);
        }
        Resource resource;
        constraintFunction.runtimeFunction().ifPresent(shaclConstraint::setRuntimeFunction);
        constraintFunction.attribute().map(AttributeUtils::getAttributeRoot).ifPresent(shaclConstraint::setAttribute);
        constraintFunction.navigation().map(NavigationUtils::getNavigationRoot).ifPresent(shaclConstraint::setNavigation);
        constraintFunction
                .lambdaFunction()
                .ifPresent(lambdaFunction -> shaclConstraint.setLambdaFunction(mapFunction(instanceUuid, lambdaFunction, false, shaclConstraintShape)));
        constraintFunction.booleanFunctions().ifPresent(booleanFunctions -> {
            booleanFunctions.forEach(booleanFunction -> shaclConstraint.addNestedFunction(mapFunction(instanceUuid, booleanFunction, false, shaclConstraintShape)));
        });
        constraintFunction.params().ifPresent(shaclConstraint::setParams);
        if (constraintFunction.runtimeFunction().isPresent()) {
            resource = ShaclFunctionMapper.CONSTRAINTS_MAP.get(RUNTIME_FUNCTION).apply(shaclConstraint);
        } else {
            resource = ShaclFunctionMapper.CONSTRAINTS_MAP.get(constraintFunction.getName()).apply(shaclConstraint);
        }
        return resource;
    }

    @Override
    public ShaclConstraintData mapToPlatformSpecificGraph(List<InstanceElement> graph) {
        ShaclConstraintData model = new ShaclConstraintData();
        graph.forEach(model::createInstance);

        graph
                .stream()
                .filter(instanceElement -> Objects.nonNull(instanceElement.getLinks()))
                .flatMap(instanceElement -> instanceElement.getLinks().stream())
                .forEach(model::addLinkToInstance);

        return model;
    }
}
