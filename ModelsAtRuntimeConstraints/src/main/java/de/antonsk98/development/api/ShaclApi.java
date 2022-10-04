package de.antonsk98.development.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.antonsk98.development.domain.codi.CodiModel;
import de.antonsk98.development.service.impl.CodiToDataModelTransformer;
import de.antonsk98.development.service.impl.CodiToShapeModelTransformer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;

/**
 * API to validate instance and model constraints.
 *
 * @author Anton Skripin
 */
@AllArgsConstructor
public class ShaclApi {

    private final CodiToDataModelTransformer codiToDataModelTransformer;

    private final CodiToShapeModelTransformer codiToShapeModelTransformer;

    @SneakyThrows
    public Model getDataModelFromCodiModel(String codiModel) {
        return codiToDataModelTransformer.transform(new ObjectMapper().readValue(codiModel, CodiModel.class));
    }

    @SneakyThrows
    public Model getShapeModelFromCodiModel(String codiModel) {
        return codiToShapeModelTransformer.transform(new ObjectMapper().readValue(codiModel, CodiModel.class));
    }

    @SneakyThrows
    public ValidationReport validateConstraints(String codiModel) {
        CodiModel deserializedModel = new ObjectMapper().readValue(codiModel, CodiModel.class);
        Model dataModel = codiToDataModelTransformer.transform(deserializedModel);
        Model shapeModel = codiToShapeModelTransformer.transform(deserializedModel);
        Shapes shapes = Shapes.parse(shapeModel.getGraph());
        ValidationReport report = ShaclValidator.get().validate(shapes, dataModel.getGraph());
        return report;
    }
}
