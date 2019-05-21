package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class CallingInOutRoute extends RouteBuilder {

	
	public CallingInOutRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("direct:start")
			.to("mock:beforeModifiedMessage")
			//.inOut("direct:modifyMessage")
			.to(ExchangePattern.InOut, "direct:modifyMessage")
			.to("log:mainRoute?showAll=true")
			.to("mock:afterModifiedMessage")
		.end();
		
		
		from("direct:modifyMessage")
			.to("mock:modifyMessage")
			.to("log:subRoute?showAll=true")
			.transform(simple("[${body}] has been modified!"));
			
	}

}
