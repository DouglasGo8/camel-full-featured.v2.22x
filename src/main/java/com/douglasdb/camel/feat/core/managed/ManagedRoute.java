package com.douglasdb.camel.feat.core.managed;

import org.apache.camel.builder.RouteBuilder;

public class ManagedRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start").bean(MyManagedBean.class).id("myManagedBean").log("${body}").to("mock:result");
    }
}
