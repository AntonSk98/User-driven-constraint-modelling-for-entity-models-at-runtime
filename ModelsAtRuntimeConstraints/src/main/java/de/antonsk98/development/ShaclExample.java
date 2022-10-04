package de.antonsk98.development;


import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.vocabulary.SchemaDO;
import org.apache.jena.vocabulary.XSD;
import org.topbraid.shacl.vocabulary.SH;

import java.util.Iterator;
import java.util.List;

/**
 * Shacl example class that showcases the data graph and model graph construction.
 *
 * @author Anton Skripin
 */
public final class ShaclExample {

    private ShaclExample() {

    }

    public static void run() {
        Model dataModel = constructDataModel();
        Model shapeModel = constructShapeModel();
        Shapes shapes = Shapes.parse(shapeModel.getGraph());
        RDFDataMgr.write(System.out, constructDataModel(), Lang.TTL);
        System.out.println();
        RDFDataMgr.write(System.out, constructShapeModel(), Lang.TTL);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataModel.getGraph());
        ShLib.printReport(report);
        System.out.println();
        RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
    }

    public static Model constructDataModel() {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("schema", SchemaDO.NS);
        model.setNsPrefix("ex", "http://example.org/ns#");
        String val = "http://example.org/ns#";
        model
                .createResource("http://example.org/ns#Bob", SchemaDO.Person)
                .addProperty(SchemaDO.givenName, "Robert")
                .addProperty(SchemaDO.birthDate, "1971-07-07")
                .addProperty(SchemaDO.deathDate, "1972-07-07")
                .addProperty(SchemaDO.gender, "male")
                .addProperty(SchemaDO.givenName, model.createResource("Test")
                        .addProperty(SchemaDO.followup, "12")
                        .addProperty(SchemaDO.deathDate, "14")
                        .addProperty(SchemaDO.boardingGroup, "15"))
                .addProperty(SchemaDO.address, model.createResource("http://example.org/ns#NonValidAdress"))
                .addProperty(SchemaDO.address, model.createResource("http://example.org/ns#BobsAdress")
                        .addProperty(SchemaDO.streetAddress, "1600 Amphitheatre Pkway")
                        .addProperty(SchemaDO.postalCode, "12", XSDDatatype.XSDint));
        return model;
    }

    public static Model constructShapeModel() {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("schema", SchemaDO.NS);
        model.setNsPrefix("ex", "http://example.org/ns#");
        model.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model
                .createResource("http://example.org/ns#PersonShape", SH.NodeShape)
                .addProperty(SH.targetNode, model.createResource("http://example.org/ns#Bob"))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.givenName)
                        .addProperty(SH.datatype, XSD.xstring)
                        .addProperty(SH.name, "given name"))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.birthDate)
                        .addProperty(SH.lessThan, SchemaDO.deathDate)
                        .addProperty(SH.maxCount, "1", XSDDatatype.XSDint))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.gender)
                        .addProperty(SH.in, model
                                .createList(
                                        model.createLiteral("male"),
                                        model.createLiteral("female")))
                        .addProperty(SH.minCount, "1", XSDDatatype.XSDint))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.address)
                        .addProperty(SH.minCount, "2", XSDDatatype.XSDint))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.address)
                        .addProperty(SH.node, model.createResource(SchemaDO.NS + "AddressShape")));
        Iterator<Resource> resources = List.of(model.createResource().addProperty(SH.datatype, XSD.xstring), model.createResource().addProperty(SH.datatype, XSD.xint)).iterator();
        model.createResource(SchemaDO.NS + "AddressShape", SH.NodeShape)
                .addProperty(SH.closed, "true", XSDDatatype.XSDboolean)
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.streetAddress)
                        .addProperty(SH.datatype, XSD.xstring))
                .addProperty(SH.property, model.createResource()
                        .addProperty(SH.path, SchemaDO.postalCode)
                        .addProperty(SH.or, model
                                .createList(resources))
                        .addProperty(SH.minInclusive, "1", XSDDatatype.XSDint)
                        .addProperty(SH.maxInclusive, "100", XSDDatatype.XSDint));

        return model;
    }


}
