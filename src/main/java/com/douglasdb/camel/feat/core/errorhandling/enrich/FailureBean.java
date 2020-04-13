package com.douglasdb.camel.feat.core.errorhandling.enrich;

import org.apache.camel.Headers;

import java.util.Map;

/**
 *
 */
public class FailureBean {

    @SuppressWarnings("unchecked")
    public void enrich(@Headers Map headers, Exception cause) throws Exception {
        String failure = "The message failed because " + cause.getMessage();
        headers.put("FailureMessage", failure);
    }
}
