package com.douglasdb.camel.feat.core.test.testing;

import lombok.SneakyThrows;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SimpleTransformAnnotationsSpringTest extends CamelSpringTestSupport {

    @Produce(uri = "direct:in")
    private ProducerTemplate template;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/test-properties-context.xml",
                "/META-INF/spring/testing/simpleTransform-context.xml");
    }

    @Test
    @SneakyThrows
    public void testPayloadIsTransformed() {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Cheese");
        //
        template.sendBody("Cheese");
        assertMockEndpointsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testPayloadIsTransformedAgain() {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Foo");
        //
        template.sendBody("Foo");
        //
        assertMockEndpointsSatisfied();
    }

}
