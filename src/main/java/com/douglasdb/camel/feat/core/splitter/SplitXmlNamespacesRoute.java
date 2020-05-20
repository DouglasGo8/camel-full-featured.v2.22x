package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;


/**
 * @author dbatista
 */
public class SplitXmlNamespacesRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        final Namespaces ns = new Namespaces("c", "http://camelcookbook.org/schema/books")
                .add("se", "http://camelcookbook.org/schema/somethingElse");

        from("direct:in")
               // .log("${body}")
                .split(ns.xpath("/c:books/c:book[@category='Tech']/c:authors/c:author/text()"))
                    .to("mock:out")
                .end();

    }
}
