package com.douglasdb.camel.feat.core.dataset;

import org.apache.camel.component.dataset.DataSetSupport;

public class ExpectedOutputDataSet extends DataSetSupport {
    @Override
    protected Object createMessageBody(long messageIndex) {
        return "Modified: message " + messageIndex;
    }
}
