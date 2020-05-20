package com.douglasdb.camel.feat.core.test.test;

import com.douglasdb.camel.feat.core.test.advice.WeaveByIdRoute;
import com.douglasdb.camel.feat.core.test.advice.WeaveByToUriRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

/**
 * @author dodiaba
 */
public class WeaveByToUriTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        // remember to override this method and return true to tell Camel that we are using advice-with in the routes
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new WeaveByToUriRoute();
    }

    @Test
    public void testWeaveByToUri() throws Exception {
        final RouteDefinition route = super.context.getRouteDefinition("quotes");

        route.adviceWith(super.context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveByToUri("seda:line").replace().to("mock:line");
            }
        });
        //
        super.context.start();
        //
        super.getMockEndpoint("mock:line").expectedBodiesReceived("camel rules", "donkey is bad");
        super.getMockEndpoint("mock:combined").expectedMessageCount(1);

        super.getMockEndpoint("mock:combined").message(0).body().isInstanceOf(List.class);
        //
        super.template.sendBody("seda:quotes","Camel Rules,Donkey is Bad");
        //
        assertMockEndpointsSatisfied();
    }

}
