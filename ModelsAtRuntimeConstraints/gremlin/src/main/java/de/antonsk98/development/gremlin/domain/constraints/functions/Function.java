package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.antonsk98.development.gremlin.service.FunctionName;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LogicalFunction.class, name = FunctionTypes.LOGICAL_FUNCTION),
        @JsonSubTypes.Type(value = BasicFunction.class, name = FunctionTypes.BASIC_FUNCTION)
})
public abstract class Function {
    private final FunctionName functionName;

    @NotNull
    private final ReturnType returnType;

    private Function parent;

    private final List<Function> nestedFunctions;

    private boolean processed = false;

    public Function(
            FunctionName functionName,
            @NotNull(value = "Every function must have valid return type!") ReturnType returnType,
            List<Function> nestedFunctions) {
        this.functionName = functionName;
        this.returnType = returnType;

        if (Objects.isNull(nestedFunctions)) {
            this.nestedFunctions = new ArrayList<>();
        } else {
            this.nestedFunctions = nestedFunctions;
            this.nestedFunctions.forEach(function -> function.setParent(this));
        }
    }

    public FunctionName getFunctionName() {
        return functionName;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Function getParent() {
        return parent;
    }

    public void setParent(Function parent) {
        this.parent = parent;
    }

    public List<Function> getNestedFunctions() {
        return nestedFunctions;
    }

    public boolean isRoot() {
        return Objects.isNull(parent);
    }

    /**
     * Iterates over a function tree and finds a non-processed non-composite function.
     *
     * @return deepest not processed composite function
     */
    public Function getDeepestCompositeFunction() {
        AtomicReference<Function> function = new AtomicReference<>();
        boolean noNestedFunctions = this.getNestedFunctions().isEmpty();
        boolean allNestedFunctionsProcessed = this.getNestedFunctions().stream().allMatch(Function::isProcessed);
        boolean thisFunctionIsNotProcessed = !this.isProcessed();
        if (thisFunctionIsNotProcessed && (noNestedFunctions || allNestedFunctionsProcessed)) {
            return this;
        }
        for (Function nestedFunction : this.getNestedFunctions()) {
            function.set(nestedFunction.getDeepestCompositeFunction());
            if (Objects.nonNull(function.get())) {
                break;
            }
        }
        return function.get();
    }

    /**
     * Whether a target function is the deepest not yet processed function in the whole function tree hierarchy.
     *
     * @return true if the function is the deepest not processed composite function in the hierarchy
     */
    public boolean isDeepestNotProcessedCompositeFunction() {
        return this.isCompositeFunction()
                && this.getNestedFunctions().stream().noneMatch(Function::isCompositeFunction)
                && this.getNestedFunctions().stream().noneMatch(Function::isProcessed);
    }

    /**
     * Leaf is a function in a tree function hierarchy that has no more nested composite functions.
     *
     * @return true if the function is a leaf
     */
    public boolean isLeaf() {
        return this.getNestedFunctions().stream().noneMatch(Function::isCompositeFunction);
    }

    public boolean isCompositeFunction() {
        return this instanceof LogicalFunction;
    }
}
