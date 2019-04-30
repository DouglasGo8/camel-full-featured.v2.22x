package com.douglasdb.camel.feat.core.transform.enrich;

import org.apache.camel.builder.RouteBuilder;


/**
 *
 */
public class EnrichXsltRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .enrich("xslt:META-INF/bookstore/book.xslt");
    }
}
