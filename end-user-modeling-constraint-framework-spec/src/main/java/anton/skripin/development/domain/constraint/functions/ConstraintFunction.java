package anton.skripin.development.domain.constraint.functions;

import anton.skripin.development.domain.constraint.functions.types.*;
import anton.skripin.development.domain.template.ObjectTemplatePlaceholder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * Abstract class representing a function
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogicalFunction.class, name = FunctionType.LOGICAL_FUNCTION),
        @JsonSubTypes.Type(value = StringBasedFunction.class, name = FunctionType.STRING_BASED_FUNCTION),
        @JsonSubTypes.Type(value = CollectionBasedFunction.class, name = FunctionType.COLLECTION_BASED_FUNCTION),
        @JsonSubTypes.Type(value = RangeBasedFunction.class, name = FunctionType.RANGE_BASED_FUNCTION),
        @JsonSubTypes.Type(value = AssociationBasedFunction.class, name = FunctionType.ASSOCIATION_BASED_FUNCTION),
        @JsonSubTypes.Type(value = RuntimeFunction.class, name = FunctionType.RUNTIME_FUNCTION),
        @JsonSubTypes.Type(value = ObjectTemplatePlaceholder.class, name = FunctionType.OBJECT_TEMPLATE_PLACEHOLDER)
}
)
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ConstraintFunction {
    private String name;

    @JsonIgnore
    private ConstraintFunction parentFunction;

    public ConstraintFunction() {
    }

    /**
     * Name of a function
     *
     * @param name        name
     */
    public ConstraintFunction(String name) {
        assert StringUtils.isNotBlank(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Name of an attribute and its target type.
     * Example: <SoftwareEngineer>name
     * @return target attribute
     */
    public Optional<String> attribute() {
        return Optional.empty();
    }

    /**
     * Navigation path to another model element via association paths.
     * Example: Context: SoftwareEngineer; Navigation: works_on(Project).consists_of(Sprint) -->
     * --> this navigation results in a list of 'Sprint' model elements 
     * that can be accessed from SoftwareEngineer to Sprint via Project.
     * @return navigation path to other models elements
     */
    public Optional<String> navigation() {
        return Optional.empty();
    }

    /**
     * Most of the constraint functions must be provided with arguments.
     * For instance, a function that determines the maximum length of a string as a {@link StringBasedFunction}
     * must be provided the maximum length param
     * @return parameters for a function as a key-value map
     */
    public Optional<Map<String, String>> params() {
        return Optional.empty();
    }

    /**
     * Logical functions {@link LogicalFunction}
     * must be provided with a list of nested functions that can be resolved to a boolean value, be it true or false.
     * @return list of constraint functions
     */
    public Optional<List<ConstraintFunction>> booleanFunctions() {
        return Optional.empty();
    }

    /**
     * Collection-based functions must be provided with a lambda function.
     * This function will be resolved against every element of a collection.
     * @return constraint function
     */
    public Optional<ConstraintFunction> lambdaFunction() {
        return Optional.empty();
    }

    /**
     * Function that is defined by end-user at runtime.
     * This function does not need to have a target context because it is resolved automatically during instantiation.
     * The function must only provide a valid platform-specific query definition.
     * @return constraint function as a string
     */
    public Optional<String> runtimeFunction() {
        return Optional.empty();
    }

    public void setParentFunction(ConstraintFunction parentFunction) {
        this.parentFunction = parentFunction;
    }
}
