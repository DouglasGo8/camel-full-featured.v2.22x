package com.douglasdb.camel.feat.core.test.testing;


import lombok.SneakyThrows;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dbatista
 */
public class SimpleTransformSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/test-properties-context.xml",
                "/META-INF/spring/testing/simpleTransform-context.xml");
    }

    @Test
    @SneakyThrows
    public void testPayloadIsTransformed() {
        final MockEndpoint mock = super.getMockEndpoint("mock:out");
        //
        mock.setExpectedMessageCount(1);
        mock.message(0).body().isEqualTo("Modified: Cheese");
        //
        super.template.sendBody("direct:in", "Cheese");
        //
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testPayloadIsTransformedAgain() throws InterruptedException {
        final MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Foo");

        template.sendBody("direct:in", "Foo");

        assertMockEndpointsSatisfied();
    }

}
