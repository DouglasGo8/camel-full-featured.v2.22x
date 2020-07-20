package com.douglasdb.camel.feat.core.testing.advice;

import org.apache.camel.builder.RouteBuilder;

public class ReplaceFromRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("aws-sqs:quotes").routeId("quotes")
                .choice()
                .when(simple("${body} contains 'Camel'"))
                    .to("seda:camel")
                .otherwise()
                    .to("seda:other")
                .end();
    }
}
