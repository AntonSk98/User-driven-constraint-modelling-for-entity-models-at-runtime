package anton.skripin.development.domain.constraint.functions;

import anton.skripin.development.exception.constraint.function.FunctionException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static anton.skripin.development.domain.constraint.functions.FunctionMetadata.FunctionNames.*;

/**
 * Class that stores a description for every function.
 */
public class FunctionDescription {


    private static final Map<String, Object> FUNCTION_TO_DESCRIPTION_MAP = new HashMap<>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(FunctionDescription.class.getResourceAsStream("/properties/function_to_description.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FUNCTION_TO_DESCRIPTION_MAP.put(AND, properties.get(AND));
        FUNCTION_TO_DESCRIPTION_MAP.put(OR, properties.get(OR));
        FUNCTION_TO_DESCRIPTION_MAP.put(MIN_LENGTH, properties.get(MIN_LENGTH));
        FUNCTION_TO_DESCRIPTION_MAP.put(MAX_LENGTH, properties.get(MAX_LENGTH));
        FUNCTION_TO_DESCRIPTION_MAP.put(FOR_ALL, properties.get(FOR_ALL));
        FUNCTION_TO_DESCRIPTION_MAP.put(FOR_SOME, properties.get(FOR_SOME));
        FUNCTION_TO_DESCRIPTION_MAP.put(FOR_NONE, properties.get(FOR_NONE));
        FUNCTION_TO_DESCRIPTION_MAP.put(FOR_EXACTLY, properties.get(FOR_EXACTLY));
        FUNCTION_TO_DESCRIPTION_MAP.put(GREATER_THAN, properties.get(GREATER_THAN));
        FUNCTION_TO_DESCRIPTION_MAP.put(GREATER_THAN_OR_EQUALS, properties.get(GREATER_THAN_OR_EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(LESS_THAN, properties.get(LESS_THAN));
        FUNCTION_TO_DESCRIPTION_MAP.put(LESS_THAN_OR_EQUALS, properties.get(LESS_THAN_OR_EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(EQUALS, properties.get(EQUALS));
        FUNCTION_TO_DESCRIPTION_MAP.put(UNIQUE, properties.get(UNIQUE));
        FUNCTION_TO_DESCRIPTION_MAP.put(NOT_NULL_OR_EMPTY, properties.get(NOT_NULL_OR_EMPTY));
        FUNCTION_TO_DESCRIPTION_MAP.put(MIN_CARDINALITY, properties.get(MIN_CARDINALITY));
        FUNCTION_TO_DESCRIPTION_MAP.put(MAX_CARDINALITY, properties.get(MAX_CARDINALITY));
    }

    private FunctionDescription() {

    }

    public static String descriptionByName(String functionName) {
        if (FUNCTION_TO_DESCRIPTION_MAP.containsKey(functionName)) {
            return FUNCTION_TO_DESCRIPTION_MAP.get(functionName).toString();
        }
        throw new FunctionException("No description is found for " + functionName + " function!");
    }
}
