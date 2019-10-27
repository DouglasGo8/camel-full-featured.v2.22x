package com.douglasdb.camel.feat.core.errorhandling.newexception;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 */
public class NotAllowedProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        final String name = (String) exchange.getIn().getHeader("name");

        if (name.equals("Kaboom")) {
            throw new NullPointerException();
        }
    }
}
