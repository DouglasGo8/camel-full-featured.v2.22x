package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.builder.RouteBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * 
 * @author Administrator
 *
 */
@Data
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HelloWorldRoute extends RouteBuilder {

	private int port1;

	public HelloWorldRoute() {
		// TODO Auto-generated constructor stub
	}

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
