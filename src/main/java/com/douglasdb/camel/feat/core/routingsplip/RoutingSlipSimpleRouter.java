package com.douglasdb.camel.feat.core.routingsplip;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class RoutingSlipSimpleRouter extends RouteBuilder {

	
	@Override
	@SuppressWarnings("deprecation")
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("direct:start")
			.routingSlip("mySlip");
		
	}
}
