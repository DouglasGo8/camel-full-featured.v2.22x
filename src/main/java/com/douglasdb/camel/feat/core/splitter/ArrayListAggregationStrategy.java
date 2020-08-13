package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.domain.splitter.Record;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;

/**
 * @author dodiaba
 */
public class ArrayListAggregationStrategy extends AbstractListAggregationStrategy<Record> {

    @Override
    public Record getValue(final Exchange exchange) {
        return exchange.getIn().getBody(Record.class);
    }
}
