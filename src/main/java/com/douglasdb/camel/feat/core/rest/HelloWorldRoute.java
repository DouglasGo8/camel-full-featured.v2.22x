package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.builder.RouteBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Administrator
 *
 */
@Data
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HelloWorldRoute extends RouteBuilder {

	private final int port1;

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		restConfiguration()
			.component("undertow")
			.host("localhost")
			.port(this.port1);
		
		
		rest("/say")
			
			.get("/hello")
				.route()
					.transform().constant("Hello World")
				.endRest()
				//.to("direct:hello")
				
			.post("/bye")
				.consumes("application/json")
				.to("mock:update");
		
		//from("direct:hello")
			//.transform().constant("Hello World");

	}

}
