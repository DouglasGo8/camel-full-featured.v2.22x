package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class ContentBasedRouterRoute extends RouteBuilder {

	
	public ContentBasedRouterRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			.choice()
				.when()
					.simple("${body} contains 'Camel'")
					.to("mock:camel")
					.log("Camel ${body}")
				.otherwise()
					.to("mock:other")
					.log("Other ${body}")
			.end()
			.log("Message ${body}");


		from("direct:ch011Start")
				.choice()
					.when(simple("${body} contains 'Camel'"))
						.setHeader("verified").constant(true)
						.to("mock:camel")
					.otherwise()
						.to("mock:other")
				.end();
	}

}
