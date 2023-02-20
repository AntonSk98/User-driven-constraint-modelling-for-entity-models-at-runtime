package ansk.development;

import ansk.development.properties.DefaultTemplateConfigurationProperties;
import ansk.development.service.TemplateFunctionInitializer;

public class Main {
    public static void main(String[] args) {
        TemplateFunctionInitializer a = new TemplateFunctionInitializer(new DefaultTemplateConfigurationProperties());
        a.initTemplateFunction();
    }
}
