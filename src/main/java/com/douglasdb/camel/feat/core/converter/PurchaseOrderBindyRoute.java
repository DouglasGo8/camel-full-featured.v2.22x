package com.douglasdb.camel.feat.core.converter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;

import com.douglasdb.camel.feat.core.domain.PurchaseOrderCsv;




/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderBindyRoute extends RouteBuilder {

	
	public PurchaseOrderBindyRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("direct:toCsv")
			//.log("${body}")
			.marshal()
				.bindy(BindyType.Csv, PurchaseOrderCsv.class)
				
				.log("${body}")
			.to("mock:result");
	}

	
	
}
