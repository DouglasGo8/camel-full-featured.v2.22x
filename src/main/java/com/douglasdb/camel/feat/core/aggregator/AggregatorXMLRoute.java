package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author DouglasDb
 *
 */
public class AggregatorXMLRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			.log("${body}")
			.aggregate(xpath("/order/@customer"), new MyAggregationStrategy())
				.completionSize(2) // NoK nut using Or
				.completionTimeout(5000) // Oak
				.log("Completed by ${property.CamelAggregatedCompletedBy}")
				.log("Sending out ${body}")
			.to("mock:result")
		.end();
	}

}
