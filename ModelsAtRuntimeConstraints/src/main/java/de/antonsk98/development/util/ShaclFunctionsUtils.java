package de.antonsk98.development.util;

import de.antonsk98.development.domain.codi.model.ConstraintType;
import de.antonsk98.development.domain.codi.model.Function;
import de.antonsk98.development.domain.shacl.DeepModel;
import de.antonsk98.development.domain.shacl.ShaclDAO;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.XSD;
import org.topbraid.shacl.vocabulary.SH;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Helper class to dynamically resolve function-related properties into SHACL domain.
 *
 * @author Anton Skripin
 */
public class ShaclFunctionsUtils {

    private static final Map<String, Pair<Property, XSDDatatype>> coreConstraintFunctions = new HashMap<>();
    private static final Map<String, Property> resourceFunctions = new HashMap<>();
    private static final Map<String, Resource> datatypeFunctions = new HashMap<>();

    private static final Map<String, BiConsumer<Function, ShaclDAO>> customConstraintFunctions = new HashMap<>();

    static {
        coreConstraintFunctions.put("maxCount", new ImmutablePair<>(SH.maxCount, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("minCount", new ImmutablePair<>(SH.minCount, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("minLength", new ImmutablePair<>(SH.minLength, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("maxLength", new ImmutablePair<>(SH.maxLength, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("minInclusive", new ImmutablePair<>(SH.minInclusive, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("maxInclusive", new ImmutablePair<>(SH.minInclusive, XSDDatatype.XSDinteger));
        coreConstraintFunctions.put("class", new ImmutablePair<>(SH.class_, null));
        coreConstraintFunctions.put("and", new ImmutablePair<>(SH.and, null));
        coreConstraintFunctions.put("or", new ImmutablePair<>(SH.or, null));
        coreConstraintFunctions.put("in", new ImmutablePair<>(SH.in, null));
    }

    static {
        resourceFunctions.put("datatype", SH.datatype);
    }

    static {
        datatypeFunctions.put("integer", XSD.integer);
        datatypeFunctions.put("string", XSD.xstring);
    }

    static {
        String functionName = "ofType";
        customConstraintFunctions.put(functionName, (function, shaclContainer) -> {
            String ofClass = "ofClass";
            String minCardinality = "minCardinality";
            String maxCardinality = "maxCardinality";
            String ofClassValue = getParameterValueByParameterName(function, ofClass, functionName);
            String minCardinalityValue = getParameterValueByParameterNameOrDefault(function, minCardinality, "0");
            String maxCardinalityValue = getParameterValueByParameterNameOrDefault(function, maxCardinality, String.valueOf(Integer.MAX_VALUE));
            shaclContainer.getConstraintResource().addProperty(SH.qualifiedMinCount, minCardinalityValue, XSDDatatype.XSDinteger);
            shaclContainer.getConstraintResource().addProperty(SH.qualifiedMaxCount, maxCardinalityValue, XSDDatatype.XSDinteger);
            shaclContainer
                    .getConstraintResource()
                    .addProperty(
                            SH.qualifiedValueShape,
                            shaclContainer
                                    .getModel()
                                    .createResource()
                                    .addProperty(SH.class_, shaclContainer.getModel().createConstraintResource(ofClassValue)));
        });
    }

    /**
     * Retrieves a parameter value for a given parameter. If it is not present, then {@link IllegalArgumentException} is thrown.
     *
     * @param function      {@link Function}
     * @param parameterName parameter name for which a value should be retrieved
     * @param functionName  custom function name
     * @return a parameter value for a given parameter
     */
    private static String getParameterValueByParameterName(Function function, String parameterName, String functionName) {
        return getOptionalParameterValueByParameterName(function, parameterName)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s is not present in function %s", parameterName, functionName)));
    }

    /**
     * Retrieves a parameter value for a given parameter. If it is not present, then a default value is returned.
     *
     * @param function      {@link Function}
     * @param parameterName parameter name for which a value should be retrieved
     * @param defaultValue  default value
     * @return a parameter value for a given parameter
     */
    private static String getParameterValueByParameterNameOrDefault(Function function, String parameterName, String defaultValue) {
        return getOptionalParameterValueByParameterName(function, parameterName)
                .orElse(defaultValue);
    }

    /**
     * Returns optional value of the parameter by its name for a given function.
     *
     * @param function      {@link Function}
     * @param parameterName parameter name for which a value should be retrieved
     * @return {@link Optional} of a parameter value
     */
    private static Optional<String> getOptionalParameterValueByParameterName(Function function, String parameterName) {
        return function.getNestedFunctions()
                .stream()
                .filter(nestedFunction -> nestedFunction.getName().equals(parameterName))
                .map(Function::getValue)
                .findFirst();
    }

    /**
     * Whether the following function is a custom constraint function.
     *
     * @param functionName function name
     * @return true if a given function is a custom function
     */
    public static boolean isCustomConstraintFunction(String functionName) {
        return Objects.nonNull(customConstraintFunctions.get(functionName));
    }

    /**
     * Returns a custom function for a given function name.
     *
     * @param functionName function name
     * @return {@link BiConsumer} function
     */
    public static BiConsumer<Function, ShaclDAO> getCustomConstraintFunction(String functionName) {
        return customConstraintFunctions.get(functionName);
    }

    /**
     * Fetches a property for a given resource function.
     *
     * @param functionName function name
     * @return property
     */
    public static Property getPropertyFunctionByName(String functionName) {
        return resourceFunctions.get(functionName);
    }

    /**
     * Whether a function of a given name is a resource function.
     *
     * @param functionName function name
     * @return true if the function is a resource function
     */
    public static boolean isResourceFunction(String functionName) {
        return Objects.nonNull(resourceFunctions.get(functionName));
    }

    /**
     * Function that resolves a datatype for {@link SH#datatype} function.
     *
     * @param functionValue function value
     * @return resource
     */
    public static Resource getDatatypeResourceByFunctionName(String functionValue) {
        return datatypeFunctions.get(functionValue);
    }

    /**
     * Returns SHACL function by a given function name.
     *
     * @param functionName function name
     * @return SHACL function
     */
    public static Property getShaclFunctionByName(String functionName) {
        return coreConstraintFunctions.get(functionName).getLeft();
    }

    public static XSDDatatype getShaclFunctionTypeByName(String functionName) {
        return coreConstraintFunctions.get(functionName).getRight();
    }

    /**
     * Dynamically resolves namespace by constraint {@link ConstraintType}.
     *
     * @param constraintType {@link ConstraintType}
     * @return namespace for a give {@link  ConstraintType}
     */
    public static String getNamespaceByConstraintType(ConstraintType constraintType) {
        if (constraintType.equals(ConstraintType.ASSOCIATION)) {
            return DeepModel.ASSOCIATION_DATA;
        } else if (constraintType.equals(ConstraintType.ATTRIBUTE)) {
            return DeepModel.ATTRIBUTE_DATA;
        } else {
            throw new IllegalArgumentException("Unsupported constraint type");
        }
    }
}
