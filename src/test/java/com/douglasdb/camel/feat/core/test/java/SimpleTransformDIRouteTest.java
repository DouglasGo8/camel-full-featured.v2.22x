package com.douglasdb.camel.feat.core.test.java;

import com.douglasdb.camel.feat.core.java.SimpleTransformDIRoute;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleTransformDIRouteTest extends CamelTestSupport {

    @Produce(uri = "direct:in") private ProducerTemplate producerTemplate;
    @EndpointInject(uri = "mock:out") private MockEndpoint mockOut;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        SimpleTransformDIRoute routeBuilder = new SimpleTransformDIRoute();
        routeBuilder.setSourceUri("direct:in");
        routeBuilder.setTargetUri("mock:out");
        return routeBuilder;
    }

    @Test
    public void testPayloadIsTransformed() throws InterruptedException {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Cheese");

        producerTemplate.sendBody("Cheese");

        assertMockEndpointsSatisfied();
    }
}
