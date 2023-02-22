package ansk.development;

import ansk.development.mapper.GremlinConstraintMapper;
import ansk.development.mapper.ShaclConstraintMapper;
import ansk.development.service.GremlinConstraintValidationService;
import ansk.development.service.ShaclConstraintValidationService;

/**
 * Configuration class.
 */
public final class Configuration {

    private static final GremlinConstraintValidationService gremlinConstraintValidationService = new GremlinConstraintValidationService();
    private static final ShaclConstraintValidationService shaclConstraintValidationService = new ShaclConstraintValidationService();

    private static final GremlinConstraintMapper gremlinConstraintMapper = new GremlinConstraintMapper();
    private static final ShaclConstraintMapper shaclConstraintMapper = new ShaclConstraintMapper();

    private Configuration() {

    }

    public static GremlinConstraintValidationService gremlinConstraintValidationService() {
        return gremlinConstraintValidationService;
    }

    public static ShaclConstraintValidationService shaclConstraintValidationService() {
        return shaclConstraintValidationService;
    }

    public static GremlinConstraintMapper gremlinConstraintMapper() {
        return gremlinConstraintMapper;
    }

    public static ShaclConstraintMapper shaclConstraintMapper() {
        return shaclConstraintMapper;
    }
}
