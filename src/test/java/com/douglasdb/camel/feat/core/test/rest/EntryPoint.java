package com.douglasdb.camel.feat.core.test.rest;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.common.MenuService;
import com.douglasdb.camel.feat.core.rest.CafeApiRoute;
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

	private MenuService getMenuService() {
		return super.context.getRegistry().lookupByNameAndType("menuService", MenuService.class);
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
			final String out = super.fluentTemplate()
					// throws a MenuItemNotFoundException handled by Camel
					.to("netty4-http:http://localhost:" + port1 + "/cafe/menu/items/" + (size + 1))
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
		} catch (Exception e) {
			//e.printStackTrace();
			return;
		}
		
		fail("Expected call to fail with exception thrown");

	}
	
	@Test
	public void testCreate() {
		
	}

}
