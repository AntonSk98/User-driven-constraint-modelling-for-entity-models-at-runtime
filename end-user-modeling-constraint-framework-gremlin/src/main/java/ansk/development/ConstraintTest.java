package ansk.development;

public class ConstraintTest {

    public static String SIMPLE_CONSTRAINT = "{\n" +
            "  \"uuid\": \"5795852a-1d4e-4994-ba18-863bf22ba5f7\",\n" +
            "  \"name\": \"Provide a meaningful name for a constraint.\",\n" +
            "  \"modelElementUuid\": \"#\",\n" +
            "  \"modelElementType\": \"SoftwareEngineer\",\n" +
            "  \"violationMessage\": \"Provide a meaningful message that will be delivered in case of constraint violation\",\n" +
            "  \"violationLevel\": \"ERROR\",\n" +
            "  \"constraintFunction\": {\n" +
            "    \"@type\": \"STRING_BASED_FUNCTION\",\n" +
            "    \"name\": \"MAX_LENGTH\",\n" +
            "    \"attribute\": \"<SoftwareEngineer>name\",\n" +
            "    \"params\": {\n" +
            "      \"max_length\": \"5\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static String FIRST_CONSTRAINT = "{\n" +
            "  \"uuid\": \"5795852a-1d4e-4994-ba18-863bf22ba5f7\",\n" +
            "  \"name\": \"Provide a meaningful name for a constraint.\",\n" +
            "  \"modelElementUuid\": \"#\",\n" +
            "  \"modelElementType\": \"SoftwareEngineer\",\n" +
            "  \"violationMessage\": \"Provide a meaningful message that will be delivered in case of constraint violation\",\n" +
            "  \"violationLevel\": \"ERROR\",\n" +
            "  \"constraintFunction\": {\n" +
            "    \"@type\": \"LOGICAL_FUNCTION\",\n" +
            "    \"name\": \"OR\",\n" +
            "    \"booleanFunctions\": [\n" +
            "      {\n" +
            "        \"@type\": \"COLLECTION_BASED_FUNCTION\",\n" +
            "        \"name\": \"FOR_ALL\",\n" +
            "        \"navigation\": \"works_on(Project).consists_of(Sprint)\",\n" +
            "        \"lambdaFunction\": {\n" +
            "          \"@type\": \"STRING_BASED_FUNCTION\",\n" +
            "          \"name\": \"MIN_LENGTH\",\n" +
            "          \"attribute\": \"<Sprint>name\",\n" +
            "          \"params\": {\n" +
            "            \"min_length\": \"1\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"@type\": \"LOGICAL_FUNCTION\",\n" +
            "        \"name\": \"AND\",\n" +
            "        \"booleanFunctions\": [\n" +
            "          {\n" +
            "            \"@type\": \"COLLECTION_BASED_FUNCTION\",\n" +
            "            \"name\": \"FOR_ALL\",\n" +
            "            \"navigation\": \"takes_part_in(Sprint)\",\n" +
            "            \"lambdaFunction\": {\n" +
            "              \"@type\": \"STRING_BASED_FUNCTION\",\n" +
            "              \"name\": \"MAX_LENGTH\",\n" +
            "              \"attribute\": \"<Sprint>name\",\n" +
            "              \"params\": {\n" +
            "                \"max_length\": \"10\"\n" +
            "              }\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"@type\": \"STRING_BASED_FUNCTION\",\n" +
            "            \"name\": \"MAX_LENGTH\",\n" +
            "            \"attribute\": \"<SoftwareEngineer>name\",\n" +
            "            \"params\": {\n" +
            "              \"max_length\": \"5\"\n" +
            "            }\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
}
