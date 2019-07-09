

package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 */
public class SplitterABCRoute extends RouteBuilder {



    /**
     * 
     */
    @Override
    public void configure() throws Exception {
        

        from("direct:start")
            .split(body())
            .log("Split line ${body}")
            .to("mock:split");

    }
}