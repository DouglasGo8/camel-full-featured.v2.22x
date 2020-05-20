package com.douglasdb.camel.feat.core.test.miranda;

import com.douglasdb.camel.feat.core.miranda.MirandaRouter;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MirandaRouter();
    }


    @Test
    public void testMiranda() throws InterruptedException {

        super.context.setTracing(true);

        final MockEndpoint mock = super.getMockEndpoint("mock:miranda");
        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(exchange -> {
            exchange.getIn().setBody("ID=123,STATUS=IN PROGRESS");
        });

        final String out = super.template.requestBody("http://localhost:9080/service/order?id=123",
                null, String.class);
        assertEquals("IN PROGRESS", out);

        assertMockEndpointsSatisfied();
    }


    @Test
    public void testMirandaJava8() throws Exception {

        context.setTracing(true);

        final MockEndpoint mock = getMockEndpoint("mock:miranda");

        mock.expectedBodiesReceived("ID=123");
        mock.whenAnyExchangeReceived(e -> e.getIn().setBody("ID=123,STATUS=IN PROGRESS"));

        final String out = fluentTemplate.to("http://localhost:9081/service/order?id=123").request(String.class);
        assertEquals("IN PROGRESS", out);

        assertMockEndpointsSatisfied();
    }

}
