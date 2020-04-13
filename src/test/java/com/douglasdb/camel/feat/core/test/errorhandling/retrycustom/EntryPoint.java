package com.douglasdb.camel.feat.core.test.errorhandling.retrycustom;

import com.douglasdb.camel.feat.core.errorhandling.retrycustom.RetryCustomRoute;
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
        return new RetryCustomRoute();
    }

    @Test
    public void testRetry() throws Exception {
        final MockEndpoint mockEndpoint = super.getMockEndpoint("mock:result");
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
        mockEndpoint.allMessages().header(Exchange.REDELIVERY_COUNTER).isEqualTo(1);

        super.template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }
}
