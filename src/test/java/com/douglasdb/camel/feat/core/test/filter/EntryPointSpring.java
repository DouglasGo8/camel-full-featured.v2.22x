package com.douglasdb.camel.feat.core.test.filter;

import org.apache.camel.test.spring.CamelSpringTestSupport;
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
        final String springPath = "META-INF/spring/filter";
        //
        final String subPath = "";
        // "simpleotherwiserouter";
        // "complexotherwiserouter";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));
    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testPlacingOrders() throws InterruptedException {

        super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }

}
