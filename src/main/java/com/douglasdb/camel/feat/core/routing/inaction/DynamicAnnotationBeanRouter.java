package com.douglasdb.camel.feat.core.routing.inaction;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author Administrator
 *
 */
public class DynamicAnnotationBeanRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("direct:start")
				// invoke the bean as if it was just a regular bean
				// you must not use the .dynamicRouter here because we will
				// annotatte the bean instead
			.bean(DynamicAnnotationBean.class, "route")
			.to("mock:result");
	}

}
