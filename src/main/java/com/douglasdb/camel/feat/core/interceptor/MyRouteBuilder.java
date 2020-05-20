package com.douglasdb.camel.feat.core.interceptor;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class MyRouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("file:///F:/.camel/data/inbox/interceptor?noop=true")
                .choice()
                    .when(xpath("/person/city = 'London'"))
                        .to("file:///F:/.camel/data/outbox/uk")
                    .otherwise()
                        .to("file:///F:/.camel/data/outbox/others")
                .end()
                .to("mock:result");
    }
}
