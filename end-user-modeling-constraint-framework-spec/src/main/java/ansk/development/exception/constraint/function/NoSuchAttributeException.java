package ansk.development.exception.constraint.function;

/**
 * This exception is thrown whenever there is an attempt to attempt one of the generic function attributes that is not defined on a certain function level.
 */
public class NoSuchAttributeException extends FunctionException {

    public NoSuchAttributeException(String attributeName, Class clazz) {
        super(String.format("Function %s has no attribute '%s' defined!", clazz.getSimpleName(), attributeName));
    }
}
