package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitAggregateExceptionHandlingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body(), new ExceptionHandlingSetAggregationStrategy())
                    .inOut("direct:out")
                .end()
                .to("mock:out");


        from("direct:out")
                .choice()
                    .when(simple("${header.CamelSplitIndex} == 1"))
                        .throwException(new IllegalStateException())
                    .otherwise()
                        .transform(simple("Processed: ${body}"))
                .end();

    }
}
