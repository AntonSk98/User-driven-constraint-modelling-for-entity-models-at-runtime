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

package ansk.development.mapper;

import ansk.development.domain.constraint.Constraint;
import ansk.development.domain.instance.InstanceElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Simple implementation of {@link AbstractToPSConstraintMapper}.
 * Unless necessary, it should not be used in production
 */
public class SimpleAbstractToPSConstraintMapper implements AbstractToPSConstraintMapper<String, String> {

    /**
     * Maps a {@link Constraint} to its string representation in JSON-format.
     *
     * @param constraint {@link Constraint}
     * @return {@link Constraint} in a JSON representation
     */
    @Override
    public String mapToPlatformSpecificConstraint(String uuid, Constraint constraint) {
        try {
            return new ObjectMapper().writeValueAsString(constraint);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred while mapping abstract to platform-specific constraint!");
        }
    }

    /**
     * Maps a list of {@link InstanceElement} to its string representation in JSON-format.
     *
     * @param subgraphForValidation subgraph required for validation
     * @return list of {@link InstanceElement} in a JSON representation
     */
    @Override
    public String mapToPlatformSpecificGraph(List<InstanceElement> subgraphForValidation) {
        try {
            return new ObjectMapper().writeValueAsString(subgraphForValidation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred while mapping an abstract to platform-specific graph");
        }
    }
}
