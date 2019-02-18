package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import com.douglasdb.camel.feat.core.common.MenuItemNotFoundException;
import com.douglasdb.camel.feat.core.domain.MenuItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class CafeApiRoute extends RouteBuilder {

	private final int port;

	/**
	 * 
	 */
	@Override
	public void configure() {

		
		onException(MenuItemNotFoundException.class)
			.handled(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
			.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
				.log("${exception.message}")
			.setBody()
				.simple("${exception.message}");
		
		
		restConfiguration()
			.component("undertow")
			.port(this.port)
			.bindingMode(RestBindingMode.json)
				.apiContextPath("api-doc")
				.apiProperty("api.title", "Cafe Menu Micro Camel Service")
				.apiProperty("api.version", "1.0.0")
				.apiProperty("api.description", "Cafe Menu Sample API")
				.apiProperty("api.contact.name", "Camel Cookbook")
				.apiProperty("api.contact.url", "http://www.camelcookbook.org")
				.apiProperty("api.license.name", "Apache 2.0")
				.apiProperty("api.license.url", "http://www.apache.org/licenses/LICENSE-2.0.html")
			// *****************
			.enableCORS(true);
			// *****************
		
		rest("/cafe/menu")
			.description("Cafe Menu Services")
			.get("/items")
				.description("Returns all menu items")
					.outType(MenuItem[].class) // Flux
					.responseMessage()
						.code(200)
						.message("All of the menu items")
					.endResponseMessage()
				.to("bean:menuService?method=getMenuItems")
			.get("/items/{id}")
				.description("Returns menu item with matching id")
				.outType(MenuItem.class) //
				.responseMessage()
					.code(200)
					.message("The requested menu item")
				.endResponseMessage()
				.responseMessage()
					.code(404)
					.message("Menu item not found")
					.endResponseMessage()
				.to("bean:menuService?method=getMenuItem(${header.id})")
			.post("/items/id")
				.description("Returns all menu items")
				.outType(MenuItem[].class)
				.to("bean:menuService?method=getMenuItems");
		

	}

}