package com.douglasdb.camel.feat.core.test.rest;

import static java.lang.System.out;

import java.util.Collection;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.common.MenuItemNotFoundException;
import com.douglasdb.camel.feat.core.common.MenuService;
import com.douglasdb.camel.feat.core.domain.menu.MenuItem;
import com.douglasdb.camel.feat.core.rest.CafeApiRoute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	private final int port1 = AvailablePortFinder.getNextAvailable();
	private final ObjectWriter objectWriter = new ObjectMapper().writer();

	/**
	 * 
	 */
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new CafeApiRoute(port1);
		// HelloWorldRoute(port1);
	}

	/**
	 * 
	 * @return
	 */
	private MenuService getMenuService() {
		return super.context.getRegistry()
				.lookupByNameAndType("menuService", MenuService.class);
	}

	@Override
	public JndiRegistry createRegistry() throws Exception {

		JndiRegistry registry = super.createRegistry();
		registry.bind("menuService", new MenuService());

		return registry;
	}

	/**
	 * 
	 */
	@Test
	@Ignore
	public void testHelloGet() {

		final String out = super.fluentTemplate()
				.to("undertow:http://localhost:" + port1 + "/say/hello")
				.withHeader(Exchange.HTTP_METHOD, "GET")
				.request(String.class);

		assertEquals("Hello World", out);

	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testPostBye() throws InterruptedException {

		final String json = "{ \"name\": \"Scott\" }";

		MockEndpoint update = super.getMockEndpoint("mock:update");

		update.expectedBodiesReceived(json);

		super.fluentTemplate().to("undertow:http://localhost:" + port1 + "/say/bye")
				.withHeader(Exchange.HTTP_METHOD, "POST").withHeader(Exchange.CONTENT_TYPE, "application/json")
				.withBody(json).send();

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testGetAll() throws Exception {

		final String origValue = objectWriter.writeValueAsString(this.getMenuService().getMenuItems());
		final String out = super.fluentTemplate().to("undertow:http://localhost:" + port1 + "/cafe/menu/items")
				.withHeader(Exchange.HTTP_METHOD, "GET").request(String.class);

		assertEquals(out, origValue);
	}

	@Test
	@Ignore
	public void testGetOne() throws Exception {

		final String origValue = objectWriter.writeValueAsString(getMenuService().getMenuItem(1));

		final String out = super.fluentTemplate()
				.to("undertow:http://localhost:" + port1 + "/cafe/menu/items/1")
				.withHeader(Exchange.HTTP_METHOD, "GET").request(String.class);

		// System.out.println(out);

		assertEquals(out, origValue);
	}

	@Test
	@Ignore
	public void testGetInvalid() {

		final int size = getMenuService().getMenuItems().size();

		try {
			// TODO: report camel-undertow not throwing exception on failure
			final String outJson = super.fluentTemplate()
					// throws a MenuItemNotFoundException handled by Camel
					.to("netty4-http:http://localhost:" + port1 + "/cafe/menu/items/" + (size + 1))
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
			
			out.println(outJson);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
		
		fail("Expected call to fail with exception thrown");

	}
	
	@Test
	@Ignore
	public void testCreate() {

		try {

			final Collection<MenuItem> menuItens = getMenuService().getMenuItems();

			assertEquals(2, menuItens.size());

			final MenuItem newItem = new MenuItem();
			
			newItem.setName("Test Item");
			newItem.setDescription("Test New Item Create");
			newItem.setCost(5);

			final String newItemJson = objectWriter.writeValueAsString(newItem);

			final Exchange outExchange = super.template
					.request("undertow:http://localhost:" + port1 + "/cafe/menu/items", (exchange) -> {
						exchange.getIn().setBody(newItemJson);
						exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
					});
			//
			assertEquals(201, outExchange.getOut().getHeader(Exchange.HTTP_RESPONSE_CODE));
			String out = outExchange.getOut().getBody(String.class);

			assertEquals("3", out);

			Collection<MenuItem> menuUpdateItems = getMenuService().getMenuItems();
			assertEquals(3, menuUpdateItems.size());
			

			MenuItem item3 = getMenuService().getMenuItem(3);
			assertEquals(3, item3.getId());
			assertEquals(newItem.getName(), item3.getName());
			assertEquals(newItem.getDescription(), item3.getDescription());
			assertEquals(newItem.getCost(), item3.getCost());
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MenuItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 */
	@Test
	@Ignore
	public void testUpdate() {

		try {
			
			final Collection<MenuItem> menuItens = getMenuService().getMenuItems();

			assertEquals(2, menuItens.size());

			MenuItem origMenuItem2 = getMenuService().getMenuItem(2);

			MenuItem newItem = new MenuItem();
			newItem.setId(origMenuItem2.getId());
			newItem.setName("Test " + origMenuItem2.getName());
			newItem.setDescription("Test " + origMenuItem2.getDescription());
			newItem.setCost(origMenuItem2.getCost() + 1);
			
			assertNotEquals(origMenuItem2, newItem);
			
	        String newItemJson = objectWriter.writeValueAsString(newItem);

			String out = fluentTemplate().to("undertow:http://localhost:" + port1 + "/cafe/menu/items/2")
					.withHeader(Exchange.HTTP_METHOD, "PUT")
					.withHeader(Exchange.CONTENT_ENCODING, "application/json")
					.withBody(newItemJson)
					.request(String.class);
			
			assertEquals(newItemJson, out);

			
			Collection<MenuItem> menuUpdateItems = getMenuService().getMenuItems();
			assertEquals(2, menuUpdateItems.size());

			MenuItem curItem2 = getMenuService().getMenuItem(2);
			assertEquals(2, curItem2.getId());
			assertEquals(newItem.getName(), curItem2.getName());
			assertEquals(newItem.getDescription(), curItem2.getDescription());
			assertEquals(newItem.getCost(), curItem2.getCost());
			
		} catch (MenuItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void testDelete() throws Exception {
		Collection<MenuItem> menuItems = getMenuService().getMenuItems();
		assertEquals(2, menuItems.size());

		fluentTemplate().to("undertow:http://localhost:" + port1 + "/cafe/menu/items/2")
				.withHeader(Exchange.HTTP_METHOD, "DELETE")
				.send();

		Collection<MenuItem> menuUpdateItems = getMenuService().getMenuItems();
		assertEquals(1, menuUpdateItems.size());
		assertEquals(menuItems.iterator().next(), menuUpdateItems.iterator().next());
	}
	
	
	@Test
	@Ignore
	public void testInvalidJson() {
		try {
			
			String outJson = super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/cafe/menu/items/1")
					.withHeader(Exchange.HTTP_METHOD, "PUT")
					.withHeader(Exchange.CONTENT_ENCODING, "application/json")
					.withBody("This is not JSON format")
					// ********************************************
					.request(String.class); // mandatory
					// ********************************************

			out.println(outJson);
			
			fail("Expected exception to be thrown");
			
		} catch (CamelExecutionException e) {
			
			HttpOperationFailedException httpException = 
					assertIsInstanceOf(HttpOperationFailedException.class,
							e.getCause());
			// ********************************************
			out.println(httpException.getResponseBody());
			// ********************************************
			assertEquals(400, httpException.getStatusCode());
			assertEquals("text/plain", httpException.getResponseHeaders().get(Exchange.CONTENT_TYPE));
			assertEquals("Invalid json data", httpException.getResponseBody());
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetInvalidAgain() throws Exception {
		
		final int size = getMenuService().getMenuItems().size();

		Exchange outExchange = super.template()
				.request("undertow:http://localhost:" + port1 + "/cafe/menu/items/" + (size + 1), (exchange) -> {
					exchange.getIn().setBody(null);
					exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
				});

		assertEquals(404, outExchange.getOut().getHeader(Exchange.HTTP_RESPONSE_CODE));
	}
	
}
