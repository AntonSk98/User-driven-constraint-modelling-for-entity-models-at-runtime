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

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.functions.ConstraintFunction;
import ansk.development.domain.instance.InstanceElement;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphTransformationTest {

    private static void removeFirstN(int number, TreeMap<Double, Double> gremlinResults, TreeMap<Double, Double> shaclResults) {
        for (int i = 0; i < number; i++) {
            gremlinResults.remove(gremlinResults.firstKey());
            shaclResults.remove(shaclResults.firstKey());
        }
    }

    /**
     * Checks time SHACL and Gremlin engines need to validate a constraint.
     * This function does not take into account time required to map a graph and a constraint from an abstract to platform-specific syntax.
     *
     * @param intervals set of element numbers that should be constructed for every evaluation step
     * @return pair of evaluation data. Left one is for Gremlin. Right one is for Shacl
     */
    public ResultData checkConstraintValidationTime(List<Double> intervals) {
        TreeMap<Double, Double> gremlinResults = new TreeMap<>();
        TreeMap<Double, Double> shaclResults = new TreeMap<>();
        intervals.forEach(interval -> {
            List<InstanceElement> graph = AbstractDataGenerator.generateDataForGraphTransformation(interval);
            Constraint attributeConstraint = AbstractDataGenerator.combineConstraintFunctionsIntoOneConstraint(AbstractDataGenerator.attributeBasedFunctions(interval));
            String randomUuid = AbstractDataGenerator.getRandomUuidByType(graph, AbstractDataGenerator.SOFTWARE_ENGINEER_TYPE);

            var gremlinGraph = Configuration.gremlinConstraintMapper().mapToPlatformSpecificGraph(graph);
            var gremlinConstraint = Configuration.gremlinConstraintMapper().mapToPlatformSpecificConstraint(randomUuid, attributeConstraint);
            gremlinResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> gremlinGraph.isValid(gremlinConstraint))
            );

            var shaclGraph = Configuration.shaclConstraintMapper().mapToPlatformSpecificGraph(graph);
            var shaclConstraint = Shapes.parse(Configuration.shaclConstraintMapper().mapToPlatformSpecificConstraint(randomUuid, attributeConstraint).getGraph());

            shaclResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> ShaclValidator.get().validate(shaclConstraint, shaclGraph.getGraph()).conforms())
            );
        });
//        removeFirstN(2, gremlinResults, shaclResults);
        return new ResultData(gremlinResults, shaclResults);
    }

    /**
     * Evaluates performance aspect of constraint checking that is constructed from an abstract, specification-based constraint.
     * Before asking an engine whether a constraint is valid or not, graph and constraint for evaluation is transformed from an abstract syntax.
     *
     * @param intervals set of element numbers that should be constructed for every evaluation step
     * @return pair of evaluation data. Left one is for Gremlin. Right one is for Shacl
     */
    public ResultData validateConstraintExperiment(List<Double> intervals) {
        TreeMap<Double, Double> gremlinResults = new TreeMap<>();
        TreeMap<Double, Double> shaclResults = new TreeMap<>();
        intervals.forEach(interval -> {
            List<InstanceElement> graph = AbstractDataGenerator.generateDataForGraphTransformation(interval);
            Constraint attributeConstraint = AbstractDataGenerator.combineConstraintFunctionsIntoOneConstraint(AbstractDataGenerator.attributeBasedFunctions(interval));
            String randomUuid = AbstractDataGenerator.getRandomUuidByType(graph, AbstractDataGenerator.SOFTWARE_ENGINEER_TYPE);
            gremlinResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> Configuration.gremlinConstraintValidationService().validateConstraint(randomUuid, graph, attributeConstraint))
            );

            shaclResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> Configuration.shaclConstraintValidationService().validateConstraint(randomUuid, graph, attributeConstraint))
            );
        });
//        removeFirstN(2, gremlinResults, shaclResults);
        return new ResultData(gremlinResults, shaclResults);
    }

    /**
     * Evaluates time Gremlin and Shacl need to construct platform-specific graph from a specification-based syntax.
     *
     * @param intervals set of element numbers that should be constructed for every evaluation step
     * @return pair of evaluation data. Left one is for Gremlin. Right one is for Shacl
     */
    public ResultData transformationGraphExperiment(List<Double> intervals) {
        TreeMap<Double, Double> gremlinResults = new TreeMap<>();
        TreeMap<Double, Double> shaclResults = new TreeMap<>();
        intervals.forEach(interval -> {
            List<InstanceElement> graph = AbstractDataGenerator.generateDataForGraphTransformation(interval);

            gremlinResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> Configuration.gremlinConstraintMapper().mapToPlatformSpecificGraph(graph))
            );

            shaclResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> Configuration.shaclConstraintMapper().mapToPlatformSpecificGraph(graph))
            );
        });
