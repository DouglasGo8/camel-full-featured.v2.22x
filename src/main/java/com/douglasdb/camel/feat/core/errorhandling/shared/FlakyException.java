package com.douglasdb.camel.feat.core.errorhandling.shared;

/**
 *
 */
public class FlakyException extends Exception {

    FlakyException(String message) {
        super(message);
    }

}
