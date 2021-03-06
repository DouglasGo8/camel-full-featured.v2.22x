package com.douglasdb.camel.feat.core.test.testing;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

public class SimpleTransformPropertiesInjectingSpringTest extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/test-properties-context.xml",
                "/META-INF/spring/testing/simpleTransform-context.xml");
    }

    @Override
    protected Boolean ignoreMissingLocationWithPropertiesComponent() {
        return true;
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        final Properties properties = new Properties();
        properties.setProperty("start.endpoint", "direct:in");
        properties.setProperty("end.endpoint", "mock:out");
        return properties;
    }

    @Test
    public void testPayloadIsTransformed() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Cheese");

        template.sendBody("direct:in", "Cheese");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testPayloadIsTransformedAgain() throws InterruptedException {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Foo");

        template.sendBody("direct:in", "Foo");

        assertMockEndpointsSatisfied();
    }
}
