package com.douglasdb.camel.feat.core.test.cbr;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author douglasdias
 */
public class EntryPointSpring extends CamelSpringTestSupport {

    /**
     *
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub
        final String springPath = "META-INF/spring/cbr/";
        //
        final String subPath = "complexstopotherwiserouter";
        // "simpleotherwiserouter";
        // "complexotherwiserouter";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrders() throws InterruptedException {

        super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:csv").setExpectedMessageCount(2);
        super.getMockEndpoint("mock:bad").setExpectedMessageCount(1);


        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersSimple() throws InterruptedException {

        super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:csv").setExpectedMessageCount(1);

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testPlacingOrderWithStop() throws InterruptedException {

        super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:csv").setExpectedMessageCount(2);
        super.getMockEndpoint("mock:bad").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:continued").expectedMessageCount(3);

        assertMockEndpointsSatisfied();

    }
}
