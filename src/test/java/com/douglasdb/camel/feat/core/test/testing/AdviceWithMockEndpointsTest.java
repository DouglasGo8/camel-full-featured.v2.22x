package com.douglasdb.camel.feat.core.test.testing;

import com.douglasdb.camel.feat.core.testing.advice.AdviceWithRouter;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class AdviceWithMockEndpointsTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new AdviceWithRouter();
    }


    @Test
    public void testMockEndpoints() throws Exception {

        final RouteDefinition route = super.context.getRouteDefinition("quotes");

        route.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.mockEndpoints();
            }
        });
        // mandatory start Camel after we are done using advice-with
        super.context.start();
        //
        super.getMockEndpoint("mock:seda:camel").expectedBodiesReceived("Camel rocks");
        super.getMockEndpoint("mock:seda:other").expectedBodiesReceived("Bad donkey");
        //
        super.template.sendBody("seda:quotes", "Camel rocks");
        super.template.sendBody("seda:quotes", "Bad donkey");
        //
        assertMockEndpointsSatisfied();
    }
}
