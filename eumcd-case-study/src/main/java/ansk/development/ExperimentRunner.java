package ansk.development;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

import static ansk.development.AbstractDataGenerator.generateIntervalsByAddition;

/**
 * Static class that starts experiments.
 */
public class ExperimentRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentRunner.class);

    private static final String EVALUATION_ONE = "graph transformation";
    private static final String EVALUATION_TWO = "attribute-based constraint transformation";
    private static final String EVALUATION_THREE = "collection-based constraint transformation";
    private static final String EVALUATION_FOUR = "constraint validation time";
    private static final String EVALUATION_FIVE = "all in one";

    private ExperimentRunner() {

    }

    /**
     * Runs experiments.
     * 1) Measures time to transform graph from abstract to platform-specific syntax
     * 2) Measures time to transform attribute-based constraints from abstract to platform-specific syntax
     * 3) Measures time to transform collection-based constraints from abstract to platform-specific syntax
     * 4) Measures time to validate constraints when graph and constraints are already mapped to platform-specific syntax
     * 5) Measures time to validate constraint -> it includes graph and constraint transformation as well as the validation itself
     * <p>
     * Upon completion of the experiments, the respective charts are saved.
     */
    public static void runExperiment() {
        GraphTransformationTest graphTransformationTest = new GraphTransformationTest();
        ChartConstructor chartConstructor = new ChartConstructor();

        var results = measureTime(() ->
                Map.of(
                        EVALUATION_ONE, graphTransformationTest.transformationGraphExperiment(generateIntervalsByAddition(100, 5)),
                        EVALUATION_TWO, graphTransformationTest.attributeBasedConstraintsExperiment(generateIntervalsByAddition(100, 5)),
                        EVALUATION_THREE, graphTransformationTest.collectionBasedConstraintsExperiment(generateIntervalsByAddition(100, 5)),
                        EVALUATION_FOUR, graphTransformationTest.checkConstraintValidationTime(generateIntervalsByAddition(100, 5)),
                        EVALUATION_FIVE, graphTransformationTest.validateConstraintExperiment(generateIntervalsByAddition(100, 5))
                )
        );


        chartConstructor.saveChart(EVALUATION_ONE, results.get(EVALUATION_ONE).getGremlinData(), results.get(EVALUATION_ONE).getShaclData());
        chartConstructor.saveChart(EVALUATION_TWO, results.get(EVALUATION_TWO).getGremlinData(), results.get(EVALUATION_TWO).getShaclData());
        chartConstructor.saveChart(EVALUATION_THREE, results.get(EVALUATION_THREE).getGremlinData(), results.get(EVALUATION_THREE).getShaclData());
        chartConstructor.saveChart(EVALUATION_FOUR, results.get(EVALUATION_FOUR).getGremlinData(), results.get(EVALUATION_FOUR).getShaclData());
        chartConstructor.saveChart(EVALUATION_FIVE, results.get(EVALUATION_FIVE).getGremlinData(), results.get(EVALUATION_FIVE).getShaclData());
    }

    private static Map<String, ResultData> measureTime(Supplier<Map<String, ResultData>> experimentMethods) {
        long startTime = System.currentTimeMillis();
        var results = experimentMethods.get();
        long finishTime = System.currentTimeMillis();
        LOGGER.info("The experiments are finished. It took {} ms in total", finishTime - startTime);
        return results;
    }
}
