package com.douglasdb.camel.feat.core.transform.bean;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class OrderToCsvBeanRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("direct:start")
			.bean(new OrderToCsvBean())
			.to("file:///D:/.camel/data/inbox/minimal?fileName=report-${header.Date}.csv");
	}

}
