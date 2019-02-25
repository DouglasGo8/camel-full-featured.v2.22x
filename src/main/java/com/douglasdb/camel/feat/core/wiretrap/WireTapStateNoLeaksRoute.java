package com.douglasdb.camel.feat.core.wiretrap;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

import com.douglasdb.camel.feat.core.domain.Cheese;;
/**
 * @author Douglas D.b
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
<<<<<<< HEAD
            .delay(1000) // forces delay
            .to("mock:out");
=======
                .delay(1000)
                .to("mock:out");
>>>>>>> gc001

        from("direct:processInBackground")
<<<<<<< HEAD
         	.bean(CheeseRipener.class, "ripen")
=======
            .bean(CheeseRipener.class, "ripen")
>>>>>>> 7df6dbc5cc1eeb09fba9a369beb97ae444b15e11
            .to("mock:tapped");
    }
}
