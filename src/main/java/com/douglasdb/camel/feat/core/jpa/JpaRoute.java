package com.douglasdb.camel.feat.core.jpa;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class JpaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:accounting")
                .to("jpa:com.douglasdb.camel.feat.core.domain.jpa.PurchaseOrder")
                .to("mock:result");

    }
}
