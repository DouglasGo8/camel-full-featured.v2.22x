package com.douglasdb.camel.feat.core.converter;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderVelocityRoute extends RouteBuilder {

	public PurchaseOrderVelocityRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		from("direct:mail")
			.setHeader("Subject", constant("Thanks for ordering"))
			.setHeader("From", constant("donotreply@riders.com"))
			.to("velocity://META-INF/template/mail.vm")

			.log("${body}")
			.to("mock:mail");
	}

}
