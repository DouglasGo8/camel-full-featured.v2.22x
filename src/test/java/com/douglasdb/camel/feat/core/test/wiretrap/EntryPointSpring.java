package com.douglasdb.camel.feat.core.test.wiretrap;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author douglasdias
 */
public class EntryPointSpring extends CamelSpringTestSupport {


    public EntryPointSpring() {
        // TODO Auto-generated constructor stub
    }


    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub
        return new ClassPathXmlApplicationContext("/META-INF/spring/wiretrap/app-context.xml");
    }

    @Test
    public void testPlacingOrders() throws InterruptedException {

        super.getMockEndpoint("mock:wiretap").expectedMessageCount(1);
        super.getMockEndpoint("mock:xml").expectedMessageCount(1);
        super.getMockEndpoint("mock:csv").expectedMessageCount(0);

        assertMockEndpointsSatisfied();
    }


}
