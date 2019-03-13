package com.douglasdb.camel.feat.core.bean.predicate;

/**
 * 
 */
public class CompoundValidate {
    public static boolean isAuthor(String xml) {
        return xml.contains("Claus") || xml.contains("Jonathan");
    }
}