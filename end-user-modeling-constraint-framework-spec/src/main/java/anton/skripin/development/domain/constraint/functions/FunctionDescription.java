package anton.skripin.development.domain.constraint.functions;

import anton.skripin.development.exception.constraint.function.FunctionException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class that stores a description for every function.
 */
public class FunctionDescription {

    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String MIN_LENGTH = "MIN_LENGTH";
    public static final String MAX_LENGTH = "MAX_LENGTH";
    public static final String FOR_ALL = "FOR_ALL";

    private static final Map<String, Object> FUNCTION_TO_DESCRIPTION_MAP = new HashMap<>();

    static {
        Properties properties =  new Properties();
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
