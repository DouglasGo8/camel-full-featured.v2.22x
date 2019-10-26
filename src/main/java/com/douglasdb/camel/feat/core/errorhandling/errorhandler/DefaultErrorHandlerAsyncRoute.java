package com.douglasdb.camel.feat.core.errorhandling.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class DefaultErrorHandlerAsyncRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()
                    .asyncDelayedRedelivery()
                    .maximumRedeliveries(2)
                    .log("---> ${threadName}")
                    .redeliveryDelay(1000)
                    .retryAttemptedLogLevel(LoggingLevel.WARN));

        from("seda:queue.inbox")
                .bean("orderService", "validate")
                .bean("orderService", "enrich")
                .log("Recevied order ${body}")
                .to("mock:queueOrder");
    }
}
