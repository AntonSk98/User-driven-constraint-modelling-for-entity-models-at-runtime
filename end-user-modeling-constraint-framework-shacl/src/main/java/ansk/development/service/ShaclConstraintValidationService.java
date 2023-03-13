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

package ansk.development.service;

import ansk.development.dsl.ShaclInstanceGraph;
import ansk.development.dsl.ShaclConstraintShape;
import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.constraint.ConstraintValidationReport;
import ansk.development.domain.instance.InstanceElement;
import ansk.development.mapper.ShaclConstraintMapper;
import ansk.development.service.api.ConstraintValidationService;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;

import java.util.List;

/**
 * Implenentation of {@link ConstraintValidationService}.
 */
public class ShaclConstraintValidationService extends AbstractConstraintValidationService {

    private static final ShaclConstraintMapper shaclConstraintMapper = new ShaclConstraintMapper();

    @Override
    public ConstraintValidationReport validateConstraint(String uuid, List<InstanceElement> subgraphForValidation, Constraint constraint) {
        ShaclInstanceGraph dataGraph = shaclConstraintMapper.mapToPlatformSpecificGraph(subgraphForValidation);
        ShaclConstraintShape shaclConstraint = shaclConstraintMapper.mapToPlatformSpecificConstraint(uuid, constraint);
        Shapes shapes = Shapes.parse(shaclConstraint.getGraph());
        ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph.getGraph());
        return new ConstraintValidationReport(
                uuid,
                constraint.getName(),
                constraint.getModelElementType(),
                report.conforms(),
                constraint.getViolationLevel(),
                constraint.getViolationMessage());
    }
}
