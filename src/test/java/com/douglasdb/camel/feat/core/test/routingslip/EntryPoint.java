

package com.douglasdb.camel.feat.core.test.routingslip;



import com.douglasdb.camel.feat.core.routingsplip.RoutingSlipRouter;

import org.apache.camel.RoutesBuilder;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * 
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        

        return new RoutingSlipRouter();
    }


    @Test
    public void testRoutingSlip() throws InterruptedException {

        super.getMockEndpoint("mock:a").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:b").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:c").setExpectedMessageCount(1);

        super.template.sendBody("direct:start", "MIL3Y CRYRUST");


        assertMockEndpointsSatisfied();

    }
}