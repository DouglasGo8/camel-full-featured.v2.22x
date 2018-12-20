package com.douglasdb.camel.feat.core.recipientlist;

import org.apache.camel.builder.RouteBuilder;


/**
 *
 */
public class RecipientListUnrecognizedEndpointRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .setHeader("multicastTo")
                    .constant("direct:first,direct:second,websphere:cheese")
                .recipientList()
                .header("multicastTo")
                    .ignoreInvalidEndpoints();

        from("direct:first")
                .to("mock:first");

        from("direct:second")
                .to("mock:second");

    }
}
