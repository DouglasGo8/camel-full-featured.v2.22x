package com.douglasdb.camel.feat.core.converter;

import com.douglasdb.camel.feat.core.domain.purchase.PurchaseOrderCsv;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;


/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderUnmarshalBindyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:toObject")
		//.log("${body}")
		.unmarshal()
			.bindy(BindyType.Csv, PurchaseOrderCsv.class)
			.log("${body}")
		.to("mock:result");
	}

}
