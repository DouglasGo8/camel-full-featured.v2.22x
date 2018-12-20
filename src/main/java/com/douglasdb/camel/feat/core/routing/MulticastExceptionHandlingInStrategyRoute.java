package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MulticastExceptionHandlingInStrategyRoute extends RouteBuilder {


    private Logger logger = LoggerFactory.getLogger(MulticastExceptionHandlingInStrategyRoute.class);


    public MulticastExceptionHandlingInStrategyRoute() {
    }

    /**
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {

        from("direct:start")
                .multicast()
                
                	//
                	// Aggregation Strategy
                	//
                    .aggregationStrategy((oldExchange, newExchange) -> {

                    final String MULTICAST__THROWEXCPT = "multicast_exception";

                    if (oldExchange == null) {
                        if (newExchange.isFailed()) {
                            logger.info("newExchange is Failed 1");
                            // this block only gets called if stopOnException() is not defined on the multicast
                            Exception ex = newExchange.getException();
                            newExchange.setException(null);
                            newExchange.setProperty(MULTICAST__THROWEXCPT, ex);
                        }
                        return newExchange;
                    } else {
                        if (newExchange.isFailed()) {
                            // this block only gets called if stopOnException() is not defined on the multicast
                            logger.info("newExchange is Failed 2");
                            Exception ex = newExchange.getException();
                            oldExchange.setProperty(MULTICAST__THROWEXCPT, ex);
                        }

                        final String body1 = oldExchange.getIn().getBody(String.class);
                        final String body2 = newExchange.getIn().getBody(String.class);
                        final String merged = (body1 == null) ? body2 : body1 + "," + body2;
                        oldExchange.getIn().setBody(merged);

                        return oldExchange;
                    }


                })
                //  
                //
                //
                .to("direct:first")
                .to("direct:second")
                .end()
                .log("continuing with ${body}")
                .to("mock:afterMulticast")
                .transform(body()); // copy the In message to the Out message; this will become the route response

        from("direct:first")
                .onException(Exception.class)
                    .log("Caught Exception")
                    .to("mock:exceptionHandler")
                    .transform(constant("Oops"))
                .end()
                .to("mock:first")
                .process(exchange -> {
                    throw new IllegalStateException("Something went horribly wrong");
                });
        from("direct:second")
                .to("mock:second")
                .transform(constant("ALL Ok Here"));
    }
}
