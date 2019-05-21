package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class LoadBalancerStickyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:start")
			.loadBalance()
				.sticky(header("customerId"))
				.log("${body}")
			.to("mock:first")
	        .to("mock:second")
	        .to("mock:third")
	    .end()
	    .to("mock:out");
	}

}
