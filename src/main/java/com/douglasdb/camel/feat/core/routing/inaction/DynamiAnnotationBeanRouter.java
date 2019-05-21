package com.douglasdb.camel.feat.core.routing.inaction;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class DynamiAnnotationBeanRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			.dynamicRouter(method(DynamicRouterBean.class, "router"))
			.to("mock:result");
	}

}
