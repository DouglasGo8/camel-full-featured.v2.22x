package com.douglasdb.camel.feat.core.test.java;

import com.douglasdb.camel.feat.core.java.SimpleTransformRoute;
import lombok.SneakyThrows;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleTransformRouteAnnotationsTest extends CamelTestSupport {

    @Produce(uri = "direct:in")
    private ProducerTemplate producerTemplate;
    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SimpleTransformRoute();
    }

    @Test
    @SneakyThrows
    public void testPayloadIsTransformed() {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Cheese");

        producerTemplate.sendBody("Cheese");

        assertMockEndpointsSatisfied();
    }


}
