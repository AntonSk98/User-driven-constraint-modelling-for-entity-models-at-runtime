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

import ansk.development.domain.instance.InstanceElement;
import ansk.development.eumcf.service.api.InstanceService;
import org.springframework.web.bind.annotation.*;


/**
 * Controller to manage instance-related operations.
 */
@RestController
public class InstanceController {

    private final InstanceService instanceService;

    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    /**
     * Removes a constraint by a given uuid.
     *
     * @param uuid uuid of a persisted constraint
     */
    @GetMapping("/delete_instance_by_id")
    public void deleteInstanceById(String uuid) {
        instanceService.deleteInstanceById(uuid);
    }


    /**
     * Creates a new instance.
     *
     * @param instanceElement constructed instance to be created
     */
    @PostMapping("/instantiate_element")
    public void createInstance(@RequestBody InstanceElement instanceElement) {
        instanceService.createInstance(instanceElement.getInstanceOf(), instanceElement.getSlots(), instanceElement.getLinks());
    }

    /**
     * Updates an instance.
     *
     * @param instanceElement updated instances
     */
    @PostMapping("/update_instance_element")
    public void updateInstance(@RequestBody InstanceElement instanceElement) {
        instanceService.updateInstance(instanceElement.getUuid(), instanceElement.getSlots(), instanceElement.getLinks());
    }

    /**
     * Fetches instance by its uuid
     *
     * @param uuid of an instance
     * @return {@link InstanceElement}
     */
    @GetMapping("/get_instance_by_id")
    public InstanceElement getInstanceById(@RequestParam String uuid) {
        return instanceService.getInstanceByUuid(uuid);
    }
}
