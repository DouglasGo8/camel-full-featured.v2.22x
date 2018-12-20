package com.douglasdb.camel.feat.core.converter;

import org.apache.camel.builder.RouteBuilder;

/**	
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderCsvConverterRoute extends RouteBuilder {

	
	public PurchaseOrderCsvConverterRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		
		super.getContext().setTracing(true);
		
		
		from("file://D:/.camel/data/inbox/full?noop=true&fileName=order.csv")
			.unmarshal()
				.csv()
				// List
				.split(body())
					.to("mock:csv");
		
	}
	
	

}
