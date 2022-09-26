package de.antonsk98.development.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.service.impl.CodiToRdfTransformer;
import lombok.SneakyThrows;
import org.apache.jena.rdf.model.Model;

/**
 * API to validate instance and model constraints
 */
public class ShaclApi {

    private final CodiToRdfTransformer transformer;

    public ShaclApi(CodiToRdfTransformer transformer) {
        this.transformer = transformer;
    }

    @SneakyThrows
    public Model getRdfFromCodiModel(String codiModel) {
        CodiModel codiModel1 = new ObjectMapper().readValue(codiModel, CodiModel.class);
        return transformer.transform(codiModel1);
    }
}
