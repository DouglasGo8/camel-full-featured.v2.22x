package com.douglasdb.camel.feat.core.errorhandling.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class ScopeRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // this is the default error handler which is context scoped
        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(2)
                .redeliveryDelay(1000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        from("file://D:/.camel/data/orders?delay=1000")
                .bean("orderService", "toCsv")
                .to("mock:file")
                .to("seda:queue.inbox");

        from("seda:queue.inbox")
                .errorHandler(deadLetterChannel("log:DLC")
                        .maximumRedeliveries(5)
                        .retryAttemptedLogLevel(LoggingLevel.INFO)
                        .redeliveryDelay(250)
                        .backOffMultiplier(2))
                .bean("orderService", "validate")
                .bean("orderService", "enrich")
                .to("mock:queueOrder");

    }
}
