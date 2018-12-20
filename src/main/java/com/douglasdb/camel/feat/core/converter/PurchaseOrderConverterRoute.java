package com.douglasdb.camel.feat.core.converter;

import org.apache.camel.builder.RouteBuilder;

import com.douglasdb.camel.feat.core.domain.PurchaseOrderDefault;

/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderConverterRoute extends RouteBuilder {

	
	public PurchaseOrderConverterRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		from("direct:start")
			.convertBodyTo(PurchaseOrderDefault.class);
		
	}
	
	

}
