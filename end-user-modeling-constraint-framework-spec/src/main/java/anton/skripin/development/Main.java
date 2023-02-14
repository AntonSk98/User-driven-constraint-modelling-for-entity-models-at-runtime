package anton.skripin.development;

import anton.skripin.development.properties.DefaultTemplateConfigurationProperties;
import anton.skripin.development.service.TemplateFunctionInitializer;

public class Main {
    public static void main(String[] args) {
        TemplateFunctionInitializer a = new TemplateFunctionInitializer(new DefaultTemplateConfigurationProperties());
        a.initTemplateFunction();
    }
}
