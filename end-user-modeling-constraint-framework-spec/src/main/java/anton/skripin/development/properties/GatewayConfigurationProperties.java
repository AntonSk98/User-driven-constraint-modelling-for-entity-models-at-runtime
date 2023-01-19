package anton.skripin.development.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GatewayConfigurationProperties {
    private final String modelElementUri;
    private final String queryParam;

    public GatewayConfigurationProperties(String modelElementUri, String queryParam) {
        this.modelElementUri = modelElementUri;
        this.queryParam = queryParam;
    }
}
