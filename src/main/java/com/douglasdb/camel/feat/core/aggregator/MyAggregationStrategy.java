

package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * 
 */
public class MyAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        
        // first Time there are no messages therefore oldExchange
        // always be null
        
        if (null == oldExchange)
            return newExchange;

        final String oldBody = oldExchange.getIn().getBody(String.class).trim();
        final String newBody = newExchange.getIn().getBody(String.class).trim();

        final String sumUpBody = oldBody + newBody;

        oldExchange.getIn().setBody(sumUpBody);

        return oldExchange;
    }
    
}