package com.douglasdb.camel.feat.core.errorhandling.reuse;

/**
 * @author dbatista
 */
public class OrderValidationException extends Exception {
    
    public OrderValidationException(String message) {
        super(message);
    }
}
