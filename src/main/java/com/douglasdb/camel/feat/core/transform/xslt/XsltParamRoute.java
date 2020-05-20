package com.douglasdb.camel.feat.core.transform.xslt;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class XsltParamRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .to("xslt:META-INF/bookstore/book-param.xslt");

        from("direct:start2")
                .to("xslt:META-INF/bookstore/book.xslt");

    }
}
