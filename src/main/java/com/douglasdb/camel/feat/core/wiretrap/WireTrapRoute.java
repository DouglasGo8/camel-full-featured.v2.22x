package com.douglasdb.camel.feat.core.wiretrap;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class WireTrapRoute extends RouteBuilder {

	public WireTrapRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("file:///Users/douglasdias/camel/input/one?noop=true")
			.log("Received file: ${header.CamelFileName}")
			.to("acmq:queue:incomingOrders");
	
		from("acmq:queue:incomingOrders")
			.wireTap("acmq:queue:orderAudit")
			.choice()
				.when(header("CamelFileName").endsWith(".xml"))
					.to("acmq:queue:xmlOrders")
				.when(header("CamelFileName").regex("^.*(csv|csl)$"))
					.to("acmq:queue:csvOrders")
				.otherwise()
					.to("acmq:queue:badOrders");
		
		from("acmq:queue:xmlOrders")
			//.multicast()
			//.stopOnException()
			.log("Received XML order: ${header.CamelFileName}")
			.to("mock:xml");
		
		from("acmq:queue:csvOrders")
		//.multicast()
		//.stopOnException()
			.log("Received XML order: ${header.CamelFileName}")
			.to("mock:csvl");
		
		from("acmq:queue:orderAudit")
		//.multicast()
		//.stopOnException()
			.log("Received XML order: ${header.CamelFileName}")
			.to("mock:wiretap");
		
		
	}

}
