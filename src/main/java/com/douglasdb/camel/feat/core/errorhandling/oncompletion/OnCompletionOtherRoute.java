package com.douglasdb.camel.feat.core.errorhandling.oncompletion;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class OnCompletionOtherRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:inAnotherRouteBuilder")
                .log("No global onCompletion should apply")
                .to("mock:outAnotherRouteBuilder");


    }
}
