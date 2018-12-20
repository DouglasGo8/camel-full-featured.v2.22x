package com.douglasdb.camel.feat.core.cbr;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class OrderWithOtherwiseRoute extends RouteBuilder {

	
	public OrderWithOtherwiseRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("file:///Users/douglasdias/camel/input/minimal?noop=true")
			.log("Received file: ${header.CamelFileName}")
			.to("acmq:queue:incomingOrders");
		
		from("acmq:queue:incomingOrders")
			.log("Received order from file..: ${header.CamelFileName}")
		.choice()
			.when(simple("${header.CamelFileName} ends with 'xml'"))
				.to("acmq:queue:xmlOrders")
			.when(simple("${header.CamelFileName} ends with 'csv'"))
				.to("acmq:queue:csvOrders");
		
		
		from("acmq:queue:xmlOrders")
			.log("Received XML order from file..: ${header.CamelFileName}")
			.to("mock:xml");
		
		from("acmq:queue:csvOrders")
			.log("Received XML order from file..: ${header.CamelFileName}")
			.to("mock:csv");
		
	
	}

}
