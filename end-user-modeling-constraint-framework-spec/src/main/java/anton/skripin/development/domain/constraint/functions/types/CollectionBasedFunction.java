package anton.skripin.development.domain.constraint.functions.types;

import anton.skripin.development.domain.constraint.functions.ConstraintFunction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static anton.skripin.development.domain.ValidationUtils.*;

/**
 * Defines an operation that must be performed on a collection with every traversal step satisfying a true condition.
 */
@Getter
public class CollectionBasedFunction extends ConstraintFunction {

    private final String navigation;

    private final ConstraintFunction lambdaFunction;

    private final Map<String, String> params;


    /**
     * Name of a function
     *
     * @param name           name
     * @param navigation     path to a collection attribute relatively to the context path
     * @param lambdaFunction {@link ConstraintFunction}
     */
    @JsonCreator
    public CollectionBasedFunction(
            @JsonProperty("name") String name,
            @JsonProperty("navigation") String navigation,
            @JsonProperty("lambdaFunction") ConstraintFunction lambdaFunction,
            @JsonProperty("params") Map<String, String> params) {
        super(name);
        validateNavigation(navigation);
        validateLambdaFunctionHasNoNavigation(name, lambdaFunction);
        this.navigation = navigation;
        this.lambdaFunction = lambdaFunction;
        this.params = params;
        this.lambdaFunction.setParentFunction(this);
    }

    public CollectionBasedFunction(
            String name,
            String navigation,
            ConstraintFunction constraintFunction,
            Map<String, String> params,
            boolean asTemplate) {
        super(name);
        if (!asTemplate) {
            validateNavigation(navigation);
            validateLambdaFunctionHasNoNavigation(name, constraintFunction);
        }
        this.navigation = navigation;
        this.lambdaFunction = constraintFunction;
        this.params = params;
        this.lambdaFunction.setParentFunction(this);
    }

    @Override
    public Optional<String> attribute() {
        return Optional.empty();
    }

    @Override
    public Optional<String> navigation() {
        return Optional.of(navigation);
    }

    @Override
    public Optional<Map<String, String>> params() {
        return Optional.of(params);
    }

    @Override
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        return Optional.empty();
    }

    @Override
    public Optional<ConstraintFunction> lambdaFunction() {
        return Optional.of(lambdaFunction);
    }
}
