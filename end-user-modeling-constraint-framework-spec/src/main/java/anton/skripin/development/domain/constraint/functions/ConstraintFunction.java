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
    public abstract Optional<String> attribute();

    /**
     * Navigation path to another model element via association paths.
     * Example: Context: SoftwareEngineer; Navigation: works_on(Project).consists_of(Sprint) -->
     * --> this navigation results in a list of 'Sprint' model elements 
     * that can be accessed from SoftwareEngineer to Sprint via Project.
     * @return navigation path to other models elements
     */
    public abstract Optional<String> navigation();

    /**
     * Most of the constraint functions must be provided with arguments.
     * For instance, a function that determines the maximum length of a string as a {@link StringBasedFunction}
     * must be provided the maximum length param
     * @return parameters for a function as a key-value map
     */
    public abstract Optional<Map<String, String>> params();

    /**
     * Logical functions {@link LogicalFunction}
     * must be provided with a list of nested functions that can be resolved to a boolean value, be it true or false.
     * @return list of constraint functions
     */
    public abstract Optional<List<ConstraintFunction>> booleanFunctions();

    /**
     * Collection-based functions must be provided with a lambda function.
     * This function will be resolved against every element of a collection.
     * @return constraint function
     */
    public abstract Optional<ConstraintFunction> lambdaFunction();

    public void setParentFunction(ConstraintFunction parentFunction) {
        this.parentFunction = parentFunction;
    }
}
