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

import ansk.development.domain.instance.InstanceElement;
import ansk.development.domain.instance.Link;
import ansk.development.domain.instance.Slot;

import java.util.List;

/**
 * Constructs test graph with initial data.
 */
public class TestGraphProvider {

    public static List<InstanceElement> getSubgraph() {
        var slot = new Slot();
        slot.setInstanceUuid("eaf4d1cf-54df-451d-871c-e72ea7c11e39");
        slot.setKey("name");
        slot.setValue("Architecture process");
        InstanceElement sprint1 = new InstanceElement();
        sprint1.setUuid("9cf435e3-80da-4d5e-8abd-5ab9a870961d");
        sprint1.setInstanceOf("Sprint");
        sprint1.setModelUuid("8d54ac08-83b9-44a9-8d2d-1d7eea9cfe58");
        sprint1.setSlots(List.of(slot));


        var slott = new Slot();
        slott.setInstanceUuid("73e0a828-f895-4d60-9e95-deb8b216ceda");
        slott.setKey("name");
        slott.setValue("Implementation process");
        InstanceElement sprint2 = new InstanceElement();
        sprint2.setUuid("c7417256-da9c-445c-a3c3-bf52981686e9");
        sprint2.setInstanceOf("Sprint");
        sprint2.setModelUuid("9051762e-63d4-451f-831c-3382f70b9c2f");
        sprint2.setSlots(List.of(slott));

        var slottt = new Slot();
        slottt.setInstanceUuid("d1b3ee4e-0f1d-4355-905f-47b29691eb72");
        slottt.setKey("name");
        slottt.setValue("Design process");
        InstanceElement sprint3 = new InstanceElement();
        sprint3.setUuid("babd56ed-6b5d-4a0a-a364-0039777f098c");
        sprint3.setInstanceOf("Sprint");
        sprint3.setModelUuid("bd53c90a-ea7b-457f-be5b-5f4af4fc2e91");
        sprint3.setSlots(List.of(slottt));

        var swslot1 = new Slot();
        swslot1.setInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        swslot1.setKey("salary");
        swslot1.setValue("10000");
        var swslot2 = new Slot();
        swslot2.setInstanceUuid("4198dae5-e704-42d9-a902-63d941bb6ebd");
        swslot2.setKey("age");
        swslot2.setValue("24");
        var swslot3 = new Slot();
        swslot3.setInstanceUuid("4422909f-3ed0-44b4-b506-917d5c92ac67");
        swslot3.setKey("name");
        swslot3.setValue("John");

        var swlink1 = new Link();
        swlink1.setInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        swlink1.setName("takes_part_in");
        swlink1.setTargetInstanceUuid("c7417256-da9c-445c-a3c3-bf52981686e9");
        var swlink2 = new Link();
        swlink2.setInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        swlink2.setName("works_on");
        swlink2.setTargetInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        var swlink3 = new Link();
        swlink3.setInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        swlink3.setName("takes_part_in");
        swlink3.setTargetInstanceUuid("9cf435e3-80da-4d5e-8abd-5ab9a870961d");
        var swlink4 = new Link();
        swlink4.setInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        swlink4.setName("takes_part_in");
        swlink4.setTargetInstanceUuid("babd56ed-6b5d-4a0a-a364-0039777f098c");
        InstanceElement sw = new InstanceElement();
        sw.setUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        sw.setInstanceOf("SoftwareEngineer");
        sw.setModelUuid("137245bb-0d84-4fa1-8c23-74010946a0d3");
        sw.setSlots(List.of(swslot1, swslot2, swslot3));
        sw.setLinks(List.of(swlink1, swlink2, swlink3, swlink4));

        var prslot1 = new Slot();
        prslot1.setInstanceUuid("397aa34b-8429-4fe9-8f30-59312515f8b7");
        prslot1.setKey("name");
        prslot1.setValue("Thesis");
        var prslot2 = new Slot();
        prslot2.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prslot2.setKey("responsible");
        prslot2.setValue("John");
        var prslot3 = new Slot();
        prslot3.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prslot3.setKey("started");
        prslot3.setValue("true");

        var prlink1 = new Link();
        prlink1.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prlink1.setName("participates");
        prlink1.setTargetInstanceUuid("45735b9d-955a-4b28-87ce-1998b67f3b40");
        var prlink2 = new Link();
        prlink2.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prlink2.setName("consists_of");
        prlink2.setTargetInstanceUuid("9cf435e3-80da-4d5e-8abd-5ab9a870961d");
        var prlink3 = new Link();
        prlink3.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prlink3.setName("consists_of");
        prlink3.setTargetInstanceUuid("c7417256-da9c-445c-a3c3-bf52981686e9");
        var prlink4 = new Link();
        prlink4.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prlink4.setName("consists_of");
        prlink4.setTargetInstanceUuid("babd56ed-6b5d-4a0a-a364-0039777f098c");
        var prlink5 = new Link();
        prlink5.setInstanceUuid("24620947-6e86-4f39-8365-40247265c9ce");
        prlink5.setName("participates");
        prlink5.setTargetInstanceUuid("ea9f52ee-a86f-48f1-b9c3-b259764a6b04");
        InstanceElement project = new InstanceElement();
        project.setUuid("24620947-6e86-4f39-8365-40247265c9ce");
        project.setInstanceOf("Project");
        project.setModelUuid("66aa8326-ed7b-42fa-ae3f-e53111a62235");
        project.setSlots(List.of(prslot1, prslot2, prslot3));
        project.setLinks(List.of(prlink1, prlink2, prlink3, prlink4, prlink5));
        return List.of(sw, project, sprint1, sprint2, sprint3);
    }
}
