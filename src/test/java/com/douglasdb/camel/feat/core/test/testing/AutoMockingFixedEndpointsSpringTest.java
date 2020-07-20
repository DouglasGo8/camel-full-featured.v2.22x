package com.douglasdb.camel.feat.core.test.testing;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoMockingFixedEndpointsSpringTest extends CamelSpringTestSupport {

    @Produce(uri="activemq:in")
    private ProducerTemplate in;

    @EndpointInject(uri = "mock:activemq:out")
    private MockEndpoint mockOut;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/fixedEndpoints-context.xml");
    }


    @Override
    public String isMockEndpoints() {
        return "activemq:out";
    }

    @Test
    public void testTransformationThroughAutoMock() throws InterruptedException {
        this.mockOut.expectedBodiesReceived("Modified: testMessage");
        in.sendBody("testMessage");
        mockOut.assertIsSatisfied();
    }
}
