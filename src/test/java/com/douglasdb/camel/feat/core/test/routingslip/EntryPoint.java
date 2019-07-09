
package com.douglasdb.camel.feat.core.test.routingslip;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.routingsplip.RoutingSlipSimpleRouter;

/**
 * 
 */
public class EntryPoint extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {

		return new RoutingSlipSimpleRouter();
				// RoutingSlipHeaderRouter();
				// RoutingSlipRouter();
	}

	@Test
	@Ignore
	public void testRoutingSlip() throws InterruptedException {

		super.getMockEndpoint("mock:a").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:b").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:c").setExpectedMessageCount(1);

		super.template.sendBody("direct:start", "MIL3Y CRYRUST");

		assertMockEndpointsSatisfied();

	}

	@Test
	@Ignore
	public void testRoutingSlipCool() throws InterruptedException {

		super.getMockEndpoint("mock:a").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:b").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:c").setExpectedMessageCount(1);

		super.template.sendBody("direct:start", "We're Cool");

		assertMockEndpointsSatisfied();

	}
	
	
	@Test
	@Ignore
	public void testRoutingSimpleSlip() throws InterruptedException {

		super.getMockEndpoint("mock:a").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:b").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:c").setExpectedMessageCount(1);

		super.template.sendBodyAndHeader("direct:start", "We're Cool", "mySlip", "mock:a,mock:c");

		assertMockEndpointsSatisfied();

	}
	

}