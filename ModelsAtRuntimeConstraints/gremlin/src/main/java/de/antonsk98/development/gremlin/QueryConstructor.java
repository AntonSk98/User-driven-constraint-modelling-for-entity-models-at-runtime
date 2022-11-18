package de.antonsk98.development.gremlin;

import java.util.Objects;

public class QueryConstructor {

    private static final String SEPARATOR = ".";
    private static final String EMPTY = "";

    public static String focusProperty(String focusProperty, boolean nested) {
        return String.format("%shasLabel('%s')", resolveNested(nested), focusProperty);
    }

    public static String greaterThan(String property, Integer value, boolean nested) {
        return String.format("%shas('%s', gt(%d))", resolveNested(nested), property, value);
    }

    public static String lessThan(String property, Integer value, boolean nested) {
        return String.format("%shas('%s', lt(%d))", resolveNested(nested), property, value);
    }

    public static String and(boolean nested, String... functions) {
        if (Objects.isNull(functions)) {
            throw new IllegalArgumentException("Logical function must have values");
        }
        return String.format("%sand(%s)", resolveNested(nested), String.join(",", functions));
    }

    public static String or(boolean nested, String... functions) {
        if (Objects.isNull(functions)) {
            throw new IllegalArgumentException("Logical function must have values");
        }
        return String.format("%sor(%s)", resolveNested(nested), String.join(",", functions));
    }

    public static void a() {
    }

    private static String resolveNested(boolean nested) {
        return nested ? EMPTY : SEPARATOR;
    }
}


