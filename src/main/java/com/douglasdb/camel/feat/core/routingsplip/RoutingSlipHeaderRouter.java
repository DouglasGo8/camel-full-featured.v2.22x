package com.douglasdb.camel.feat.core.routingsplip;

import org.apache.camel.builder.RouteBuilder;

import lombok.NoArgsConstructor;

/**
 * 
 * @author Administrator
 *
 */
@NoArgsConstructor
public class RoutingSlipHeaderRouter extends RouteBuilder {

		
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		
		from("direct:start")
			.log("${body}")
			// deprecated
			//.setHeader("mySlip")
			//	.method(ComputeSlip.class)
			//.routingSlip("mySlip");
			.routingSlip(method(ComputeSlip.class));
	}

}
