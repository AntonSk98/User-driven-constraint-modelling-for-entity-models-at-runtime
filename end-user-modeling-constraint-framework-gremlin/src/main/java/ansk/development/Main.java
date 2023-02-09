package ansk.development;

import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.instance.InstanceElement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<InstanceElement> instances = Registry.getSubgraph();
        GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();
        constraintMapper.mapToPlatformSpecificGraph(instances);
    }
}