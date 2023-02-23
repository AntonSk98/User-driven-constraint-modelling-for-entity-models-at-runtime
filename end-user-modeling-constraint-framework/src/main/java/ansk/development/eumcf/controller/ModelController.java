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

package ansk.development.eumcf.controller;

import ansk.development.domain.model.ModelElement;
import ansk.development.eumcf.service.api.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelController {

    private final ModelService modelService;


    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * Extends a required model element with another one that can be accessed via navigation.
     *
     * @param from a model element that should be extended
     * @param to   a model element that a target model element is extended with
     * @param path valid path that is used to access a 'TO' element
     * @return Extended {@link ModelElement} with attributes and associations accessed via navigation
     */
    @GetMapping("/add_to_opened_model_element")
    public ResponseEntity<ModelElement> addToOpenedModelElement(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("path") String path
    ) {
        return ResponseEntity.ok().body(modelService.addToOpenedModelElement(from, to, path));
    }

    /**
     * Updates an attribute
     *
     * @param id          of an attribute
     * @param type        of a model element that has the attribute
     * @param attributeId id
     * @param key         key definition of an attribute
     * @param datatype    datatype of an attribute
     */
    @GetMapping("/update_attribute")
    public void updateAttribute(
            @RequestParam String id,
            @RequestParam String type,
            @RequestParam String attributeId,
            @RequestParam String key,
            @RequestParam String datatype
    ) {
        modelService.updateAttributeDefinition(id, type, attributeId, key, datatype);
    }

    /**
     * Updates association
     *
     * @param id            id
     * @param type          of a model element
     * @param associationId id of an association
     * @param byRelation    connector link between source and target models
     * @param target        model element
     */
    @GetMapping("/update_association")
    public void updateAssociation(
            @RequestParam String id,
            @RequestParam String type,
            @RequestParam String associationId,
            @RequestParam String byRelation,
            @RequestParam String target
    ) {
        modelService.updateAssociationDefinition(id, type, associationId, byRelation, target);
    }
}
