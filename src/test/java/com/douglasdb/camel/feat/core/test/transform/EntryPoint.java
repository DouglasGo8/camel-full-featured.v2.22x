package com.douglasdb.camel.feat.core.test.transform;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import com.douglasdb.camel.feat.core.domain.json.View;
import com.douglasdb.camel.feat.core.enrich.AbbreviationExpander;
import com.douglasdb.camel.feat.core.transform.json.JsonJacksonRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;


/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {
	
	
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new JsonJacksonRoute();
				// EnrichXsltRoute();
				// EnrichRoute();
				// CsvRoute();
				// OrderToCsvBeanRoute();
	}

	@Override
	protected void doPreSetup() throws Exception {
		super.doPreSetup();

		Locale.setDefault(Locale.US);
	}

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry jndiRegistry = super.createRegistry();

		jndiRegistry.bind("myExpander", new AbbreviationExpander());

		return jndiRegistry;
	}

	@Test
	@Ignore
	public void testOrderToCsvBean() {
		
		String inHouse = "0000005555000001144120091209  2319@1108";
		
		super.template.sendBodyAndHeader("direct:start", inHouse, "Date", "20181112");
		final File file = new File("D:/.camel/data/inbox/minimal/report-20091209.csv");
		assertTrue("File should exist", file.exists());
		
		String body = super.context.getTypeConverter().convertTo(String.class, file);
		
		
		assertEquals("0000005555,20091209,0000011441,2319,1108", body);
		
		
	}


	@Test
	@Ignore
	public void testCsvMarshal() throws Exception {

		String response = super.template.requestBody("direct:marshal", this.getBookModel(), String.class);

		//System.out.println(response);
		final String expects = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n" +
				"PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";

		assertEquals(expects, response);
	}

	@Test
	@Ignore
	public void testCsvUnmarshal() throws Exception {

		final String request = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n" +
				"PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";

		@SuppressWarnings("unchecked")
		final List<BookModel> response = Collections.checkedList(
				super.template.requestBody("direct:unmarshal", request, List.class), BookModel.class);

		System.out.println(response.get(0));
		assertEquals(this.getBookModel().get(0), response.get(0));

	}


	@Test
	@Ignore
	public void testEnrich() {

		String response = template.requestBody("direct:start", "MA", String.class);

		assertEquals("Massachusetts", response);

		response = template.requestBody("direct:start", "CA", String.class);

		assertEquals("California", response);

	}

	@Test
	@Ignore
	public void testEnrichXslt() throws Exception {
		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		String response = template.requestBody("direct:start", request, String.class);

		log.info("Response = {}", response);
		assertEquals("<books><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>", response);
	}

	@Test
	public void testJsonJacksonMarshal() {
		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		String response = super.template.requestBody("direct:marshal", view, String.class);

		log.info(response);
		assertEquals("{\"age\":29,\"weight\":34,\"height\":46}", response);

	}


	@Test
	@Ignore
	public void testJsonJacksonUnmarshal() {

		final String request = "{\"age\":29,\"weight\":34,\"height\":46}";


		View response = super.template.requestBody("direct:unmarshal", request, View.class);


		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		assertEquals(view, response);

	}


	@Test
	@Ignore
	public void testJsonJacksonMarshalXStream() {
		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		String response = super.template.requestBody("direct:marshal_xstream", view, String.class);

		log.info(response);
		assertEquals("{\"com.douglasdb.camel.feat.core.domain.json.View\":{\"age\":29,\"weight\":34,\"height\":46}}",
				response);

	}


	@Test
	public void testJsonUnmarshal() throws Exception {
		final String request = "{\"com.douglasdb.camel.feat.core.domain.json.View\":{\"age\":29,\"weight\":34,\"height\":46}}";

		View response = template.requestBody("direct:unmarshal_xstream", request, View.class);

		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		assertEquals(view, response);
	}





	/**
	 *
	 * @return
	 * @throws Exception
	 */
	private ArrayList<BookModel> getBookModel() throws Exception {
		final ArrayList<BookModel> books = new ArrayList<>();
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy");

		BookModel book = new BookModel();
		book.setCategory("PROGRAMMING");
		book.setTitle("Camel in Action");
		book.setTitleLanguage("en");
		book.setAuthor1("Claus Ibsen");
		book.setAuthor2("Jon Anstey");
		book.setPublishDate(simpleDateFormat.parse("Dec-2010"));
		book.setPrice(BigDecimal.valueOf(49.99));

		books.add(book);


		book = new BookModel();
		book.setCategory("PROGRAMMING");
		book.setTitle("Apache Camel Developer's Cookbook");
		book.setTitleLanguage("en");
		book.setAuthor1("Scott Cranton");
		book.setAuthor2("Jakub Korab");
		book.setPublishDate(simpleDateFormat.parse("Dec-2013"));
		book.setPrice(BigDecimal.valueOf(49.99));

		books.add(book);

		return  books;
	}
	

}
