package com.douglasdb.camel.feat.core.logthroughput;

import org.apache.camel.builder.RouteBuilder;

public class LogThroughputRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .to("log:throughput?groupInterval=1000&groupDelay=500")
                .to("mock:result");

        from("direct:startInterval")
                .to("log:throughput?groupInterval=1000&groupDelay=500")
                .to("mock:result");
    }
}
