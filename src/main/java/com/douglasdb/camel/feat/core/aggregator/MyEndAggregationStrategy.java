

package com.douglasdb.camel.feat.core.aggregator;


import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * 
 */
public class MyEndAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        
        
        if (null == oldExchange)
            return newExchange;
        
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);

        if ("END".equals(newBody))
            return oldExchange;
        
        String body = oldBody + newBody;

        oldExchange.getIn().setBody(body);

        return oldExchange;

    }

    
}