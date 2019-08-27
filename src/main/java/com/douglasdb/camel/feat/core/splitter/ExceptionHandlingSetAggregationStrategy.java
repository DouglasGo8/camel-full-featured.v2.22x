package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dbatista
 */
public class ExceptionHandlingSetAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        String body = newExchange.getIn().getBody(String.class);
        Exception exception = newExchange.getException();

        if (null != exception) {
            newExchange.setException(null); // bypass the exception
            body = "Failed: " + body;
        }

        if (null == oldExchange) {
            Set<String> set = new HashSet<String>();
            set.add(body);
            newExchange.getIn().setBody(set);
            return newExchange;
        } else {
            @SuppressWarnings("unchecked")
            Set<String> set = Collections.checkedSet(oldExchange.getIn().getBody(Set.class), String.class);
            set.add(body);
            return oldExchange;
        }


    }
}
