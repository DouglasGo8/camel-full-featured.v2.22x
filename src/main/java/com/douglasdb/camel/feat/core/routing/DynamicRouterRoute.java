package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class DynamicRouterRoute extends RouteBuilder {

	public DynamicRouterRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			.dynamicRouter(method(MyDynamicRouter.class, "routeMe"));
		
		from("direct:other")
			.to("mock:other");
	}

}
