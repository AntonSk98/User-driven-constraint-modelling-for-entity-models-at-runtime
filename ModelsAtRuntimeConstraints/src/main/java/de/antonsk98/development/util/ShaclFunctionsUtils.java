package de.antonsk98.development.util;

import de.antonsk98.development.domain.codi.model.ConstraintType;
import de.antonsk98.development.domain.shacl.DeepModel;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.XSD;
import org.topbraid.shacl.vocabulary.SH;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class to dynamically resolve function-related properties into SHACL domain.
 *
 * @author Anton Skripin
 */
public class ShaclFunctionsUtils {

    private static final Map<String, Pair<Property, XSDDatatype>> coreConstraintFunctions = new HashMap<>();
    private static final Map<String, Property> resourceFunctions = new HashMap<>();
    private static final Map<String, Resource> datatypeFunctions = new HashMap<>();

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
