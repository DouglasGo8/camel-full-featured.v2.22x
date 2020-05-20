

package com.douglasdb.camel.feat.core.aggregator;

import static java.util.Collections.checkedSet;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * 
 */
public class SetAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        final String body = newExchange.getIn().getBody(String.class);

        /**
         * first time there are no messages therefore oldExchange always be null
         */
        if (null == oldExchange) {
            Set<String> set = new HashSet<>();
            set.add(body);
            newExchange.getIn().setBody(set);

            return newExchange;
            
        } else {
            @SuppressWarnings("unchecked")
            Set<String> set = checkedSet(oldExchange.getIn().getBody(Set.class), String.class);
            set.add(body);
            return oldExchange;
        }

    }
    
}