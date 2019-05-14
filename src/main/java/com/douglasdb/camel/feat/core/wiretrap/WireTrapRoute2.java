package com.douglasdb.camel.feat.core.wiretrap;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class WireTrapRoute2 extends RouteBuilder {

	public WireTrapRoute2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			// copy
			.wireTap("mock:tapped")
			.to("mock:out");
	}

}
