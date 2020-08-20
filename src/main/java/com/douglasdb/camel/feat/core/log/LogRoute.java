package com.douglasdb.camel.feat.core.log;

import org.apache.camel.builder.RouteBuilder;

public class LogRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:start").to("log:myLog").to("mock:result");
        from("direct:startAll").to("log:myLog?level=INFO&showAll=true&multiline=true").to("mock:result");
    }
}