//        removeFirstN(2, gremlinResults, shaclResults);
        return new ResultData(gremlinResults, shaclResults);
    }

    /**
     * Evaluates performance of Shacl and Gremlin based implementations to transform an abstract attribute-based constraint
     * to a platform-specific constraint.
     *
     * @param intervals set of the number of constraint functions that are created randomly for every evaluation step
     * @return pair of evaluation data. Left one is for Gremlin. Right one is for Shacl
     */
    public ResultData attributeBasedConstraintsExperiment(List<Double> intervals) {
        TreeMap<Double, Double> gremlinResults = new TreeMap<>();
        TreeMap<Double, Double> shaclResults = new TreeMap<>();
        intervals.forEach(interval -> {
            Constraint attributeConstraints = AbstractDataGenerator.combineConstraintFunctionsIntoOneConstraint(AbstractDataGenerator.attributeBasedFunctions(interval));
            gremlinResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> {
                        Configuration.gremlinConstraintMapper().mapToPlatformSpecificConstraint(UUID.randomUUID().toString(), attributeConstraints);
                    })
            );
            shaclResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> {
                        Configuration.shaclConstraintMapper().mapToPlatformSpecificConstraint(UUID.randomUUID().toString(), attributeConstraints);
                    })
            );
        });
//        removeFirstN(2, gremlinResults, shaclResults);
        return new ResultData(gremlinResults, shaclResults);
    }

    /**
     * Evaluates performance of Shacl and Gremlin based implementations to transform an abstract collection-based constraint
     * to a platform-specific constraint.
     *
     * @param intervals set of the number of constraint functions that are created randomly for every evaluation step
     * @return pair of evaluation data. Left one is for Gremlin. Right one is for Shacl
     */
    public ResultData collectionBasedConstraintsExperiment(List<Double> intervals) {
        TreeMap<Double, Double> gremlinResults = new TreeMap<>();
        TreeMap<Double, Double> shaclResults = new TreeMap<>();
        intervals.forEach(interval -> {
            List<ConstraintFunction> functions = AbstractDataGenerator.collectionBasedFunctions(interval);
            Constraint constraintForEvaluation = AbstractDataGenerator.combineConstraintFunctionsIntoOneConstraint(functions);
            gremlinResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> {
                        Configuration.gremlinConstraintMapper().mapToPlatformSpecificConstraint(UUID.randomUUID().toString(), constraintForEvaluation);
                    })
            );
            shaclResults.put(
                    interval,
                    measureFunctionExecutionTime(() -> {
                        Configuration.shaclConstraintMapper().mapToPlatformSpecificConstraint(UUID.randomUUID().toString(), constraintForEvaluation);
                    })
            );
        });
//        removeFirstN(2, gremlinResults, shaclResults);
        return new ResultData(gremlinResults, shaclResults);
    }

    private double measureFunctionExecutionTime(Runnable function) {
        double start = System.currentTimeMillis();
        function.run();
        double finish = System.currentTimeMillis();
        return finish - start;
    }

    public static ResultData runSeveralTimes(int warmUpPhases, int numberOfRuns, Supplier<ResultData> experiment) {
        List<ResultData> resultData = new ArrayList<>();

        // A warm-up phase to allow the JVM to optimize the code before you start measuring the time.
        // This will help to minimize the impact of JVM startup time and optimize the code for better performance.
        for (int i = 0; i < warmUpPhases; i++) {
            experiment.get();
        }

        for (int i = 0; i < numberOfRuns; i++) {
            resultData.add(experiment.get());
        }

        var gremlin = resultData.stream().map(ResultData::getGremlinData);
        var shacl = resultData.stream().map(ResultData::getShaclData);

        return new ResultData(computeAverage(gremlin), computeAverage(shacl));
    }

    private static Map<Double, Double> computeAverage(Stream<Map<Double, Double>> stream) {
        return stream
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingDouble(Map.Entry::getValue)));
    }
}
