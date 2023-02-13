package ansk.development;

import ansk.development.exception.GraphTransformationException;
import ansk.development.mapper.GremlinConstraintMapper;
import anton.skripin.development.domain.instance.InstanceElement;

import java.lang.management.ManagementFactory;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getVmVersion());
        List<InstanceElement> instances = Registry.getSubgraph();
        GremlinConstraintMapper constraintMapper = new GremlinConstraintMapper();
        constraintMapper.mapToPlatformSpecificGraph(instances);

    }
}