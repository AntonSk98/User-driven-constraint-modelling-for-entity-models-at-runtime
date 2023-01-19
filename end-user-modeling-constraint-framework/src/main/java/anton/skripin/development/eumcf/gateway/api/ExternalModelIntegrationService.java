package anton.skripin.development.eumcf.gateway.api;

import anton.skripin.development.domain.model.ModelElement;

public interface ExternalModelIntegrationService {

    ModelElement getModelElementById(String id);
}
