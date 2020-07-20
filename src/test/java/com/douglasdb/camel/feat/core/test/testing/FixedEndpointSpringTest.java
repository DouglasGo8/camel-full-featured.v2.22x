package com.douglasdb.camel.feat.core.test.testing;

import lombok.SneakyThrows;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FixedEndpointSpringTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/fixedEndpoints-context.xml");
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    @SneakyThrows
    public void testOverriddenEndpoints() {
        context.getRouteDefinition("modifyPayloadBetweenQueues")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        //
                        replaceFromWith("direct:in");
                        //
                        interceptSendToEndpoint("activemq:out")
                                .skipSendToOriginalEndpoint()
                                .to("mock:out");
                    }
                });
        context.start();
        //
        MockEndpoint out = getMockEndpoint("mock:out");
        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("Modified: Cheese");
        template.sendBody("direct:in", "Cheese");
        //
        assertMockEndpointsSatisfied();
    }
}
