package com.douglasdb.camel.feat.core.cbr;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class OrderWithOtherwiseComplexRoute extends RouteBuilder {

	
	public OrderWithOtherwiseComplexRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("file:///Users/douglasdias/camel/input/full?noop=true")
			.log("Received file: ${header.CamelFileName}")
			.to("acmq:queue:incomingOrders");
		
		from("acmq:queue:incomingOrders")
			.log("Received order from file..: ${header.CamelFileName}")
		.choice()
			.when(header("CamelFileName").endsWith(".xml"))
				.to("acmq:queue:xmlOrders")
			.when(header("CamelFileName").regex("^.*(csv|csl)$"))
				.to("acmq:queue:csvOrders")
			.otherwise()
				.to("acmq:queue:badOrders");
		
		
		from("acmq:queue:xmlOrders")
			.log("Received a XML order from file..: ${header.CamelFileName}")
			.to("mock:xml");
		
		from("acmq:queue:csvOrders")
			.log("Received a CSV and CSV order from file..: ${header.CamelFileName}")
			.to("mock:csv");
		
		from("acmq:queue:badOrders")
			.log("Received a BaD order from file..: ${header.CamelFileName}")
			.to("mock:bad");
	
	}

}
