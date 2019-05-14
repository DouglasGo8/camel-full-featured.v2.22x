package com.douglasdb.camel.feat.core.filter;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class OrderWithFilterRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("file:///Users/douglasdias/camel/input/minimal?noop=true")
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
			//.log("Ok got message ${body}")
			.filter(xpath("//order[not(@test)]"))
				.log("Received XML order: ${header.CamelFileName}")
				.to("mock:xml")
		.end();
		
	}

}
