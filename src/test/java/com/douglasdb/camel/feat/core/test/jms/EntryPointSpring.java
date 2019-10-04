package com.douglasdb.camel.feat.core.test.jms;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 *
 */
public class EntryPointSpring extends CamelSpringTestSupport {


    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub
        final String springPath = "META-INF/spring/jms";
        return new ClassPathXmlApplicationContext(springPath.concat("/app-context.xml"));
    }

    @Test
    @Ignore
    public void testMock() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:end");
        mock.setExpectedMessageCount(1);

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testSendFileContentToJms() throws InterruptedException {

        super.getMockEndpoint("mock:end").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }
}
