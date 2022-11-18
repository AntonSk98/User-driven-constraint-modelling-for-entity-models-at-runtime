package de.antonsk98.development.gremlin.domain.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Getter
@AllArgsConstructor
@FieldNameConstants
public class Person {
    private final String name;
    private final int age;
    private final String salary;
    private final Company employer;
}
