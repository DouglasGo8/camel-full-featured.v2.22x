package com.douglasdb.camel.feat.core.test.routing;

import static java.lang.System.out;

import java.util.function.Function;
import java.util.stream.IntStream;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.routing.ThrottlerAsyncDelayedRoute;

/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {

    // @Produce(uri="direct:start")
    // private ProducerTemplate in;

    public EntryPoint() {
        // TODO: Auto-generated constructor stub
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub

        {
            // Only to ThrottlerAsyncDelayedRoute

            ThreadPoolProfileBuilder threadPoolProfileBuilder =
                    new ThreadPoolProfileBuilder("myThrottler");
            threadPoolProfileBuilder.maxQueueSize(5);
            super.context.getExecutorServiceManager()
                    .registerThreadPoolProfile(threadPoolProfileBuilder.build());


        }

        return new ThrottlerAsyncDelayedRoute();



        // ThrottlerDynamicRoute();
        // ThrottlerRoute();
        // RoutingSlipRoute();
        // MulticastWithAggregationRoute();
        // MulticastWithAggregationOfRequestRoute();
        // MulticastTimeoutRoute();
        // MulticastRoute();
        // MulticastStopOnExceptionRoute();
        // MulticastShallowCopyRoute();
        // MulticastParallelProcessingRoute();
        // MulticastExceptionHandlingInStrategyRoute();
        // LoadBalancerFailoverRoute();
        // LoadBalancerStickyRoute();
        // FilteringRoute();
        // DynamicRouterRoute();
        // ContentBasedRouterRoute();
        // CallingInOutRoute();
        // CallingInOnlyRoute();
        //
    }

    /**
     * @throws InterruptedException
     */
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

        // RequestBody have InOut Default ExchangePattern
        final String response = super.template.requestBody("direct:start", payload, String.class);

        assertEquals("Done", response);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().stream().findFirst().get();
        Exchange afterBeforeExchange = aftOneWay.getReceivedExchanges().stream().findFirst().get();

        assertNotEquals(oneWayExchange, afterBeforeExchange);

        assertEquals(oneWayExchange.getIn().getBody(), afterBeforeExchange.getIn().getBody());
        assertEquals(oneWayExchange.getUnitOfWork(), afterBeforeExchange.getUnitOfWork());
        //

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

        // SendBody have InOnly Default ExchangePattern
        super.template.sendBody("direct:start", MEP, payload);

        assertMockEndpointsSatisfied();

        Exchange oneWayExchange = oneWay.getReceivedExchanges().stream().findFirst().get();
        Exchange afterBeforeExchange = aftOneWay.getReceivedExchanges().stream().findFirst().get();

        assertNotEquals(oneWayExchange, afterBeforeExchange);

        assertEquals(oneWayExchange.getIn().getBody(), afterBeforeExchange.getIn().getBody());
        assertEquals(oneWayExchange.getUnitOfWork(), afterBeforeExchange.getUnitOfWork());
        //

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOnlyMEPChangedForModifyMessage() throws InterruptedException {

        final String payload = "Camel Rocks!!!";
        final ExchangePattern MEP = ExchangePattern.InOnly;

        final MockEndpoint beforeModifiedMessage = super.getMockEndpoint("mock:beforeModifiedMessage");
        final MockEndpoint modifyMessage = super.getMockEndpoint("mock:modifyMessage");
        final MockEndpoint afterModifiedMessage = super.getMockEndpoint("mock:afterModifiedMessage");

        beforeModifiedMessage.setExpectedMessageCount(1);
        beforeModifiedMessage.message(0).body().isEqualTo(payload);
        // Should match calling MEP
        beforeModifiedMessage.message(0).exchangePattern().isEqualTo(MEP);

        modifyMessage.setExpectedMessageCount(1);
        modifyMessage.message(0).body().isEqualTo(payload);
        // Should always be InOut
        modifyMessage.message(0).exchangePattern().isEqualTo(ExchangePattern.InOut);

        afterModifiedMessage.setExpectedMessageCount(1);
        afterModifiedMessage.message(0).body().isNotEqualTo(payload);
        afterModifiedMessage.message(0).exchangePattern().isEqualTo(MEP);

        super.template.sendBody("direct:start", MEP, payload);

        Exchange modifyMessageExchange = modifyMessage.getReceivedExchanges().stream().findFirst().get();
        Exchange afterModifiedExchange = afterModifiedMessage.getReceivedExchanges().stream().findFirst().get();

        assertEquals(modifyMessageExchange.getExchangeId(), afterModifiedExchange.getExchangeId());
        assertEquals(modifyMessageExchange.getUnitOfWork(), afterModifiedExchange.getUnitOfWork());

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOutMEPChangedForModifyMessage() throws InterruptedException {

        final String payload = "Camel Rocks!!!";
        final ExchangePattern MEP = ExchangePattern.InOut;

        final MockEndpoint beforeModifiedMessage = super.getMockEndpoint("mock:beforeModifiedMessage");
        final MockEndpoint modifyMessage = super.getMockEndpoint("mock:modifyMessage");
        final MockEndpoint afterModifiedMessage = super.getMockEndpoint("mock:afterModifiedMessage");

        beforeModifiedMessage.setExpectedMessageCount(1);
        beforeModifiedMessage.message(0).body().isEqualTo(payload);
        // Should match calling MEP
        beforeModifiedMessage.message(0).exchangePattern().isEqualTo(MEP);

        modifyMessage.setExpectedMessageCount(1);
        modifyMessage.message(0).body().isEqualTo(payload);
        // Should always be InOut
        modifyMessage.message(0).exchangePattern().isEqualTo(ExchangePattern.InOut);

        afterModifiedMessage.setExpectedMessageCount(1);
        afterModifiedMessage.message(0).body().isNotEqualTo(payload);
        afterModifiedMessage.message(0).exchangePattern().isEqualTo(MEP);

        super.template.sendBody("direct:start", MEP, payload);

        Exchange modifyMessageExchange = modifyMessage.getReceivedExchanges().stream().findFirst().get();
        Exchange afterModifiedExchange = afterModifiedMessage.getReceivedExchanges().stream().findFirst().get();

        assertEquals(modifyMessageExchange.getExchangeId(), afterModifiedExchange.getExchangeId());
        assertEquals(modifyMessageExchange.getUnitOfWork(), afterModifiedExchange.getUnitOfWork());

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testWhenAndOther() throws InterruptedException {

        super.getMockEndpoint("mock:camel").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:other").setExpectedMessageCount(1);

        // super.template.sendBody("direct:start", "Camel Rocks!!!");
        super.template.sendBody("direct:start", "Pissed Off");

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
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
        // mockC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
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

    /**
     * @throws InterruptedException
     */
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

        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 4; j++)
                super.template.sendBodyAndHeader("direct:start", payload, "customerId", j);

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testMessageLoadBalancedWithFailover() throws InterruptedException {

        final String payload = "Client bought something";

        super.getMockEndpoint("mock:first").expectedMessageCount(1);
        super.getMockEndpoint("mock:third").expectedMessageCount(1);
        super.getMockEndpoint("mock:out").expectedMessageCount(2);

        // CorrelationId infer on the result
        template.sendBody("direct:start", payload);

        template.sendBody("direct:start", payload);

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testMessageRoutedToMulticastEndpoints() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_afterMulticast = super.getMockEndpoint("mock:afterMulticast");
        MockEndpoint mock_exceptionHandler = super.getMockEndpoint("mock:exceptionHandler");

        mock_first.message(0).body().isEqualTo(payload);

        mock_afterMulticast.setExpectedMessageCount(1);
        mock_afterMulticast.message(0).predicate().simple("${header.multicast_exception} != null");

        mock_exceptionHandler.setExpectedMessageCount(1);

        String response = super.template.requestBody("direct:start", payload, String.class);

        assertEquals("Oops,ALL Ok Here", response);

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testAllMessagesParticipateInDifferentTransactions() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_afterMulticast = super.getMockEndpoint("mock:afterMulticast");
        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");

        mock_afterMulticast.setExpectedMessageCount(1);
        mock_first.setExpectedMessageCount(1);
        mock_second.setExpectedMessageCount(1);

        template.sendBody("direct:start", payload);

        assertMockEndpointsSatisfied();

        assertNotEquals(myExchange.apply(mock_afterMulticast).getUnitOfWork(),
                myExchange.apply(mock_first).getUnitOfWork());

        assertNotEquals(myExchange.apply(mock_afterMulticast).getUnitOfWork(),
                myExchange.apply(mock_second).getUnitOfWork());

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testAllEndpointsReachedByDifferentThreads() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_afterMulticast = super.getMockEndpoint("mock:afterMulticast");
        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");

        mock_afterMulticast.setExpectedMessageCount(1);
        mock_first.setExpectedMessageCount(1);
        mock_second.setExpectedMessageCount(1);

        String response = template.requestBody("direct:start", payload, String.class);

        assertEquals("response", response);

        assertMockEndpointsSatisfied();

        final String mainThreadName = myExchange.apply(mock_afterMulticast).getIn().getHeader("threadName",
                String.class);

        final String firstThreadName = myExchange.apply(mock_first).getIn().getHeader("threadName", String.class);

        final String secondThreadName = myExchange.apply(mock_second).getIn().getHeader("threadName", String.class);

        assertNotEquals(firstThreadName, mainThreadName);
        assertNotEquals(firstThreadName, secondThreadName);
        assertNotEquals(mainThreadName, secondThreadName);

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testMessageRoutedToMulticastEndpointsInShallowCopy() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");
        MockEndpoint mock_after_multicast = super.getMockEndpoint("mock:afterMulticast");

        mock_first.setExpectedMessageCount(1);
        mock_first.message(0).body().isEqualTo(payload);
        mock_first.message(0).header("firstModifies").isEqualTo("apple");

        mock_second.setExpectedMessageCount(1);
        mock_second.message(0).body().isEqualTo(payload);
        mock_second.message(0).header("secondModifies").isEqualTo("banana");
        mock_second.message(0).header("firstModifies").isNull();

        mock_after_multicast.setExpectedMessageCount(1);
        mock_after_multicast.message(0).body().isEqualTo(payload);
        mock_after_multicast.message(0).header("modifiedBy").isNull();

        super.template.sendBody("direct:start", payload);

        // check that all of the messages participated in different transactions
        assertNotEquals(myExchange.apply(mock_after_multicast).getUnitOfWork(),
                myExchange.apply(mock_first).getUnitOfWork());
        assertNotEquals(myExchange.apply(mock_after_multicast).getUnitOfWork(),
                myExchange.apply(mock_second).getUnitOfWork());

        // check that all of the mock endpoints were reached by the same thread

        String fThreadName = myExchange.apply(mock_first).getIn().getHeader("threadName", String.class);
        String sThreadName = myExchange.apply(mock_second).getIn().getHeader("threadName", String.class);

        assertEquals(fThreadName, sThreadName);

        assertMockEndpointsSatisfied();

    }

    /**
     *
     */
    @Test
    @Ignore
    public void testMessageRoutedToMulticastEndpointsStopOnException() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");
        MockEndpoint mock_after_multicast = super.getMockEndpoint("mock:afterMulticast");
        // MockEndpoint mock_exception_handler =
        // super.getMockEndpoint("mock:exceptionHandler");

        mock_first.setExpectedMessageCount(1);
        mock_first.message(0).body().isEqualTo(payload);

        mock_second.setExpectedMessageCount(0);

        mock_after_multicast.setExpectedMessageCount(0);

        String response = super.template.requestBody("direct:start", payload, String.class);

        assertEquals("Oops", response);

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testMessageRoutedToMulticastEndpoints2() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");
        MockEndpoint mock_third = super.getMockEndpoint("mock:third");

        mock_first.setExpectedMessageCount(1);
        mock_first.message(0).body().isEqualTo(payload);

        mock_second.setExpectedMessageCount(1);
        mock_second.message(0).body().isEqualTo(payload);

        mock_third.setExpectedMessageCount(1);
        mock_third.message(0).body().isEqualTo(payload);

        super.template.sendBody("direct:start", payload);

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testTimedOutMessageNotDeliveredToEndpoints() throws InterruptedException {

        final String payload = "Message to be multicast";

        MockEndpoint mock_afterMulticast = super.getMockEndpoint("mock:afterMulticast");
        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        super.getMockEndpoint("mock:second").setExpectedMessageCount(1);

        mock_first.setExpectedMessageCount(1);

        mock_first.message(0).body().isNotEqualTo("response");

        mock_afterMulticast.message(0).body().isNotEqualTo("response");

        super.template.sendBody("direct:start", payload);

        assertMockEndpointsSatisfied();

    }

    /**
     *
     */
    @Test
    @Ignore
    public void testAggregationOfResponsesFromMulticast() {
        final String payload = "Message to be multicast";

        String response = super.template.requestBody("direct:start", payload, String.class);

        // enrich
        // assertEquals("Message to be multicast,first response,second response",
        // response);

        // non enrich
        assertEquals("first response,second response", response);
    }

    @Test
    @Ignore
    public void testRoutingSlip() throws InterruptedException {

        super.getMockEndpoint("mock:a").expectedMessageCount(1);
        super.getMockEndpoint("mock:other").expectedMessageCount(1);

        super.template.sendBodyAndHeader("direct:start",
                "Camel body",
                "myRoutingSlipHeader",
                "mock:a,direct:other");

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testThrottle() throws InterruptedException {
        final int throttleRate = 5;
        final int messageCount = throttleRate + 2;

        super.getMockEndpoint("mock:unthrottled").expectedMessageCount(messageCount);
        //
        super.getMockEndpoint("mock:throttled").expectedMessageCount(throttleRate);
        super.getMockEndpoint("mock:after").expectedMessageCount(throttleRate);

        for (int i = 0; i < messageCount; i++)
            super.template.asyncSendBody("direct:start", "Camel Rocks!!!");

        super.assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testThrottleDynamic() throws InterruptedException {

        final int throttleRate = 3;
        final int messageCount = throttleRate + 2;

        super.getMockEndpoint("mock:unthrottled").expectedMessageCount(messageCount);
        super.getMockEndpoint("mock:throttled").expectedMessageCount(throttleRate);
        super.getMockEndpoint("mock:after").expectedMessageCount(throttleRate);


        IntStream.range(0, messageCount)
                .forEach(i -> {
                    Exchange exchange = super
                            .getMandatoryEndpoint("direct:start")
                            .createExchange();
                    {
                        Message in = exchange.getIn();
                        in.setHeader("throttleHeaderRate", throttleRate);
                        in.setBody("Camel Rocks!!!");
                    }
                    super.template.asyncSend("direct:start", exchange);
                });
        assertMockEndpointsSatisfied();

    }

    @Test
    public void testAsyncDelayedThrottle() throws InterruptedException {

        final int throttleRate = 5;
        final int messageCount = throttleRate + 2;

        MockEndpoint mockThrottled = super.getMockEndpoint("mock:throttled");
        mockThrottled.expectedMessageCount(messageCount);

        IntStream.range(0, messageCount)
                .forEach(i -> {
                    super.template.asyncSendBody("direct:start", "Camel Rocks!!!");
                });


        assertMockEndpointsSatisfied();

        mockThrottled.getExchanges().stream()
                .forEach(exchange -> {
                    out.println(exchange.getIn()
                            .getHeader("threadName", String.class));
                });
    }

    /**
     *
     */
    private final Function<MockEndpoint, Exchange> myExchange = (mock) -> mock.getExchanges().get(0);

}
