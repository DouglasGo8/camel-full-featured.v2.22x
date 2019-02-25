package com.douglasdb.camel.feat.core.test.rest;

import static java.lang.System.out;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.common.ItemService;
import com.douglasdb.camel.feat.core.domain.Item;
import com.douglasdb.camel.feat.core.rest.BindingModeRoute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


/**
 * 
 * @author Administrator
 *
 */
public class EntryPointBindingMode extends CamelTestSupport {

	private final int port1 = AvailablePortFinder.getNextAvailable();

	private ObjectWriter objectWriter = new ObjectMapper().writer();

	private ItemService getItemService() {
		return super.context.getRegistry()
						.lookupByNameAndType("itemService", ItemService.class);

	}

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		// TODO Auto-generated method stub
		final JndiRegistry registry = super.createRegistry();
		registry.bind("itemService", new ItemService());
		return registry;
	}	
	
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new BindingModeRoute(port1);
	}
	

	public EntryPointBindingMode() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Test
	@Ignore
	public void testGetMany() {
		try {
			
			final Item[] origItems = getItemService().getItems();

			final String origItemJson = objectWriter.writeValueAsString(origItems);
			
			out.println(origItemJson);
			
			String outJson = super.fluentTemplate
						.to("undertow:http://localhost:" + port1 + "/items")
						.withHeader(Exchange.HTTP_METHOD, "GET")
						.withHeader("Accept", "application/json")
						.request(String.class);
			
			assertEquals(origItemJson, outJson);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@Ignore
	public void testGetOne() {
		try {
			final Item origItem = getItemService().getItem(0);

			final String origItemJson = objectWriter.writeValueAsString(origItem);
			
			out.println(origItemJson);
			
			
			String outJson = super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/items/0")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.withHeader("Accept", "application/json")
					.request(String.class);
			
			assertEquals(origItemJson, outJson);
			
			
			String outXml = super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/items/0")
					.withHeader("Accept", "application/xml")
					.request(String.class);
			
			JAXBContext jaXb = JAXBContext.newInstance(Item.class);
			
			Unmarshaller jaXbUnmarshal = jaXb.createUnmarshaller();
			
			Item itemOut = (Item) jaXbUnmarshal.unmarshal(new StringReader(outXml));
			
			assertEquals(origItem, itemOut);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@Ignore
	public void testGetOneJson() {
		
		try {
			String origValue = objectWriter.writeValueAsString(getItemService().getItem(0));
			String out = fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/items/0/json")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);

			assertEquals(origValue, out);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
	}
	
	@Test
	@Ignore
	public void testGetOneXml() {
		
		try {
			final Item origItem = getItemService().getItem(0);

			String out = fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/items/0/xml")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);

			JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Item itemOut = (Item) jaxbUnmarshaller.unmarshal(new StringReader(out));

			assertEquals(origItem, itemOut);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	@Test
	public void testSetOneXml() {

		try {
			
			final Item item = getItemService().getItem(0);

			// change name to something different
			item.setName(item.getName() + "Foo");

			// Move to XML
			JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			StringWriter sw = new StringWriter();

			jaxbMarshaller.marshal(item, sw);

			String xmlItem = sw.toString();

			fluentTemplate()
				.to("undertow:http://localhost:" + port1 + "/items/0")
				.withHeader(Exchange.HTTP_METHOD, "PUT")
				.withBody(xmlItem); //.request(String.class);

			assertEquals(item, getItemService().getItem(0));
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
