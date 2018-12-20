package com.douglasdb.camel.feat.core.ftp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;


/**
 * 
 * @author douglasdias
 *
 */
public class FtpToJmsRoute extends RouteBuilder {

	
	private final Logger log = Logger.getLogger(FtpToJmsRoute.class);
	
	public FtpToJmsRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("ftp://192.168.1.39:2121?username=guest&password=welcome1&noop=true")
			// .log("${header.CamelFileName}")
			.process((exchange) -> {
				log.info("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
			})
			
			// without placehoder
			
			//.setHeader("myDest", constant("incomingOrders"))
			//.toD("acmq:queue:${header.myDest}");
			
			// with placeholder
			.to("acmq:queue:{{myDest}}");

		from("acmq:queue:incomingOrders")
			.to("mock:out")
		.end();
	}

}
