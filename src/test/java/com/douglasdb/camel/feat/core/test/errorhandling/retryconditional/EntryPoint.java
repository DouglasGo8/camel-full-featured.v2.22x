package com.douglasdb.camel.feat.core.test.errorhandling.retryconditional;

import com.douglasdb.camel.feat.core.errorhandling.retryconditional.RetryConditionalRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RetryConditionalRoute();
    }

    @Test
    public void testRetry() throws Exception {
        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
        mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER).isEqualTo(1);

        template.sendBody("direct:start", "Foo");

        assertMockEndpointsSatisfied();
    }
}
