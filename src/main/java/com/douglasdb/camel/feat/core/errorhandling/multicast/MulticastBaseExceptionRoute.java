package com.douglasdb.camel.feat.core.errorhandling.multicast;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class MulticastBaseExceptionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(MulticastBusinessException.class)
                .handled(true)
                //.useOriginalMessage()
                .log("*** ${body} ***")
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.ERROR);
    }
}
