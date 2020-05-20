package com.douglasdb.camel.feat.core.errorhandling.enrich;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class EnrichFailureProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(deadLetterChannel("mock:dead").useOriginalMessage()
                .onPrepareFailure(exchange -> {
                    Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    String failure = "The message failed because " + e.getMessage();
                    exchange.getIn().setHeader("FailureMessage", failure);
                }));

        from("direct:start")
                .transform(constant("This is a changed body"))
                .throwException(new IllegalArgumentException("Forced Error"));
    }
}
