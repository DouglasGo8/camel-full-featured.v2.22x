package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;

import com.douglasdb.camel.feat.core.structuring.route.processor.OrderProcessor;


/**
 * 
 * @author Administrator
 *
 */
public class RouteStartupRoute extends RouteBuilder {

	public RouteStartupRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:receiveOrders")
			.startupOrder(20)
			.to("seda:processOrders");
		
		from("seda:processOrders")
			.startupOrder(10)
			.process(new OrderProcessor());
		
			
		
	}

}
