package com.douglasdb.camel.feat.core.test.multicast;

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

    @Override
    public void setUp() throws Exception {
        deleteDirectory("activemq-data");
        super.setUp();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub

        final String springPath = "META-INF/spring/multicast/";
        //
        final String subPath = "parallel";
        // "soe";
        // "multi";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));
    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersWithMulticastSOE() throws InterruptedException {

        super.getMockEndpoint("mock:accounting_before_exception")
                .expectedMessageCount(1);
        super.getMockEndpoint("mock:accounting").expectedMessageCount(0);
        super.getMockEndpoint("mock:production").expectedMessageCount(0);

        assertMockEndpointsSatisfied();
    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersWithMulticast() throws InterruptedException {


        super.getMockEndpoint("mock:accounting").expectedMessageCount(1);
        super.getMockEndpoint("mock:production").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }


    /**
     * @throws InterruptedException
     */
    @Test

    public void testPlacingOrdersWithParallelMulticast() throws InterruptedException {


        super.getMockEndpoint("mock:accounting").expectedMessageCount(1);
        super.getMockEndpoint("mock:production").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }


}
