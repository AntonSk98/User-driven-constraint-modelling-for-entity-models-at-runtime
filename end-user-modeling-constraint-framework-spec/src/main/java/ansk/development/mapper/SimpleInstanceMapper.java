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

import ansk.development.domain.instance.InstanceElement;

/**
 * Simple implementation of {@link InstanceMapper}.
 * It should not be used in production until required or necessary
 */
public class SimpleInstanceMapper implements InstanceMapper<InstanceElement> {

    /**
     * Maps {@link InstanceElement} to itself.
     * This mapper should be used only for testing purposes or fast bootstrap.
     *
     * @param instanceElement {@link InstanceElement}
     * @return {@link InstanceElement}
     */
    @Override
    public InstanceElement mapToInstanceElement(InstanceElement instanceElement) {
        return instanceElement;
    }
}
