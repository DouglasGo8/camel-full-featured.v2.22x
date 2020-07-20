package com.douglasdb.camel.feat.core.testing.advice;

import org.apache.camel.builder.RouteBuilder;

import java.util.ArrayList;

import static org.apache.camel.util.toolbox.AggregationStrategies.flexible;

public class WeaveByToUriRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("seda:quotes").routeId("quotes")
                .split(body(), flexible().accumulateInCollection(ArrayList.class))
                    .transform(simple("${body.toLowerCase()}"))
                    .to("seda:line")
                .end()
                .to("mock:combined");
    }
}
