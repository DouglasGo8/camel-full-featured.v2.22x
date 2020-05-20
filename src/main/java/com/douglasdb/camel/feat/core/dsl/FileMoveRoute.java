package com.douglasdb.camel.feat.core.dsl;

import org.apache.camel.builder.RouteBuilder;

public class FileMoveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://F:/.camel/data/inbox")
                .to("file://F:/.camel/data/outbox");
    }
}
