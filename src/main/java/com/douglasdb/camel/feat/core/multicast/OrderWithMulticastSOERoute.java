package com.douglasdb.camel.feat.core.multicast;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class OrderWithMulticastSOERoute extends RouteBuilder {

	
	public OrderWithMulticastSOERoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("file:///Users/douglasdias/camel/input/full?noop=true")
			.log("Received file: ${header.CamelFileName}")
			.to("acmq:queue:incomingOrders");
		
		from("acmq:queue:incomingOrders")
			.choice()
				.when(header("CamelFileName").endsWith(".xml"))
					.to("acmq:queue:xmlOrders")
				.when(header("CamelFileName").regex("^.*(csv|csl)$"))
					.to("acmq:queue:csvOrders")
				.otherwise()
					.to("acmq:queue:badOrders");
		
		from("acmq:queue:xmlOrders")
			.multicast()
			.stopOnException()
			.to("acmq:queue:accounting", "acmq:queue:production");
		
		from("acmq:queue:accounting")
			.to("mock:accounting_before_exception")
			.throwException(Exception.class, "I miserably failed!!")
			.log("Accounting received order: ${header.CamelFileName}")
			.to("mock:accounting");
		
		from("acmq:queue:production")
			.log("Production received order: ${header.CamelFileName}")
			.to("mock:production");
	}

}
