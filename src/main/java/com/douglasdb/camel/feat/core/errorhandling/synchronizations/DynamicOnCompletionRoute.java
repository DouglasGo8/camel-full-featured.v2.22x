package com.douglasdb.camel.feat.core.errorhandling.synchronizations;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class DynamicOnCompletionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
            .process(new ConfirmCancelProcessor())
                .choice()
                    .when(simple("${body} contains 'explode'"))
                        .throwException(new IllegalArgumentException("Exchange caused explosion"))
                .end()
                .log("Processed message");

    }
}
