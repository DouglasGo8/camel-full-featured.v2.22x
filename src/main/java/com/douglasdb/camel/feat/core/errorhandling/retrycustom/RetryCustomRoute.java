package com.douglasdb.camel.feat.core.errorhandling.retrycustom;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class RetryCustomRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()
                .onRedelivery(exchange -> {
                    exchange.setProperty("optimizeBit", true);
                })
                .maximumRedeliveries(2));

        from("direct:start")
                .bean(FlakyProcessor.class)
                .to("mock:result");
    }
}
