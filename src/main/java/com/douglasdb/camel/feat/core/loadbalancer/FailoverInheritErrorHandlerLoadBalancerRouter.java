
package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class FailoverInheritErrorHandlerLoadBalancerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        /*errorHandler(defaultErrorHandler()
            .maximumRedeliveries(3)
            .redeliveryDelay(2000)
                // reduce some logging noise
            .retryAttemptedLogLevel(LoggingLevel.WARN)
            .retriesExhaustedLogLevel(LoggingLevel.WARN)
            .logStackTrace(false));*/

        from("direct:start")
            .log("${body}")
                // use load balancer with failover strategy
                // 1 = which will try 1 failover attempt before exhausting
                // true = do use Camel error handling
                // false = do not use round robin mode
                //.loadBalance().failover(1, true, false)
                // will send to A first, and if fails then send to B afterwards
                //.to("direct:a")
                //.to("direct:b")
            .end();

        // service A
       // from("direct:a").log("A received: ${body}").to("mock:a");

        // service B
        //from("direct:b").log("B received: ${body}").to("mock:b");
    }

}
