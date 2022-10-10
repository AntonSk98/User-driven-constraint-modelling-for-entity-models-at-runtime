package de.antonsk98.development;

import de.antonsk98.development.api.ShaclApi;
import de.antonsk98.development.service.impl.CodiToDataModelTransformer;
import de.antonsk98.development.service.impl.CodiToShapeModelTransformer;
import lombok.SneakyThrows;
import org.apache.jena.ext.com.google.common.io.Resources;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    static Model model = ModelFactory.createDefaultModel();

    @SneakyThrows
    public static void main(String[] args) {
        ShaclApi shaclApi = new ShaclApi(new CodiToDataModelTransformer(), new CodiToShapeModelTransformer());
        String content = Files.readString(Path.of(Resources.getResource("model_and_configuration.json").toURI()));
        RDFDataMgr.write(System.out, shaclApi.getShapeModelFromCodiModel(content), Lang.TTL);
        RDFDataMgr.write(System.out, shaclApi.validateConstraints(content).getModel(), Lang.TTL);
        RDFDataMgr.write(System.out, shaclApi.getDataModelFromCodiModel(content), Lang.TTL);
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
