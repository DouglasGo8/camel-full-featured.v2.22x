package com.douglasdb.camel.feat.core.test.test;

import com.douglasdb.camel.feat.core.test.advice.WeaveByIdRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class WeaveByIdTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new WeaveByIdRoute();
    }

    @Test
    public void testWeaveById() throws Exception {

        final RouteDefinition route = super.context.getRouteDefinition("quotes");

        route.adviceWith(super.context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveById("transform").replace()
                        .transform().simple("${body.toUpperCase()}");
                weaveAddLast().to("mock:result");
            }
        });

        context.start();

        super.getMockEndpoint("mock:result").expectedBodiesReceived("HELLO CAMEL");
        //
        super.template.sendBody("seda:quotes", "Hello Camel");
        //
        assertMockEndpointsSatisfied();

    }
}
