package com.douglasdb.camel.feat.core.paralell.asyncrequest;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SlowProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:processInOut")
                .log("Received ${body}")
                .delay(1000)
                .log("Processing ${body}")
                .transform(simple("Processed ${body}"));
    }
}
