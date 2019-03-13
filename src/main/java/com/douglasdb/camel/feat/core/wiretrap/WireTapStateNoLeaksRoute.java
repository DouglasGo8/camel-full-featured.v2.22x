package com.douglasdb.camel.feat.core.wiretrap;

import com.douglasdb.camel.feat.core.domain.cheese.Cheese;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;


/**
 * @author Douglas D.b
 * 
 */
public class WireTapStateNoLeaksRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {


        from("direct:start")
            .log("Cheese is ${body.age} months old")
            .wireTap("direct:processInBackground")
                .onPrepare(exchange -> {
                    Message in = exchange.getIn();
                    Cheese cheese = in.getBody(Cheese.class);
                    if (cheese != null)
                        in.setBody(cheese.clone());
                })
            .delay(1000) // forces delay
            .to("mock:out");
                

        from("direct:processInBackground")
         	.bean(CheeseRipener.class, "ripen")
            //.bean(CheeseRipener.class, "ripen")
            .to("mock:tapped");
    }
}
