package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class CallingInOnlyRoute extends RouteBuilder {
	
	
	public CallingInOnlyRoute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("direct:start")
			.to("mock:beforeOneWay")
			.to(ExchangePattern.InOnly, "direct:oneWay")
//			.inOnly("direct:oneWay")
			.to("mock:afterOneWay")
			.transform(constant("Done"))
		.end();
		
		
		from("direct:oneWay")
			.to("mock:oneWay");
		
	}

}
