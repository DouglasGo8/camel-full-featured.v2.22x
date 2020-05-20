package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitMultiLineRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body().tokenize("\n"))
                .to("mock:out")
                .end();
    }
}
