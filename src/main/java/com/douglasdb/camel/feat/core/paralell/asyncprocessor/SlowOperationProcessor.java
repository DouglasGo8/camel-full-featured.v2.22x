
package com.douglasdb.camel.feat.core.paralell.asyncprocessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class SlowOperationProcessor implements AsyncProcessor {

    private final Logger log = LoggerFactory.getLogger(SlowOperationProcessor.class);
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();


    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        final boolean completedSynchronously = false;

        this.backgroundExecutor.submit(() -> {
            log.info("Running operation asynchronously");
        });

        return completedSynchronously;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

    }
}