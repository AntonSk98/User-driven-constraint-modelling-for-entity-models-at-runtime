package de.antonsk98.development;

import de.antonsk98.development.api.ShaclApi;
import de.antonsk98.development.service.impl.CodiToRdfTransformer;
import lombok.SneakyThrows;
import org.apache.jena.ext.com.google.common.io.Resources;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        ShaclApi shaclApi = new ShaclApi(new CodiToRdfTransformer());
        String content = Files.readString(Path.of(Resources.getResource("model_and_configuration.json").toURI()));
        RDFDataMgr.write(System.out, shaclApi.getRdfFromCodiModel(content), Lang.TTL);
    }
}
