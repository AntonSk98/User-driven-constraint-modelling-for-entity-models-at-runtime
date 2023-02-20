package ansk.development.service.api;

import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.constraint.functions.FunctionType;
import ansk.development.domain.template.Template;

import java.util.List;

/**
 * Provides functions that fill up a {@link ConstraintFunction} with default values formin an abstract syntax
 * to facilitate the creation of constraint for an end-user.
 */
public interface TemplateFunctionService {

    /**
     * Returns all templates.
     *
     * @return list of {@link Template}
     */
    List<Template> getAllTemplates();

    /**
     * Returns all template functions by {@link FunctionType}.
     *
     * @param functionType {@link FunctionType}
     * @return list of {@link Template}
     */
    List<Template> getAllTemplatesOfFunctionType(String functionType);

    /**
     * Returns all template functions by the name of a function.
     *
     * @param functionName name of a function
     * @return {@link Template}
     */
    Template getTemplateByFunctionName(String functionName);

    /**
     * Add new function template at runtime.
     *
     * @param functionName function name
     * @param description description
     * @param functionType function type
     * @param template     -> concrete syntax of a constraint engine
     */

    void addNewTemplate(String functionName, String description, String functionType, String template);

    /**
     * Updates with a new template.
     *
     * @param functionName function name used for update
     * @param description description
     * @param functionType function type used for update
     */
    void updateTemplate(String functionName, String description, String functionType, String templateFunction);

    /**
     * Removes a template function by its name.
     *
     * @param functionName function name
     * @return true if a function is removed successfully
     */
    boolean deleteTemplateByFunctionName(String functionName);

    /**
     * Removes a template function by its name and type
     *
     * @param functionName function name
     * @param functionType function type
     * @return true if a function is removed successfully
     */
    boolean deleteTemplateByFunctionNameAndType(String functionName, String functionType);

    /**
     * Deletes all template functions and recreate predefined functions
     */
    void resetFunctionTemplates();
}
