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

package ansk.development;

import java.util.Map;

public class ResultData {
    private final Map<Double, Double> gremlinData;
    private final Map<Double, Double> shaclData;

    public ResultData(Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        this.gremlinData = gremlinData;
        this.shaclData = shaclData;
    }

    public Map<Double, Double> getGremlinData() {
        return gremlinData;
    }

    public Map<Double, Double> getShaclData() {
        return shaclData;
    }
}
