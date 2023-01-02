package de.antonsk98.development.gremlin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.antonsk98.development.gremlin.domain.company.Company;
import de.antonsk98.development.gremlin.domain.company.Person;
import de.antonsk98.development.gremlin.domain.constraints.Constraint;
import de.antonsk98.development.gremlin.domain.constraints.functions.BasicFunction;
import de.antonsk98.development.gremlin.domain.constraints.functions.Function;
import de.antonsk98.development.gremlin.domain.constraints.functions.LogicalFunction;
import de.antonsk98.development.gremlin.service.GremlinFunctionService;
import de.antonsk98.development.gremlin.service.GremlinLogicalFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.io.Resources;
import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.apache.tinkerpop.gremlin.jsr223.ConcurrentBindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static de.antonsk98.development.gremlin.service.GremlinFunctionService.*;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        Constraint fetchedConstraint = fetchConstraint("person_must_be_either_between_10_and_16_or_have_salary_more_than_25000.json");


        List<Company> testCompanies = createTestEmployers();
        List<Person> testEmployees = createTestEmployees(testCompanies.get(0));

        TinkerGraph tg = TinkerGraph.open();
        GraphTraversalSource g = tg.traversal();

        createTestGraphFromData(g, testEmployees, testCompanies);

        System.out.println(g);

        ConcurrentBindings b = new ConcurrentBindings();
        b.putIfAbsent("g", g);
        GremlinExecutor ge = GremlinExecutor.build().evaluationTimeout(150000L).globalBindings(b).create();

        for (Function function : fetchedConstraint.getFunctions()) {
            String constraint = createConstraint(function);
            CompletableFuture<?> result = ge.eval(String.format(
                    "g.V()%s%s.count().next()",
                    QueryConstructor.focusProperty().apply(fetchedConstraint.getFocusProperty(), false, null),
                    constraint
            ));
            long begin = System.currentTimeMillis();
            System.out.println(result.get());
            System.out.println(String.format("Method invocation took: %s ms", System.currentTimeMillis() - begin));
        }
    }

    private static String createConstraint(Function function) {
        List<String> subQueries = new ArrayList<>();
        List<Function> notProcessedCompositeFunctions = getNonProcessedCompositeFunctions(function);
        if (notProcessedCompositeFunctions.isEmpty()) {
            function.getNestedFunctions().forEach(nestedFunction -> {
                BasicFunction basicFunction = (BasicFunction) nestedFunction;
                String gremlinQuery = basicFunctionToGremlinQuery
                        .get(basicFunction.getFunctionName())
                        .apply(basicFunction.getTarget(), true, basicFunction.getParams());
                subQueries.add(gremlinQuery);
                basicFunction.setProcessed(true); // check
            });
            LogicalFunction logicalFunction = (LogicalFunction) function;
            String gremlinQuery = logicalFunctionToGremlinQuery
                    .get(logicalFunction.getFunctionName())
                    .apply(!logicalFunction.isRoot(), subQueries.toArray(String[]::new));
            logicalFunction.setProcessed(true); // check
            return gremlinQuery;
        } else {
            notProcessedCompositeFunctions.forEach(notProcessedCompositeFunction -> {
                subQueries.add(createConstraint(notProcessedCompositeFunction));
            });
        }
        function.getNestedFunctions().stream().filter(nestedFunction -> nestedFunction.getNestedFunctions().isEmpty()).forEach(nestedFunction -> {
            BasicFunction basicFunction = (BasicFunction) nestedFunction;
            String gremlinQuery = basicFunctionToGremlinQuery
                    .get(basicFunction.getFunctionName())
                    .apply(basicFunction.getTarget(), !basicFunction.isRoot(), basicFunction.getParams());
            subQueries.add(gremlinQuery);
            basicFunction.setProcessed(true);
        });
        function.setProcessed(true);
        return logicalFunctionToGremlinQuery.get(function.getFunctionName()).apply(!function.isRoot(), subQueries.toArray(String[]::new));
    }

    private static List<Function> getNonProcessedCompositeFunctions(Function function) {
        return function.getNestedFunctions().stream().filter(nestedFunction ->
                        nestedFunction.getNestedFunctions().size() > 0
                                && nestedFunction
                                .getNestedFunctions()
                                .stream()
                                .noneMatch(Function::isProcessed))
                .collect(Collectors.toList());
    }

    private static Constraint fetchConstraint(String resourcePath) throws IOException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();
        URI uri = Resources.getResource(resourcePath).toURI();
        String content = Files.readString(Path.of(uri));
        return objectMapper.readValue(content, Constraint.class);
    }

    private static List<Company> createTestEmployers() {
        return Collections.singletonList(new Company("T-Systems MMS", "Germany"));
    }

    private static List<Person> createTestEmployees(Company company) {
        List<Person> employees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            employees.add(new Person(
                    String.format("Name %d", i),
                    ThreadLocalRandom.current().nextInt(9, 45),
                    String.valueOf(ThreadLocalRandom.current().nextInt(10000, 40000 + 1)),
                    company
                    ));
        }
        Person anton = new Person("Anton", 24, "10000", company);
        Person egor = new Person("Egor", 12, "12000", company);
        Person petr = new Person("Petr", 26, "30000", company);

        company.setEmployees(employees);
        return employees;
    }

    private static GraphTraversalSource createTestGraphFromData(GraphTraversalSource g, List<Person> employees, List<Company> employers) {
        employees.forEach(person -> {
            g.addV(person.getClass().getSimpleName())
                    .as(person.getName())
                    .property("name", person.getName())
                    .property("age", person.getAge())
                    .property("salary", person.getSalary()).iterate();
        });
        employers.forEach(company -> {
            g.addV(company.getClass().getSimpleName())
                    .as(company.getName())
                    .property("name", company.getName())
                    .property("location", company.getLocation()).iterate();
        });
        employees.forEach(person -> {
            g.addE("employer")
                    .from(g.V().has(person.getClass().getSimpleName(), "name", person.getName()).next())
                    .to(g.V().has(person.getEmployer().getClass().getSimpleName(), "name", person.getEmployer().getName()).next())
                    .iterate();
        });
        employers.forEach(company -> {
            company.getEmployees().forEach(person -> {
                g.addE("employee")
                        .from(g.V().has(company.getClass().getSimpleName(), "name", company.getName()).next())
                        .to(g.V().has(person.getClass().getSimpleName(), "name", person.getName()).next())
                        .iterate();
            });
        });
        return g;
    }
}
