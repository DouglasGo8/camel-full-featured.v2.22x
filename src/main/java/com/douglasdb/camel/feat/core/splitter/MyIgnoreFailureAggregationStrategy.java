
package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * 
 */
public class MyIgnoreFailureAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (newExchange.getException() != null) {
            // yes there was, so we just handle it by ignoring it
            return oldExchange;

        }

        if (oldExchange == null) {
            // this is the first time so no existing aggregated exchange
            return newExchange;
        }

        final String body = newExchange.getIn().getBody(String.class);
        final String existing = oldExchange.getIn().getBody(String.class);

        oldExchange.getIn().setBody(existing + "+" + body);
        return oldExchange;

    }

}