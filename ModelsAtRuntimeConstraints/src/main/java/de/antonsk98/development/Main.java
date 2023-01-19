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
import org.apache.jena.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    static Model model = ModelFactory.createDefaultModel();

    @SneakyThrows
    public static void main(String[] args) {
        ShaclApi shaclApi = new ShaclApi(new CodiToDataModelTransformer(), new CodiToShapeModelTransformer());
        String content = "{\n" +
                "  \"definition\": {\n" +
                "    \"model\": [\n" +
                "      {\n" +
                "        \"name\": \"NamedElement:854c2eaf-5201-458a-aaf8-273ba23e3cc5\",\n" +
                "        \"template\": true,\n" +
                "        \"childOf\": [],\n" +
                "        \"associations\": [],\n" +
                "        \"attributes\": [\n" +
                "          \"9d696f3c-d33c-4c00-b6bf-b53c1400bb36:Title:String:true\"\n" +
                "        ],\n" +
                "        \"constraints\": [\n" +
                "          {\n" +
                "            \"focusProperty\": \"NamedElement-854c2eaf-5201-458a-aaf8-273ba23e3cc5-Title\",\n" +
                "            \"constraintType\": \"attribute\",\n" +
                "            \"message\": \"Constraint on 'Title' attribute is violated\",\n" +
                "            \"functions\": [\n" +
                "              {\n" +
                "                \"name\": \"or\",\n" +
                "                \"nestedFunctions\": [\n" +
                "                  {\n" +
                "                    \"name\": \"datatype\",\n" +
                "                    \"value\": \"integer\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"name\": \"datatype\",\n" +
                "                    \"value\": \"string\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"name\": \"minCount\",\n" +
                "                \"value\": \"1\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"values\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Project:854c2eaf-5201-458a-aaf8-273ba23e3cc5\",\n" +
                "        \"template\": false,\n" +
                "        \"childOf\": [\n" +
                "          \"0a9b3ce0-b67c-4ad9-8109-ef8e3a391877:854c2eaf-5201-458a-aaf8-273ba23e3cc5:NamedElement\"\n" +
                "        ],\n" +
                "        \"associations\": [\n" +
                "          \"4f6b405d-6d8f-44ea-93cc-684e130f0400:hasPart:ProjectItem:*\"\n" +
                "        ],\n" +
                "        \"attributes\": [\n" +
                "          \"d2486a68-e5ed-447e-a80f-601e9a24d3b3:Deadline:DATETIME:false\",\n" +
                "          \"19f36aa3-47bb-4363-9276-656299aa4000:Responsible:STRING:true\"\n" +
                "        ],\n" +
                "        \"values\": []\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"PremiumProject:854c2eaf-5201-458a-aaf8-273ba23e3cc5\",\n" +
                "        \"template\": false,\n" +
                "        \"childOf\": [\n" +
                "          \"a74cbb79-5008-4076-be8e-c515374ad161:854c2eaf-5201-458a-aaf8-273ba23e3cc5:NamedElement\",\n" +
                "          \"0ef6f5dd-63c1-4ee6-a9bb-377237229e9b:854c2eaf-5201-458a-aaf8-273ba23e3cc5:Project\"\n" +
                "        ],\n" +
                "        \"associations\": [],\n" +
                "        \"attributes\": [\n" +
                "          \"ccee6469-cfb6-4c8a-8bbc-e18350e7f506:state:String:true\",\n" +
                "          \"99d9bbca-ab5a-4ec3-ac08-113eaff3ccff:budget:String:true\",\n" +
                "          \"918ec21d-14e1-4078-b192-01066d94ff6a:fullName:String:false\"\n" +
                "        ],\n" +
                "        \"constraints\": [\n" +
                "          {\n" +
                "            \"focusProperty\": \"PremiumProject-854c2eaf-5201-458a-aaf8-273ba23e3cc5-hasPart\",\n" +
                "            \"constraintType\": \"association\",\n" +
                "            \"message\": \"Association constraint is violated\",\n" +
                "            \"functions\": [\n" +
                "              {\n" +
                "                \"name\": \"ofType\",\n" +
                "                \"nestedFunctions\": [\n" +
                "                  {\n" +
                "                    \"name\": \"ofClass\",\n" +
                "                    \"value\": \"Todo-854c2eaf-5201-458a-aaf8-273ba23e3cc5\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"name\": \"minCardinality\",\n" +
                "                    \"value\": \"2\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"name\": \"maxCardinality\",\n" +
                "                    \"value\": \"2\"\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"values\": [\n" +
                "          \"aa58405d-fb7a-4bc8-a6f7-107f6018afbe:ATTRIBUTE:(Premium project:true)\"\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"configuration\": [\n" +
                "    [\n" +
                "      {\n" +
                "        \"instanceId\": \"8859b0cd-1415-4421-ad27-603c0ec64388\",\n" +
                "        \"instanceOf\": \"NamedElement\",\n" +
                "        \"identity\": \"854c2eaf-5201-458a-aaf8-273ba23e3cc5\"\n" +
                "      },\n" +
                "      [],\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"8859b0cd-1415-4421-ad27-603c0ec64388\",\n" +
                "          \"key\": \"Title\",\n" +
                "          \"value\": \"\",\n" +
                "          \"isFinal\": false\n" +
                "        }\n" +
                "      ],\n" +
                "      []\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"instanceId\": \"c5802e7f-fb6d-42f6-852f-c7d19cfaa908\",\n" +
                "        \"instanceOf\": \"Project\",\n" +
                "        \"identity\": \"854c2eaf-5201-458a-aaf8-273ba23e3cc5\"\n" +
                "      },\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"c5802e7f-fb6d-42f6-852f-c7d19cfaa908\",\n" +
                "          \"parentInstanceId\": \"8859b0cd-1415-4421-ad27-603c0ec64388\"\n" +
                "        }\n" +
                "      ],\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"c5802e7f-fb6d-42f6-852f-c7d19cfaa908\",\n" +
                "          \"key\": \"Deadline\",\n" +
                "          \"value\": \"\",\n" +
                "          \"isFinal\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"c5802e7f-fb6d-42f6-852f-c7d19cfaa908\",\n" +
                "          \"key\": \"Responsible\",\n" +
                "          \"value\": \"Anton Skripin\",\n" +
                "          \"isFinal\": false\n" +
                "        }\n" +
                "      ],\n" +
                "      []\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "        \"instanceOf\": \"PremiumProject\",\n" +
                "        \"identity\": \"854c2eaf-5201-458a-aaf8-273ba23e3cc5\"\n" +
                "      },\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"parentInstanceId\": \"c5802e7f-fb6d-42f6-852f-c7d19cfaa908\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"parentInstanceId\": \"49eb00c6-e0b1-41a8-84aa-a9ea57f86948\"\n" +
                "        }\n" +
                "      ],\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"key\": \"state\",\n" +
                "          \"value\": \"NOT STARTED\",\n" +
                "          \"isFinal\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"key\": \"budget\",\n" +
                "          \"value\": \"10000\",\n" +
                "          \"isFinal\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"key\": \"fullName\",\n" +
                "          \"value\": \"Premium project\",\n" +
                "          \"isFinal\": true\n" +
                "        }\n" +
                "      ],\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"byRelation\": \"hasPart\",\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"targetInstanceId\": \"6e9ba750-3faf-47c8-90f2-9fc7aa554405\",\n" +
                "          \"isFinal\": false\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"byRelation\": \"hasPart\",\n" +
                "          \"instanceId\": \"4282980a-bf20-4f75-8f7a-434f6f8e7a54\",\n" +
                "          \"targetInstanceId\": \"68f841fe-e463-4b7f-9b87-68c54a82b372\",\n" +
                "          \"isFinal\": false\n" +
                "        }\n" +
                "      ]\n" +
                "    ],\n" +
                "    [\n" +
                "      {\n" +
                "        \"instanceId\": \"49eb00c6-e0b1-41a8-84aa-a9ea57f86948\",\n" +
                "        \"instanceOf\": \"NamedElement\",\n" +
                "        \"identity\": \"854c2eaf-5201-458a-aaf8-273ba23e3cc5\"\n" +
                "      },\n" +
                "      [],\n" +
                "      [\n" +
                "        {\n" +
                "          \"id\": 0,\n" +
                "          \"instanceId\": \"49eb00c6-e0b1-41a8-84aa-a9ea57f86948\",\n" +
                "          \"key\": \"Title\",\n" +
                "          \"value\": \"Premium project item\",\n" +
                "          \"isFinal\": false\n" +
                "        }\n" +
                "      ],\n" +
                "      []\n" +
                "    ]\n" +
                "  ]\n" +
                "}\n";
        RDFDataMgr.write(System.out, shaclApi.getShapeModelFromCodiModel(content), Lang.TTL);
        RDFDataMgr.write(System.out, shaclApi.validateConstraints(content).getModel(), Lang.TTL);
        RDFDataMgr.write(System.out, shaclApi.getDataModelFromCodiModel(content), Lang.TTL);
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
