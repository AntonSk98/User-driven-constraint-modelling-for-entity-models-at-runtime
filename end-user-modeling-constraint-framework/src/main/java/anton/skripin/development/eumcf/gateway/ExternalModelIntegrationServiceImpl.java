package anton.skripin.development.eumcf.gateway;

import anton.skripin.development.domain.model.ModelElement;
import anton.skripin.development.eumcf.gateway.api.ExternalModelIntegrationService;
import anton.skripin.development.exception.NoModelElementFoundException;
import anton.skripin.development.properties.GatewayConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalModelIntegrationServiceImpl implements ExternalModelIntegrationService {

    private final RestTemplate restTemplate;
    private final GatewayConfigurationProperties gatewayConfigurationProperties;

    public ExternalModelIntegrationServiceImpl(RestTemplate restTemplate,
                                               GatewayConfigurationProperties gatewayConfigurationProperties) {
        this.restTemplate = restTemplate;
        this.gatewayConfigurationProperties = gatewayConfigurationProperties;
    }

    public ModelElement getModelElementById(String id) {
        String uri = gatewayConfigurationProperties.getModelElementUri();
        String queryParam = gatewayConfigurationProperties.getQueryParam();
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(uri)
                .queryParam(queryParam, id)
                .build();
        HttpEntity<ModelElement> httpEntity = restTemplate.getForEntity(uriComponents.toUriString(), ModelElement.class);
        if (!httpEntity.hasBody()) {
            String exceptionMsg = String.format("No model element could by found. Uri: %s. Query param: %s", uri, queryParam);
            throw new NoModelElementFoundException(exceptionMsg);
        }
        return httpEntity.getBody();
    }
}
