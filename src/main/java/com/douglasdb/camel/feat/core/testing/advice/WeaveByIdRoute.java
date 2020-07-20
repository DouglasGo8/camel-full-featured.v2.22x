package com.douglasdb.camel.feat.core.testing.advice;

import org.apache.camel.builder.RouteBuilder;

public class WeaveByIdRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:quotes").routeId("quotes")
                .bean("transformer")
                .id("transform")
                .to("seda:lower");
    }
}
