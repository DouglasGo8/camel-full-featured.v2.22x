package com.douglasdb.camel.feat.core.test.test;

import com.douglasdb.camel.feat.core.test.advice.ReplaceFromRouter;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ReplaceFromTest extends CamelTestSupport {


    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ReplaceFromRouter();
    }

    @Test
    public void testReplaceFromWithEndpoints() throws Exception {
        RouteDefinition route = context.getRouteDefinition("quotes");
        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // replace the incoming endpoint with a direct endpoint
                // we can easily call from unit test
                replaceFromWith("direct:hitme");
                // and then mock all seda endpoints os we can use mock endpoints
                // to assert the test is correct
                mockEndpoints("seda:*");
            }
        });

        // must start Camel after we are done using advice-with
        super.context.start();
        //
        super.getMockEndpoint("mock:seda:camel").expectedBodiesReceived("Camel rocks");
        super.getMockEndpoint("mock:seda:other").expectedBodiesReceived("Bad donkey");

        super.template.sendBody("direct:hitme", "Camel rocks");
        super.template.sendBody("direct:hitme", "Bad donkey");

        assertMockEndpointsSatisfied();
    }
}
