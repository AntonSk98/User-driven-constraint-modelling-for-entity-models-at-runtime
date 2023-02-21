package ansk.development.eumcf.mapper;

import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;
import ansk.development.exception.instance.InstanceMapperException;
import ansk.development.mapper.InstanceMapper;
import modicio.core.DeepInstance;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static ansk.development.eumcf.util.ScalaToJavaMapper.future;
import static ansk.development.eumcf.util.ScalaToJavaMapper.set;

/**
 * Concrete implementation of {@link InstanceMapper}.
 */
public class ModicioInstanceMapper implements InstanceMapper<DeepInstance> {

    /**
     * Maps two technical spaces: Modicio space with constraint engine space.
     * In this context a deep instance is flattened into one {@link InstanceElement}
     *
     * @param deepInstance {@link DeepInstance}
     * @return {@link InstanceElement}
     */
    @Override
    public InstanceElement mapToInstanceElement(DeepInstance deepInstance) {
        try {
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
                    .collect(Collectors.toList()));

            return instanceElement;
        } catch (InterruptedException | ExecutionException e) {
            throw new InstanceMapperException(e);
        }
    }
}
