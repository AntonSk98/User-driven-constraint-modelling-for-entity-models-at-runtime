package de.antonsk98.development.service.impl;

import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.domain.codi.model.Constraint;
import de.antonsk98.development.domain.codi.model.Function;
import de.antonsk98.development.domain.codi.model.ModelElement;
import de.antonsk98.development.domain.shacl.DeepModel;
import de.antonsk98.development.domain.shacl.ShaclContainer;
import de.antonsk98.development.service.api.Transformer;
import de.antonsk98.development.util.ShaclFunctionsUtils;
import lombok.SneakyThrows;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.vocabulary.SH;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Transforms constraints into SHACL shapes.
 *
 * @author Anton Skripin
 */
public class CodiToShapeModelTransformer implements Transformer<Model, CodiModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Model transform(CodiModel model) {
        DeepModel m = new DeepModel();
        model.getModel().getModelElement()
                .forEach(modelElement -> modelElement.getConstraints()
                        .forEach(constraint -> constraint.getFunctions()
                                .forEach(function -> m.add(transform(modelElement, constraint, function)))));
        return m;
    }

    /**
     * Create constraint model for a given function
     *
     * @param modelElement {@link ModelElement}
     * @param constraint   {@link Constraint}
     * @param function     {@link Function}
     * @return SHACL model for a given function
     */
    @SneakyThrows
    private Model transform(ModelElement modelElement, Constraint constraint, Function function) {
        DeepModel deepModel = new DeepModel();


        String pathUri = String.format(
                "%s%s",
                ShaclFunctionsUtils.getNamespaceByConstraintType(constraint.getConstraintType()),
                constraint.getFocusProperty());

        ShaclContainer shaclContainer = new ShaclContainer(
                deepModel,
                deepModel.createResource()
                        .addProperty(SH.path, deepModel.createProperty(pathUri))
                        .addProperty(SH.message, constraint.getMessage()),
                new ArrayList<>());

        processComplexFunction(function, shaclContainer);

        String modelName = modelElement.getName().replace(":", "-");
        deepModel.createResource(DeepModel.getShapeName(modelName), SH.NodeShape)
                .addProperty(SH.targetClass, deepModel.createResource(DeepModel.getShapeTargetClass(modelName)))
                .addProperty(SH.property, shaclContainer.getConstraintResource());
        return deepModel;
    }

    /**
     * Core method for transforming a function into a SHACL constraint.
     * The algorithm is the following:
     * 1) Find deepest not processed composite function
     * 2) If (1) returns null a given function is taking into consideration, otherwise (1)
     * 2.1) If function is non-composite -> go to (3) otherwise go to (4)
     * --------------------------------------------------------------------
     * 3) NON-COMPOSITE FUNCTION TRANSFORMATION
     * 3.1) If the function is not composite, a respective SHACL constraint resource is constructed. In this case, goes straight to (5)
     * --------------------------------------------------------------------
     * 4) COMPOSITE FUNCTION TRANSFORMATION
     * 4.1) If a function is a root element, then all its nested constraints will be added directly to it. If not, then a separate resource hierarchy is constructed
     * 4.2) All nested non-composite functions of the function are transformed into SHACL constraints and the result is saved as a list
     * 4.4) If the function is a leaf with no more nested functions, then a constructed SHACL resource will be passed to its parent
     * 4.5) If the function is an aggregate of composite functions, then all constraints associated to it are combined and a unified SHACL resource is constructed
     * --------------------------------------------------------------------
     * 5) Repeat all the steps until the function tree is not fully processed from its bottom to the root element
     *
     * @param function       {@link Function}
     * @param shaclContainer {@link ShaclContainer}
     */
    private void processComplexFunction(Function function, ShaclContainer shaclContainer) {
        if (Objects.isNull(function)) {
            return;
        }
        function.checkValidCompositeFunction();
        transformFunction(Objects.requireNonNullElse(function.getDeepestCompositeFunction(), function), shaclContainer);
    }

    /**
     * Processes given function and constructs a SHACL constraint.
     * This method deals with two types of functions:
     * 1) composite - function that contains an arbitrary number of nested functions.
     * 2) non-composite - function that has no nested functions.
     *
     * @param function       {@link  Function}
     * @param shaclContainer {@link ShaclContainer}
     */
    private void transformFunction(Function function, ShaclContainer shaclContainer) {
        function.setProcessed(true);
        if (function.isNotCompositeFunction()) {
            transformNotCompositeFunction(function, shaclContainer);
        } else {
            transformCompositeFunction(function, shaclContainer);
        }
    }

    /**
     * Transforms composite function and all its nested functions into a SHACL resource constraint.
     *
     * @param function       {@link  Function}
     * @param shaclContainer {@link  ShaclContainer}
     */
    @SneakyThrows
    private void transformCompositeFunction(Function function, ShaclContainer shaclContainer) {
        Resource resource = function.isRootFunction()
                ? shaclContainer.getConstraintResource()
                : shaclContainer.getModel().createResource();

        List<Resource> nonCompositeResources = processAllNonCompositeFunctions(function, shaclContainer);

        if (function.isLeaf()) {
            shaclContainer.addResource(resource);
        } else {
            nonCompositeResources.addAll(shaclContainer.getResourceList());
            shaclContainer.clearResourceList();
            shaclContainer.addResource(resource);
        }

        resource.addProperty(ShaclFunctionsUtils.getShaclFunctionByName(function.getName()),
                shaclContainer.getModel().createList(
                        nonCompositeResources
                                .stream()
                                .iterator()));

        processComplexFunction(function.getParent(), shaclContainer);
    }

    /**
     * Constructs resources for all non-composite functions contained in a composite function.
     *
     * @param function       not processed composite function
     * @param shaclContainer {@link ShaclContainer}
     * @return list of constructed resources for the model
     */
    private List<Resource> processAllNonCompositeFunctions(Function function, ShaclContainer shaclContainer) {
        List<Resource> resourceList = new ArrayList<>();
        function
                .getNestedFunctions()
                .stream()
                .filter(nestedFunction -> !nestedFunction.isCompositeFunction())
                .forEach(nestedNonCompositeFunction -> {
                    resourceList.add(addPropertyDynamically(nestedNonCompositeFunction, shaclContainer.getModel().createResource()));
                    nestedNonCompositeFunction.setProcessed(true);
                });
        return resourceList;
    }

    /**
     * Construct a constraint resource for a non-composite function.
     *
     * @param function       function for which a resource shall be constructed
     * @param shaclContainer {@link ShaclContainer}
     */
    @SneakyThrows
    private void transformNotCompositeFunction(Function function, ShaclContainer shaclContainer) {
        addPropertyDynamically(function, shaclContainer.getConstraintResource());
        processComplexFunction(function.getParent(), shaclContainer);
    }

    /**
     * Function that constructs a SHACL constraint using the functionality of {@link ShaclFunctionsUtils}
     * @param function function
     * @param resource resource used to construct a constraint
     * @return {@link Resource}
     */
    private Resource addPropertyDynamically(Function function, Resource resource) {
        if (ShaclFunctionsUtils.isResourceFunction(function.getName())) {
            resource.addProperty(ShaclFunctionsUtils.getPropertyFunctionByName(function.getName()), ShaclFunctionsUtils.getDatatypeResourceByFunctionName(function.getValue()));
            return resource;
        }
        resource.addProperty(
                        ShaclFunctionsUtils.getShaclFunctionByName(function.getName()),
                        function.getValue(),
                        ShaclFunctionsUtils.getShaclFunctionTypeByName(function.getName()));
        return resource;
    }
}
