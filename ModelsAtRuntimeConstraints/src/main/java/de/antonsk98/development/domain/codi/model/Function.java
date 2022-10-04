package de.antonsk98.development.domain.codi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.jena.rdf.model.Resource;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Domain of a function constraint.
 * Each function can be either a flat function with no nested functions.
 * Or it can be a composite function that has two or more nested functions, thus, building a function tree hierarchy.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Function {

    private String name;

    private String value;

    private List<Function> nestedFunctions;

    @JsonIgnore
    private Function parent;

    @JsonIgnore
    private boolean processed = false;

    @JsonIgnore
    private boolean compositeFunction = false;

    @JsonIgnore
    private Resource resource;

    /**
     * Getter for {@link #nestedFunctions}.
     * @return {@link #nestedFunctions}
     */
    public List<Function> getNestedFunctions() {
        if (Objects.isNull(this.nestedFunctions)) {
            this.nestedFunctions = new ArrayList<>();
        }
        return this.nestedFunctions;
    }

    /**
     * Setter for {@link #nestedFunctions}.
     * If this method is invoked, then it is implicitly inferred that a function is a composite function
     * and for every nested function, a parent element is the invoked object.
     * @param nestedFunctions {@link #nestedFunctions}
     */
    public void setNestedFunctions(List<Function> nestedFunctions) {
        this.nestedFunctions = nestedFunctions;
        this.nestedFunctions.forEach(nestedFunction -> nestedFunction.setParent(this));
        this.setCompositeFunction(true);
    }

    /**
     * Iterates over a function tree and finds the deepest not processed composite function.
     * @return deepest not processed composite function
     */
    public Function getDeepestCompositeFunction() {
        AtomicReference<Function> deepestFunction = new AtomicReference<>();
        if (isDeepestNotProcessedCompositeFunction()) {
            return this;
        } else {
            this.getNestedFunctions()
                    .forEach(nestedFunction -> Optional
                            .ofNullable(nestedFunction.getDeepestCompositeFunction()).ifPresent(deepestFunction::set));
            return deepestFunction.get();
        }
    }

    /**
     * Every composite function must have two or more nested function to be considered a valid composite function.
     */
    @SneakyThrows
    public void checkValidCompositeFunction() {
        if (this.isCompositeFunction() && this.getNestedFunctions().size() < 2) {
            throw new Exception("Composite function must contain at least two parameters");
        }
    }

    /**
     * Whether a function is non-composite.
     * @return true if a function is a non-composite function
     */
    public boolean isNotCompositeFunction() {
        return this.getNestedFunctions().isEmpty() && !this.isCompositeFunction();
    }

    /**
     * Whether a target function is the deepest not yet processed function in the whole function tree hierarchy.
     * @return true if the function is the deepest not processed composite function in the hierarchy
     */
    public boolean isDeepestNotProcessedCompositeFunction() {
        return this.isCompositeFunction()
                && this.getNestedFunctions().stream().noneMatch(Function::isCompositeFunction)
                && this.getNestedFunctions().stream().noneMatch(Function::isProcessed);
    }

    /**
     * Leaf is a function in a tree function hierarchy that has no more nested composite functions.
     * @return true if the function is a leaf
     */
    public boolean isLeaf() {
        return this.getNestedFunctions().stream().noneMatch(Function::isCompositeFunction);
    }

    /**
     * Root is a function that has no parent function associated with it.
     * @return true if the function is a root
     */
    public boolean isRootFunction() {
        return Objects.isNull(this.parent);
    }
}
