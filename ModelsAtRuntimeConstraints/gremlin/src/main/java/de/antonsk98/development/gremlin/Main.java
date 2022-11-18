package de.antonsk98.development.gremlin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.antonsk98.development.gremlin.domain.company.Company;
import de.antonsk98.development.gremlin.domain.company.Person;
import de.antonsk98.development.gremlin.domain.constraints.Constraint;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        Constraint personMustBeEitherBetween10And16OrHaveSalaryMoreThan25000 = fetchConstraint("person_must_be_either_between_10_and_16_or_have_salary_more_than_25000.json");
        System.out.println(personMustBeEitherBetween10And16OrHaveSalaryMoreThan25000);

        List<Company> testCompanies = createTestEmployers();
        List<Person> testEmployees = createTestEmployees(testCompanies.get(0));

        TinkerGraph tg = TinkerGraph.open();
        GraphTraversalSource g = tg.traversal();

        createTestGraphFromData(g, testEmployees, testCompanies);

        System.out.println(g);

        ConcurrentBindings b = new ConcurrentBindings();
        b.putIfAbsent("g", g);
        GremlinExecutor ge = GremlinExecutor.build().evaluationTimeout(150000L).globalBindings(b).create();

        CompletableFuture<?> evalResult = ge.eval(String.format("g.V()%s%s.count().next()",
                        QueryConstructor.focusProperty("Person", false),
                        QueryConstructor.or(false,
                                QueryConstructor.greaterThan("salary", 25000, true),
                                QueryConstructor.and(true,
                                        QueryConstructor.lessThan("age", 16, true),
                                        QueryConstructor.greaterThan("age", 10, true)))
                )
        );
        System.out.println(evalResult.get());
        System.out.println("hi!");
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
        Person anton = new Person("Anton", 24, "10000", company);
        Person egor = new Person("Egor", 12, "12000", company);
        Person petr = new Person("Petr", 26, "30000", company);
        List<Person> employees = List.of(anton, egor, petr);
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
