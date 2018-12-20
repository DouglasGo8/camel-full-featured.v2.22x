package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class DirectRoute extends RouteBuilder {

	public DirectRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:in")
			.log("${body}")
			.transform(simple("A1[ ${body} ]"))
			.log("${body}")
			.to("direct:b")
			.transform(simple("A2[ ${body} ]"))
			.log("${body}")
			.to("mock:a")
		.end();
		
		
		from("direct:b")
			.transform(simple("B[ ${body} ]"))
			.log("${body}")
			.to("mock:b")
		.end();
	}

}
