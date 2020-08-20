package com.douglasdb.camel.feat.core.log;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class LogEipRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:start").routeId("LogEipRoute").log("Something interesting happened - ${body}")
                .to("mock:result");
        //
        from("direct:startLevel").routeId("LogEipInfoLevelRoute")
                .log(LoggingLevel.INFO, "Something informational happened - ${body}")
                .to("mock:result");
        //
        from("direct:startName").routeId("LogEipCustomLogNameRoute")
                .log(LoggingLevel.INFO, "MyName", "Something myName happened - ${body}")
                .to("mock:result");
    }
}
