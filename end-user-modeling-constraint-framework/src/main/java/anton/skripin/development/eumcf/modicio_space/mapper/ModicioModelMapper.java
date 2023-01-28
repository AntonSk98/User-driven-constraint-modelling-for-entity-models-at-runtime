package anton.skripin.development.eumcf.modicio_space.mapper;

import anton.skripin.development.domain.model.Association;
import anton.skripin.development.domain.model.Attribute;
import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.mapper.ModelMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.set;

public class ModicioModelMapper implements ModelMapper<modicio.core.ModelElement> {

    @Override
    @SneakyThrows
    public ModelElement mapToModelElement(modicio.core.ModelElement modicioModelElement) {
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
            attribute.setPath(String.format("<%s>%s", modelElement.getName(), attribute.getKey()));
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
            association.setPath(String.format("%s(%s)", association.getName(), association.getTargetModelElementName()));
            associations.add(association);
        });
        modelElement.setAttributes(attributes);
        modelElement.setAssociations(associations);

        return modelElement;
    }
}
