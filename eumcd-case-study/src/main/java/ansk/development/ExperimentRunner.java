/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

import static ansk.development.AbstractDataGenerator.generateIntervalsByAddition;
import static ansk.development.GraphTransformationTest.runSeveralTimes;

/**
 * Static class that starts experiments.
 */
public class ExperimentRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentRunner.class);

    private static final String EVALUATION_ONE = "Graph transformation experiment";
    private static final String EVALUATION_TWO = "Constraint mapping experiment";
    private static final String EVALUATION_THREE = "Instance-object-nets constraint mapping experiment";
    private static final String EVALUATION_FOUR = "Constraint validation time experiment";
    private static final String EVALUATION_FIVE = "Overall constraint validation experiment";

    private static final String TOTAL_NUMBER_OF_INSTANCES = "Total number of instances";

    private static final String TOTAL_NUMBER_OF_CONSTRAINT_FUNCTIONS = "Total number of constraint functions";

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
//                        EVALUATION_ONE, runSeveralTimes(1, 5, () -> graphTransformationTest.transformationGraphExperiment(generateIntervalsByAddition(100, 5))),
//                        EVALUATION_TWO, runSeveralTimes(1, 5, () -> graphTransformationTest.attributeBasedConstraintsExperiment(generateIntervalsByAddition(100, 5))),
//                        EVALUATION_THREE,  runSeveralTimes(1, 5, () -> graphTransformationTest.collectionBasedConstraintsExperiment(generateIntervalsByAddition(100, 5))),
                        EVALUATION_FOUR, runSeveralTimes(1, 5, () -> graphTransformationTest.checkConstraintValidationTime(generateIntervalsByAddition(50, 80)))
//                        EVALUATION_FIVE, runSeveralTimes(1, 5, () -> graphTransformationTest.validateConstraintExperiment(generateIntervalsByAddition(100, 5)))
                )
        );


//        chartConstructor.saveChart(EVALUATION_ONE, TOTAL_NUMBER_OF_INSTANCES, results.get(EVALUATION_ONE).getGremlinData(), results.get(EVALUATION_ONE).getShaclData());
//        chartConstructor.saveChart(EVALUATION_TWO, TOTAL_NUMBER_OF_CONSTRAINT_FUNCTIONS, results.get(EVALUATION_TWO).getGremlinData(), results.get(EVALUATION_TWO).getShaclData());
//        chartConstructor.saveChart(EVALUATION_THREE, TOTAL_NUMBER_OF_CONSTRAINT_FUNCTIONS, results.get(EVALUATION_THREE).getGremlinData(), results.get(EVALUATION_THREE).getShaclData());
        chartConstructor.saveChart(EVALUATION_FOUR, TOTAL_NUMBER_OF_CONSTRAINT_FUNCTIONS, results.get(EVALUATION_FOUR).getGremlinData(), results.get(EVALUATION_FOUR).getShaclData());
//        chartConstructor.saveChart(EVALUATION_FIVE, TOTAL_NUMBER_OF_INSTANCES, results.get(EVALUATION_FIVE).getGremlinData(), results.get(EVALUATION_FIVE).getShaclData());
    }

    private static Map<String, ResultData> measureTime(Supplier<Map<String, ResultData>> experimentMethods) {
        long startTime = System.currentTimeMillis();
        var results = experimentMethods.get();
        long finishTime = System.currentTimeMillis();
        LOGGER.info("The experiments are finished. It took {} ms in total", finishTime - startTime);
        return results;
    }
}
