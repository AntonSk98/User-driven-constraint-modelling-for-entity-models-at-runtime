package ansk.development;

import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.constraint.Constraint;
import anton.skripin.development.domain.instance.InstanceElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        List<InstanceElement> instances = Registry.getSubgraph();
        GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();
        constraintMapper.mapToPlatformSpecificGraph(instances);
//        constraintMapper.mapToPlatformSpecificConstraint("ea9f52ee-a86f-48f1-b9c3-b259764a6b04", new ObjectMapper().readValue(FOR_ALL_CONSTRAINT, Constraint.class));

    }
}