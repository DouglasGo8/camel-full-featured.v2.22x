package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitXmlNamespaceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
                .split(xpath("/c:books/c:book[@category='Tech']/c:authors/c:author/text()")
                        .namespace("c", "http://camelcookbook.org/schema/books"))
                .to("mock:out")
                .end();
    }
}
