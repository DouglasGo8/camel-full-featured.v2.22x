package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;


/**
 *
 */
public class MulticastWithAggregationOfRequestRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {


        from("direct:start")
                .enrich("direct:performMulticast",
                        MulticastWithAggregationOfRequestRoute::aggregate)
                .transform(body());

        from("direct:performMulticast")
                .multicast()
                .aggregationStrategy(MulticastWithAggregationOfRequestRoute::aggregate)
                .to("direct:first")
                .to("direct:second")
                .end();


        from("direct:first")
                .transform(constant("first response"));

        from("direct:second")
                .transform(constant("second response"));

    }



    /**
     * @param exchange1
     * @param exchange2
     * @return
     */
    protected static Exchange aggregate(Exchange exchange1, Exchange exchange2) {
        if (exchange1 == null) {
            return exchange2;
        } else {
            String body1 = exchange1.getIn().getBody(String.class);
            String body2 = exchange2.getIn().getBody(String.class);
            String merged = (body1 == null) ? body2 : body1 + "," + body2;
            exchange1.getIn().setBody(merged);
            return exchange1;
        }
    }
}
