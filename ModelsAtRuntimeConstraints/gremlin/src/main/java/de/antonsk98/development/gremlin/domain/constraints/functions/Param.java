package de.antonsk98.development.gremlin.domain.constraints.functions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.thrift.annotation.Nullable;

public class Param {
    @Nullable
    private final String name;
    private final String value;

    @JsonCreator
    public Param(@JsonProperty("name") String name, @JsonProperty("value") String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
