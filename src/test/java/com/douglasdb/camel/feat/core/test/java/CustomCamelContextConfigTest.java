package com.douglasdb.camel.feat.core.test.java;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.seda.SedaComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CustomCamelContextConfigTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final CamelContext context = new DefaultCamelContext();
        context.addComponent("activemq", new SedaComponent()); // Custom
        return context;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:in").to("activemq:orders");
                from("activemq:orders").to("mock:out");
            }
        };
    }

    @Test
    @SneakyThrows
    public void testMessagesFlowOverQueue() {
        final MockEndpoint out = getMockEndpoint("mock:out");
        out.setExpectedMessageCount(1);
        out.expectedBodiesReceived("hello");

        template.sendBody("direct:in", "hello");

        assertMockEndpointsSatisfied();
    }
}
