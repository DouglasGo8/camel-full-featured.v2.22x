package com.douglasdb.camel.feat.core.errorhandling.shared;

/**
 *
 */
public class FlakyException extends Exception {

    public FlakyException() {
        super();
    }

    public FlakyException(String message) {
        super(message);
    }

}
