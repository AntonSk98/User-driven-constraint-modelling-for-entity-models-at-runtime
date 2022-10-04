package de.antonsk98.development.domain.codi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Type of constraint.
 *
 * @author Anton Skripin
 */
public enum ConstraintType {
    @JsonProperty("attribute")
    ATTRIBUTE,
    @JsonProperty("association")
    ASSOCIATION
}
