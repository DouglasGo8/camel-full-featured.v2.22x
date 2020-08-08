package com.douglasdb.camel.feat.core.dataset;

import org.apache.camel.Exchange;
import org.apache.camel.component.dataset.DataSetSupport;

public class InputDataSet extends DataSetSupport {

    @Override
    protected Object createMessageBody(long messageIndex) {
        return "message " + messageIndex;
    }

    @Override
    protected void applyHeaders(Exchange exchange, long messageIndex) {
        exchange.getIn().setHeader("mySequenceId", messageIndex);
    }
}
