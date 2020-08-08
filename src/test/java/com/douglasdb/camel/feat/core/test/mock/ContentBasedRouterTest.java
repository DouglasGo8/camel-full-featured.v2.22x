package com.douglasdb.camel.feat.core.test.mock;

import com.douglasdb.camel.feat.core.routing.cookbook.ContentBasedRouterRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ContentBasedRouterTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ContentBasedRouterRoute();
    }

    @Test
    public void testWhen() throws InterruptedException {

        final MockEndpoint mockCamel = super.getMockEndpoint("mock:camel");
        mockCamel.expectedMessageCount(2);
        mockCamel.message(0).body().isEqualTo("Camel Rocks");
        mockCamel.message(0).header("verified").isEqualTo(true);
        mockCamel.message(0).arrives().noLaterThan(50).millis().beforeNext();
        mockCamel.message(0).simple("${header[verified]} == true");
        //
        final MockEndpoint mockOther = super.getMockEndpoint("mock:other");
        mockOther.expectedMessageCount(0);
        // Two exchanges
        super.template.sendBody("direct:ch011Start", "Camel Rocks");
        super.template.sendBody("direct:ch011Start", "Loving the Camel");
        //
        mockCamel.assertIsSatisfied();
        mockOther.assertIsSatisfied();
        //
        final Exchange exchange0 = mockCamel.assertExchangeReceived(0);
        final Exchange exchange1 = mockCamel.assertExchangeReceived(1);
        //
        assertEquals(exchange0.getIn().getHeader("verified"), exchange1.getIn().getHeader("verified"));
    }

    @Test
    public void testOther() throws InterruptedException {

        super.getMockEndpoint("mock:camel").expectedMessageCount(0);
        super.getMockEndpoint("mock:other").expectedMessageCount(1);

        template.sendBody("direct:ch011Start", "Hello World");

        // asserts that all the mock objects involved in this test are happy
        assertMockEndpointsSatisfied();
    }
}
