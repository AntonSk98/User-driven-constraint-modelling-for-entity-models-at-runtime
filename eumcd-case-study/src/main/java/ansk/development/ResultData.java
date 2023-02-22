package ansk.development;

import java.util.Map;

public class ResultData {
    final Map<Double, Double> gremlinData;
    final Map<Double, Double> shaclData;

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
