package com.douglasdb.camel.feat.core.test.recipientlist;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
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

    /**
     *
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub
        final String springPath = "META-INF/spring/recipientlist/";
        //
        final String subPath = "annotation";
        // "soe";
        // "multi";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));

    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersRecipientListAnnotation() throws InterruptedException {
        getMockEndpoint("mock:accounting").expectedMessageCount(2);
        getMockEndpoint("mock:production").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }

    @Test

    public void testPlacingOrdersRecipientList() throws InterruptedException {
        getMockEndpoint("mock:accounting").expectedMessageCount(2);
        getMockEndpoint("mock:production").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }


}
