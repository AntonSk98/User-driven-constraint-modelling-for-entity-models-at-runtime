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

package ansk.development.eumcf.mapper;

import ansk.development.domain.model.Association;
import ansk.development.domain.model.Attribute;
import ansk.development.domain.model.ModelElement;
import ansk.development.exception.model.ModelMapperException;
import ansk.development.mapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static ansk.development.eumcf.util.ScalaToJavaMapper.future;
import static ansk.development.eumcf.util.ScalaToJavaMapper.set;


/**
 * Concrete implementation of {@link ModelMapper}
 */
public class ModicioModelMapper implements ModelMapper<modicio.core.ModelElement> {

    /**
     * Maps two technical spaces: Modicio space with constraint engine space.
     * In this context a {@link modicio.core.ModelElement} is mapped to {@link ModelElement}
     *
     * @param modicioModelElement {@link modicio.core.ModelElement}
     * @return {@link ModelElement}
     */
    @Override
    public ModelElement mapToModelElement(modicio.core.ModelElement modicioModelElement) {
        try {
            future(modicioModelElement.unfold()).get();
            ModelElement modelElement = new ModelElement();
            modelElement.setUuid(modicioModelElement.identity());
            modelElement.setName(modicioModelElement.name());
            List<Attribute> attributes = new ArrayList<>();
            set(modicioModelElement.deepAttributeRuleSet()).forEach(attributeRule -> {
                Attribute attribute = new Attribute();
                attribute.setUuid(attributeRule.id());
                attribute.setDatatype(attributeRule.datatype());
                attribute.setKey(attributeRule.name());
                attribute.setAttribute(String.format("<%s>%s", modelElement.getName(), attribute.getKey()));
                attributes.add(attribute);
            });
            List<Association> associations = new ArrayList<>();
            set(modicioModelElement.deepAssociationRuleSet()).forEach(associationRule -> {
                Association association = new Association();
                association.setUuid(associationRule.id());
                association.setName(associationRule.associationName());
                association.setMultiplicity(associationRule.multiplicity());
                association.setTargetModelElementName(associationRule.targetName());
                association.setTargetModelElementUuid(modicio.core.ModelElement.REFERENCE_IDENTITY());
                association.setNavigation(String.format("%s(%s)", association.getName(), association.getTargetModelElementName()));
                associations.add(association);
            });
            modelElement.setAttributes(attributes);
            modelElement.setAssociations(associations);

            return modelElement;
        } catch (InterruptedException | ExecutionException e) {
            throw new ModelMapperException(e);
        }
    }
}
