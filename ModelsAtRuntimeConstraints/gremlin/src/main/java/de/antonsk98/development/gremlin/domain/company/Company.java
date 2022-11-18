package de.antonsk98.development.gremlin.domain.company;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.List;


@Getter
@FieldNameConstants
public class Company {
    private final String name;
    private final String location;
    private List<Person> employees;

    public Company(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }
}
