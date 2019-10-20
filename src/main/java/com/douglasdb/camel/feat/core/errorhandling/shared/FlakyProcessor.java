package com.douglasdb.camel.feat.core.errorhandling.shared;


import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FlakyProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(FlakyProcessor.class);

    public void doSomething(Exchange exchange) throws FlakyException {
        if (exchange.getProperty("optimizeBit", false, boolean.class)) {
            LOG.info("FlakyProcessor works with optimizationBit set");
            return;
        }

        if ("KaBoom".equalsIgnoreCase(exchange.getIn().getBody(String.class))) {
            LOG.error("Throwing FlakyException");
            throw new FlakyException(" -> Ouch!!! FlakyProcessor has gone Flaky");
        }

    }

}
