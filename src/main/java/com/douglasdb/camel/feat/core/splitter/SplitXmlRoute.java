package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitXmlRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
                .split(xpath("/books/book[@category='Tech']/authors/author/text()"))
                   .log("${body}")
                   .to("mock:out")
                .end();
    }
}
