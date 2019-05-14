package com.douglasdb.camel.feat.core.transform.enrich;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class EnrichRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .enrich("bean:myExpander?method=expand");

    }
}

