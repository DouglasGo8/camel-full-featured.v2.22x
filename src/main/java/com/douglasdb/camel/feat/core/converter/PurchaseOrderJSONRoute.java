package com.douglasdb.camel.feat.core.converter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;


/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderJSONRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		
		from("jetty://http://0.0.0.0:10222/order/service")
			.bean("purchaseOrderService", "lookup")
			
			.marshal()
				.json(JsonLibrary.Jackson);
	}
	

}
