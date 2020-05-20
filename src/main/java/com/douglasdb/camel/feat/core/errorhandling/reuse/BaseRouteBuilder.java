package com.douglasdb.camel.feat.core.errorhandling.reuse;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class BaseRouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("mock:dead")
                .maximumRedeliveries(2)
                .redeliveryDelay(1000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));
    }
}
