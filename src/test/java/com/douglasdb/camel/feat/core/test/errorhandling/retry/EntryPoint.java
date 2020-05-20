package com.douglasdb.camel.feat.core.test.errorhandling.retry;

import com.douglasdb.camel.feat.core.errorhandling.retry.RetryRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RetryRoute();
    }

    @Test
    public void testRetry() throws InterruptedException {

        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");

        mockResult.setExpectedMessageCount(1);

        mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
        mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER).isEqualTo(1);
        mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER).isEqualTo(2);
        mockResult.allMessages().header(Exchange.REDELIVERY_DELAY).isNull();

        super.template.sendBody("direct:start", "Foo");
        assertMockEndpointsSatisfied();
        ;
    }

    @Test
    public void testRetryRouteSpecific() throws Exception {

        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
        mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER).isEqualTo(1);
        mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER).isEqualTo(2);
        mockResult.allMessages().header(Exchange.REDELIVERY_DELAY).isNull();

        super.template.sendBody("direct:routeSpecific", "Foo");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRetryRouteSpecificDelay() throws Exception {
        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
        mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER).isEqualTo(1);
        mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER).isEqualTo(2);
        mockResult.allMessages().exchangeProperty("SporadicDelay").isGreaterThanOrEqualTo(500);

        super.template.sendBody("direct:routeSpecificDelay", "Foo");

        log.info("delay = {}", mockResult.getReceivedExchanges().get(0).getProperty("SporadicDelay", long.class));

        assertMockEndpointsSatisfied();
    }

}
