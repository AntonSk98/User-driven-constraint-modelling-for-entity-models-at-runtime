package de.antonsk98.development.domain.codi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.antonsk98.development.domain.codi.deepInstance.DeepInstance;
import de.antonsk98.development.domain.codi.model.Model;
import de.antonsk98.development.service.impl.CodiDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Codi model consisting of model-space and user-space representations.
 *
 * @author Anton Skripin
 */
@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(using = CodiDeserializer.class)
public class CodiModel {
    Model model;
    DeepInstance deepInstance;
}
