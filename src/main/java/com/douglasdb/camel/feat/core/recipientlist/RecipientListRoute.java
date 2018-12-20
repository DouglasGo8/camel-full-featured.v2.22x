package com.douglasdb.camel.feat.core.recipientlist;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class RecipientListRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .setHeader("endpointsToBeTriggered")
                .method(RecipientsBean.class, "getEndpointsToRouteMessageTo")
                .recipientList(header("endpointsToBeTriggered"));


        from("direct:order.priority").to("mock:order.priority");
        from("direct:order.normal").to("mock:order.normal");
        from("direct:billing").to("mock:billing");
        from("direct:unrecognized").to("mock:unrecognized");
    }
}
