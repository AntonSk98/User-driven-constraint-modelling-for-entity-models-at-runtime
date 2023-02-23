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

package ansk.development.domain.instance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * Class that defines an instance element.
 */
@Getter
@Setter
@NoArgsConstructor
public class InstanceElement {
    private String uuid;
    private String instanceOf;
    private String modelUuid;
    private List<Slot> slots;
    private List<Link> links;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceElement that = (InstanceElement) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(instanceOf, that.instanceOf) && Objects.equals(modelUuid, that.modelUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, instanceOf, modelUuid);
    }
}
