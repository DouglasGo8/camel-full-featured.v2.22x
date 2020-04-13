package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitParallelProcessingExceptionHandlingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body())
                    .parallelProcessing()
                    .stopOnException()
                .log("Processing message[${property.CamelSplitIndex}]")
                .to("direct:failOn20th")
                .end()
                .to("mock:out");

        from("direct:failOn20th")
                .choice()
                    .when(simple("${property.CamelSplitIndex} == 20"))
                        .throwException(new IllegalStateException("boom"))
                .otherwise()
                    .to("mock:split")
                .end();
    }
}
