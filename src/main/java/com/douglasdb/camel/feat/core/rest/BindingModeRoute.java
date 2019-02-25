package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import com.douglasdb.camel.feat.core.domain.Item;


/**
 * 
 * @author Administrator
 *
 */
public class BindingModeRoute extends RouteBuilder {

	
	private int port1;
	
	
	public BindingModeRoute() {
		// TODO Auto-generated constructor stub
	}
	
	public BindingModeRoute(int port1) {
		// TODO Auto-generated constructor stub
		this.port1 = port1;
	}
	
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		 
		restConfiguration()
        	.component("undertow")
         	.port(port1)
         	.bindingMode(RestBindingMode.json_xml);
		
		rest()
			.get("/items")
				.outType(Item[].class)
				.to("bean:itemService?method=getItems")
			.get("items/{id}")
				.outType(Item.class)
				.to("bean:itemService?method=getItem(${header.id})")
			.get("/items/{id}/xml")
				.outType(Item.class)
					.bindingMode(RestBindingMode.xml)
				.to("bean:itemService?method=getItem(${header.id})")
			.get("/items/{id}/json")
				.outType(Item.class)
					.bindingMode(RestBindingMode.json)
				.to("bean:itemService?method=getItem(${header.id})")
		     .put("/items/{id}")
		     	.type(Item.class)
		     	.to("bean:itemService?method=setItem(${header.id}, ${body})");
	}

}
