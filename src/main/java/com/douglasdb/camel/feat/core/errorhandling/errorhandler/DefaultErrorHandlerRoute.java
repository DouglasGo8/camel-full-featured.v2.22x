package com.douglasdb.camel.feat.core.errorhandling.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class DefaultErrorHandlerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(1000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        from("seda:queue.inbox")
                .bean("orderService", "validate")
                .bean("orderService", "enrich")
                .log("Received order ${body}")
                .to("mock:queueOrder");

    }
}
