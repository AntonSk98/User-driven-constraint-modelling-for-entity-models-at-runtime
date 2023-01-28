package anton.skripin.development.eumcf.modicio_space;

import anton.skripin.development.domain.instance.InstanceElement;
import anton.skripin.development.domain.instance.Link;
import anton.skripin.development.domain.instance.Slot;
import anton.skripin.development.mapper.InstanceMapper;
import lombok.SneakyThrows;
import modicio.core.DeepInstance;

import java.util.stream.Collectors;

import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.future;
import static anton.skripin.development.eumcf.util.ScalaToJavaMapping.set;

public class ModicioInstanceMapper implements InstanceMapper<DeepInstance> {

    @Override
    @SneakyThrows
    public InstanceElement mapToInstanceElement(DeepInstance deepInstance) {
        InstanceElement instanceElement = new InstanceElement();
        future(deepInstance.unfold()).get();
        instanceElement.setUuid(deepInstance.getInstanceId());
        instanceElement.setInstanceOf(deepInstance.typeHandle().getTypeName());
        instanceElement.setModelUuid(deepInstance.typeHandle().getTypeIdentity());
        instanceElement.setSlots(set(deepInstance.getDeepAttributes())
                .stream()
                .map(attributeData -> {
                    Slot slot = new Slot();
                    slot.setInstanceUuid(attributeData.instanceId());
                    slot.setKey(attributeData.key());
                    slot.setValue(attributeData.value());
                    return slot;
                })
                .collect(Collectors.toList()));
        instanceElement.setLinks(set(deepInstance.getAssociations())
                .stream()
                .map(associationData -> {
                    Link link = new Link();
                    link.setInstanceUuid(associationData.instanceId());
                    link.setName(associationData.byRelation());
                    link.setTargetInstanceUuid(associationData.targetInstanceId());
                    return link;
                })
                .toList());

        return instanceElement;
    }
}
