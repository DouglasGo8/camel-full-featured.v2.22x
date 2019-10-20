package com.douglasdb.camel.feat.core.errorhandling.logging;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class LoggingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(super.loggingErrorHandler().logName("MyLoggingErrorHandler").level(LoggingLevel.ERROR));

        from("direct:start")
                .bean(FlakyProcessor.class)
                .to("mock:result");

        from("direct:routeSpecific")
                .errorHandler(super.loggingErrorHandler().logName("MyRouteSpecificLogging").level(LoggingLevel.ERROR))
                .bean(FlakyProcessor.class)
                .to("mock:result");



    }
}
