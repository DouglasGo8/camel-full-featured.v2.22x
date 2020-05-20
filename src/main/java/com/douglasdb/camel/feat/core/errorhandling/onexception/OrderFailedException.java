package com.douglasdb.camel.feat.core.errorhandling.onexception;

/**
 *
 */
public class OrderFailedException extends Exception {

    public OrderFailedException(String message) {
        super(message);
    }

    public OrderFailedException(String message, Throwable t) {
        super(message, t);
    }


}
