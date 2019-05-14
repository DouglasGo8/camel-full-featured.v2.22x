package com.douglasdb.camel.feat.core.transform.enrich;

public class AbbreviationExpander {

    public String expand(String abbreviation) {

        if ("MA".equalsIgnoreCase(abbreviation)) {
            return "Massachusetts";
        }

        if ("CA".equalsIgnoreCase(abbreviation)) {
            return "California";
        }

        throw new IllegalArgumentException("Unknown abbreviation '" + abbreviation + ";");
    }
}
