package com.douglasdb.camel.feat.core.microservice.standalone;


import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:13166/hello")
                .transform(simple("Hello from Camel"));
    }
}
