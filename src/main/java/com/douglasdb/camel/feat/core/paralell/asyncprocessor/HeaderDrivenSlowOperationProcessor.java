package com.douglasdb.camel.feat.core.paralell.asyncprocessor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dbatista
 */
public class HeaderDrivenSlowOperationProcessor implements AsyncProcessor {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public boolean process(Exchange exchange, AsyncCallback asyncCallback) {

        final Message in = exchange.getIn();

        if (in.getHeader("processAsync", Boolean.class)) {
            executorService.submit(() -> {
                in.setBody("Processed async: " + in.getBody(String.class));
                asyncCallback.done(false);
            });
            return false;
        } else {
            in.setBody("Processed sync: " + in.getBody(String.class));
            asyncCallback.done(true);
            return true;
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        throw new IllegalStateException("Should never be called");
    }
}
