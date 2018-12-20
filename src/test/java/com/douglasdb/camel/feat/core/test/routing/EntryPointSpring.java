package com.douglasdb.camel.feat.core.test.routing;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.mock.MockEndpoint;
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
        final String springPath = "META-INF/spring/routing/";
        //
        final String subPath = "loadbalance/failover";
        // "loadbalance/sticky";
        // "filter";
        // "dynamic/annotated";
        // "dynamic/bean";
        // "contentbased";
        // "soe";
        // "multi";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOnlyMEPChangedForOneWay() throws InterruptedException {

        final String payload = "Camel Rocks!!!";
        final ExchangePattern MEP = ExchangePattern.InOnly;

        final MockEndpoint befOneWay = super.getMockEndpoint("mock:beforeOneWay");
        final MockEndpoint oneWay = super.getMockEndpoint("mock:oneWay");
        final MockEndpoint aftOneWay = super.getMockEndpoint("mock:afterOneWay");

        befOneWay.setExpectedMessageCount(1);
        befOneWay.message(0).exchangePattern().isEqualTo(MEP);

        oneWay.setExpectedMessageCount(1);
        oneWay.message(0).exchangePattern().isEqualTo(ExchangePattern.InOnly);

        aftOneWay.setMinimumExpectedMessageCount(1);

        // Should be restored to calling Exchange Pattern
        aftOneWay.message(0).exchangePattern().isEqualTo(MEP);

        // Explicitly set Exchange Pattern
        super.template.sendBody("direct:start", MEP, payload);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().stream().findFirst().get();
        Exchange afterBeforeExchange = aftOneWay.getReceivedExchanges().stream().findFirst().get();

        assertNotEquals(oneWayExchange, afterBeforeExchange);

        assertEquals(oneWayExchange.getIn().getBody(), afterBeforeExchange.getIn().getBody());
        assertEquals(oneWayExchange.getUnitOfWork(), afterBeforeExchange.getUnitOfWork());
        //

    }

    @Test
    @Ignore
    public void testInOutMEPChangedForOneWay() throws InterruptedException {
        final String payload = "Camel Rocks!!!";
        final ExchangePattern MEP = ExchangePattern.InOut;

        final MockEndpoint befOneWay = super.getMockEndpoint("mock:beforeOneWay");
        final MockEndpoint oneWay = super.getMockEndpoint("mock:oneWay");
        final MockEndpoint aftOneWay = super.getMockEndpoint("mock:afterOneWay");

        befOneWay.setExpectedMessageCount(1);
        befOneWay.message(0).exchangePattern().isEqualTo(MEP);

        oneWay.setExpectedMessageCount(1);
        oneWay.message(0).exchangePattern().isEqualTo(ExchangePattern.InOnly);

        aftOneWay.setMinimumExpectedMessageCount(1);

        // Should be restored to calling Exchange Pattern
        aftOneWay.message(0).exchangePattern().isEqualTo(MEP);

        // Explicitly set Exchange Pattern
        super.template.sendBody("direct:start", MEP, payload);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().stream().findFirst().get();
        Exchange afterBeforeExchange = aftOneWay.getReceivedExchanges().stream().findFirst().get();

        assertNotEquals(oneWayExchange, afterBeforeExchange);

        assertEquals(oneWayExchange.getIn().getBody(), afterBeforeExchange.getIn().getBody());
        assertEquals(oneWayExchange.getUnitOfWork(), afterBeforeExchange.getUnitOfWork());
        //
    }

    @Test
    @Ignore
    public void testWhenAndOther() throws InterruptedException {

        super.getMockEndpoint("mock:camel").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:other").setExpectedMessageCount(1);

        //super.template.sendBody("direct:start", "Camel Rocks!!!");
        super.template.sendBody("direct:start", "Pissed Off");

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testDynamicRouter() throws InterruptedException {

        super.getMockEndpoint("mock:a").expectedMessageCount(1);
        super.getMockEndpoint("mock:b").expectedMessageCount(1);
        super.getMockEndpoint("mock:c").expectedMessageCount(1);
        super.getMockEndpoint("mock:other").expectedMessageCount(1);
        super.getMockEndpoint("mock:result").expectedMessageCount(1);


        super.template.sendBody("direct:start", "Camel Rocks!!!");

        assertMockEndpointsSatisfied();

    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testFirstFilter() throws InterruptedException {

        final MockEndpoint mockC = super.getMockEndpoint("mock:c");
        final MockEndpoint mockAfterC = super.getMockEndpoint("mock:afterC");
        final MockEndpoint mockOther = super.getMockEndpoint("mock:other");
        //
        mockC.expectedMessageCount(1);
        mockC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
        //
        mockAfterC.expectedMessageCount(1);
        mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        //

        super.getMockEndpoint("mock:amel").expectedMessageCount(0);

        //

        mockOther.expectedMessageCount(1);
        mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        super.template.sendBody("direct:start", "Cooks Rocks");


        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testSecondFilter() throws InterruptedException {

        final MockEndpoint mockAmel = super.getMockEndpoint("mock:amel");
        final MockEndpoint mockAfterC = super.getMockEndpoint("mock:afterC");
        final MockEndpoint mockOther = super.getMockEndpoint("mock:other");
        //
        super.getMockEndpoint("mock:c").expectedMessageCount(0);
//		mockC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
        //
        mockAfterC.expectedMessageCount(1);
        mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        //

        mockAmel.expectedMessageCount(1);
        mockAmel.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
        //

        mockOther.expectedMessageCount(1);
        mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        super.template.sendBody("direct:start", "amel is in Belgium");


        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testBothFilter() throws InterruptedException {

        final MockEndpoint mockC = super.getMockEndpoint("mock:c");
        final MockEndpoint mockAmel = super.getMockEndpoint("mock:amel");
        final MockEndpoint mockAfterC = super.getMockEndpoint("mock:afterC");
        final MockEndpoint mockOther = super.getMockEndpoint("mock:other");
        //
        mockC.expectedMessageCount(1);
        mockC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
        //
        mockAfterC.expectedMessageCount(1);
        mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        //

        mockAmel.expectedMessageCount(1);
        mockAmel.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
        //

        mockOther.expectedMessageCount(1);
        mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

        super.template.sendBody("direct:start", "Camel Rocks!");


        assertMockEndpointsSatisfied();
    }


    @Test
    @Ignore
    public void testOtherFilter() throws InterruptedException {


        final MockEndpoint mockAfterC = super.getMockEndpoint("mock:afterC");
        final MockEndpoint mockOther = super.getMockEndpoint("mock:other");
        //
        super.getMockEndpoint("mock:c").expectedMessageCount(0);
        //
        mockAfterC.expectedMessageCount(1);
        mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        //

        super.getMockEndpoint("mock:amel").expectedMessageCount(0);

        //

        mockOther.expectedMessageCount(1);
        mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

        super.template.sendBody("direct:start", "Hello World!");


        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testMessageLoadBalancedToStickyEndpoints() throws InterruptedException {

        final String payload = "Client has bought something";
        final MockEndpoint mockFirst = super.getMockEndpoint("mock:first");
        final MockEndpoint mockSecond = super.getMockEndpoint("mock:second");
        final MockEndpoint mockThird = super.getMockEndpoint("mock:third");

        mockFirst.expectedMessageCount(4);
        mockFirst.message(0).header("customerId").isEqualTo(0);
        mockFirst.message(1).header("customerId").isEqualTo(3);
        mockFirst.message(2).header("customerId").isEqualTo(0);
        mockFirst.message(3).header("customerId").isEqualTo(3);

        mockSecond.setExpectedMessageCount(2);
        mockSecond.message(0).header("customerId").isEqualTo(1);
        mockSecond.message(1).header("customerId").isEqualTo(1);

        mockThird.setExpectedMessageCount(2);
        mockThird.message(0).header("customerId").isEqualTo(2);
        mockThird.message(1).header("customerId").isEqualTo(2);
        // CorrelationId infer on the resutl
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 4; j++)
                super.template.sendBodyAndHeader("direct:start", payload, "customerId", j);

        assertMockEndpointsSatisfied();

    }


    /**
     * @throws InterruptedException
     */
    @Test
    public void testMessageLoadBalancedWithFailover() throws InterruptedException {

        final String payload = "Client has bought something";

        super.getMockEndpoint("mock:first").expectedMessageCount(1);
        super.getMockEndpoint("mock:third").expectedMessageCount(1);
        super.getMockEndpoint("mock:out").expectedMessageCount(2);

        // CorrelationId infer on the resutl
        template.sendBody("direct:start", payload);

        template.sendBody("direct:start", payload);


        assertMockEndpointsSatisfied();
    }

}
