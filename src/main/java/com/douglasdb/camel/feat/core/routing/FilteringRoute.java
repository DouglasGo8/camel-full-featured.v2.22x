package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author Administrator
 *
 */
public class FilteringRoute extends RouteBuilder {

	public FilteringRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		from("direct:start")
			.filter()
				.simple("${body} regex '^C.*'")
				.to("mock:c")
			.end()
			.to("mock:afterC")
			.filter()
				.simple("${body} contains 'amel'")
				.to("mock:amel")
			.end()
			.to("mock:other");
		
		
	}

}
