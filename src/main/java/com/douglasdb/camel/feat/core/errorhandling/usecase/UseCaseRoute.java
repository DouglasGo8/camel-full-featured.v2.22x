package com.douglasdb.camel.feat.core.errorhandling.usecase;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;

/**
 *
 */
public class UseCaseRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        super.getContext().setTracing(true);

        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(5)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        onException(HttpOperationFailedException.class)
                .maximumRedeliveries(5)
                .handled(true)
                .to("file://D:/.camel/data/upload");


        from("file://D:/.camel/data/usecase?delay=5000&readLock=none")
                .to("http://localhost:8765/rider");

    }
}
