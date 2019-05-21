package com.douglasdb.camel.feat.core.test.transform;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.douglasdb.camel.feat.core.transform.xquery.XqueryParamRoute;
import com.douglasdb.camel.feat.core.transform.xslt.XsltParamRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import com.douglasdb.camel.feat.core.domain.jaxb.Book;
import com.douglasdb.camel.feat.core.domain.jaxb.Bookstore;
import com.douglasdb.camel.feat.core.domain.json.View;
import com.douglasdb.camel.feat.core.enrich.AbbreviationExpander;


/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new //XsltParamRoute();
				XqueryParamRoute();
			// XmlJsonRoute();
			// SimpleRoute();
			// NormalizerRouter();
			// JsonJacksonRoute();
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

		// System.out.println(response);
		final String expects = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n"
				+ "PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";

		assertEquals(expects, response);
	}

	@Test
	@Ignore
	public void testCsvUnmarshal() throws Exception {

		final String request = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n"
				+ "PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";

		@SuppressWarnings("unchecked")
		final List<BookModel> response = Collections
				.checkedList(super.template.requestBody("direct:unmarshal", request, List.class), BookModel.class);

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
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		String response = template.requestBody("direct:start", request, String.class);

		log.info("Response = {}", response);
		assertEquals(
				"<books><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>",
				response);
	}

	@Test
	@Ignore
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
	@Ignore
	public void testJsonUnmarshal() throws Exception {
		final String request = "{\"com.douglasdb.camel.feat.core.domain.json.View\":{\"age\":29,\"weight\":34,\"height\":46}}";

		View response = template.requestBody("direct:unmarshal_xstream", request, View.class);

		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		assertEquals(view, response);
	}

	@Test
	@Ignore
	public void testNormalizeXml() {

		final InputStream resource = getClass().getResourceAsStream("/META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		// System.out.println(request);

		try {

			super.getMockEndpoint("mock:unknown").setExpectedMessageCount(0);
			super.getMockEndpoint("mock:csv").setExpectedMessageCount(0);
			super.getMockEndpoint("mock:json").setExpectedMessageCount(0);

			super.getMockEndpoint("mock:xml").expectedBodiesReceived(this.getExpectedBookstore());
			super.getMockEndpoint("mock:normalized").expectedBodiesReceived(getExpectedBookstore());
			
			super.template.sendBodyAndHeader("direct:start", request, Exchange.FILE_NAME, "bookstore.xml");

			assertMockEndpointsSatisfied();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Test
	@Ignore
	public void testNormalizeCsv() throws Exception {
		
		final InputStream resource = getClass().getResourceAsStream("/META-INF/bookstore/bookstore.csv");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		super.getMockEndpoint("mock:unknown").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:json").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:xml").setExpectedMessageCount(0);

		super.getMockEndpoint("mock:csv").expectedBodiesReceived(getExpectedBookstore());
		super.getMockEndpoint("mock:normalized").expectedBodiesReceived(getExpectedBookstore());
		
		
		
		template.sendBodyAndHeader("direct:start", request, Exchange.FILE_NAME, "bookstore.csv");

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testNormalizeJson() throws Exception {

		final InputStream resource = getClass().getResourceAsStream("/META-INF/bookstore/bookstore.json");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		super.getMockEndpoint("mock:unknown").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:csv").setExpectedMessageCount(0);
		super.getMockEndpoint("mock:xml").setExpectedMessageCount(0);

		super.getMockEndpoint("mock:json").expectedBodiesReceived(getExpectedBookstore());
		super.getMockEndpoint("mock:normalized").expectedBodiesReceived(getExpectedBookstore());
			
		template.sendBodyAndHeader("direct:start", request, Exchange.FILE_NAME, "bookstore.json");

		assertMockEndpointsSatisfied();
	}



	@Test
	@Ignore
    public void testNormalizeUnknown() throws Exception {
        getMockEndpoint("mock:csv").setExpectedMessageCount(0);
        getMockEndpoint("mock:json").setExpectedMessageCount(0);
        getMockEndpoint("mock:xml").setExpectedMessageCount(0);
        getMockEndpoint("mock:normalized").setExpectedMessageCount(0);

        final MockEndpoint mockUnknown = getMockEndpoint("mock:unknown");
        mockUnknown.expectedBodiesReceived("Unknown Data");
        mockUnknown.expectedHeaderReceived(Exchange.FILE_NAME, "bookstore.unknown");

        template.sendBodyAndHeader("direct:start", "Unknown Data", Exchange.FILE_NAME, "bookstore.unknown");

        assertMockEndpointsSatisfied();
    }



	@Test
	@Ignore
	public void testSimple() throws Exception {
		String response = template.requestBody("direct:start", "Camel Rocks", String.class);

		assertEquals("Hello Camel Rocks", response);
	}

	@Test
	@Ignore
	public void testXmlJsonMarshal() throws Exception {

		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		final String response = template.requestBody("direct:marshal", request, String.class);


	}


	@Test
	@Ignore
	public void testXmlJsonUnmarshal() throws Exception {
		final String request = "[" +
				"{\"@category\":\"COOKING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Everyday Italian\"},\"author\":\"Giada De Laurentiis\",\"year\":\"2005\",\"price\":\"30.00\"}," +
				"{\"@category\":\"CHILDREN\",\"title\":{\"@lang\":\"en\",\"#text\":\"Harry Potter\"},\"author\":\"J K. Rowling\",\"year\":\"2005\",\"price\":\"29.99\"}," +
				"{\"@category\":\"WEB\",\"title\":{\"@lang\":\"en\",\"#text\":\"Learning XML\"},\"author\":\"Erik T. Ray\",\"year\":\"2003\",\"price\":\"39.95\"}," +
				"{\"@category\":\"PROGRAMMING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Apache Camel Developer's Cookbook\"},\"author\":[\"Scott Cranton\",\"Jakub Korab\"],\"year\":\"2013\",\"price\":\"49.99\"}" +
				"]";

		final String response = template.requestBody("direct:unmarshal", request, String.class);

		log.info(response);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
				"<a>" +
				"<e category=\"COOKING\"><author>Giada De Laurentiis</author><price>30.00</price><title lang=\"en\">Everyday Italian</title><year>2005</year></e>" +
				"<e category=\"CHILDREN\"><author>J K. Rowling</author><price>29.99</price><title lang=\"en\">Harry Potter</title><year>2005</year></e>" +
				"<e category=\"WEB\"><author>Erik T. Ray</author><price>39.95</price><title lang=\"en\">Learning XML</title><year>2003</year></e>" +
				"<e category=\"PROGRAMMING\"><author><e>Scott Cranton</e><e>Jakub Korab</e></author><price>49.99</price><title lang=\"en\">Apache Camel Developer's Cookbook</title><year>2013</year></e>" +
				"</a>\r\n", response);


	}


	@Test
	public void testXmlJsonUnmarshalBookstore() throws Exception {
		final String request = "[" +
				"{\"@category\":\"COOKING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Everyday Italian\"},\"author\":\"Giada De Laurentiis\",\"year\":\"2005\",\"price\":\"30.00\"}," +
				"{\"@category\":\"CHILDREN\",\"title\":{\"@lang\":\"en\",\"#text\":\"Harry Potter\"},\"author\":\"J K. Rowling\",\"year\":\"2005\",\"price\":\"29.99\"}," +
				"{\"@category\":\"WEB\",\"title\":{\"@lang\":\"en\",\"#text\":\"Learning XML\"},\"author\":\"Erik T. Ray\",\"year\":\"2003\",\"price\":\"39.95\"}," +
				"{\"@category\":\"PROGRAMMING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Apache Camel Developer's Cookbook\"},\"author\":[\"Scott Cranton\",\"Jakub Korab\"],\"year\":\"2013\",\"price\":\"49.99\"}" +
				"]";

		final String response = template.requestBody("direct:unmarshalBookstore", request, String.class);

		log.info(response);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
				"<bookstore>" +
				"<book category=\"COOKING\"><author>Giada De Laurentiis</author><price>30.00</price><title lang=\"en\">Everyday Italian</title><year>2005</year></book>" +
				"<book category=\"CHILDREN\"><author>J K. Rowling</author><price>29.99</price><title lang=\"en\">Harry Potter</title><year>2005</year></book>" +
				"<book category=\"WEB\"><author>Erik T. Ray</author><price>39.95</price><title lang=\"en\">Learning XML</title><year>2003</year></book>" +
				"<book category=\"PROGRAMMING\"><author>Scott Cranton</author><author>Jakub Korab</author><price>49.99</price><title lang=\"en\">Apache Camel Developer's Cookbook</title><year>2013</year></book>" +
				"</bookstore>\r\n", response);
	}


	@Test
	@Ignore
	public void testXqueryParam() throws Exception {

		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);


		String response = template.requestBodyAndHeader("direct:start",
				request, "myParamValue", new Integer(30), String.class);

	}

	@Test
	@Ignore
	public void testXquery() throws Exception {
		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		String response = template.requestBody("direct:start2", request, String.class);

		log.info("Response = {}", response);
		assertEquals("<books><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>", response);
	}




	@Test
	@Ignore
	public void testXsltParam() throws Exception {
		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		String response = template.requestBodyAndHeader("direct:start", request, "myParamValue", 30, String.class);

		log.info("Response > 30 = {}", response);
		assertEquals("<books value=\"30\"><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>", response);

		response = template.requestBodyAndHeader("direct:start", request, "myParamValue", 40, String.class);

		log.info("Response > 40 = {}", response);
		assertEquals("<books value=\"40\"><title lang=\"en\">Apache Camel Developer's Cookbook</title></books>", response);
	}


	@Test
	public void testXslt() throws Exception {
		final InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/bookstore/bookstore.xml");
		final String request = context().getTypeConverter().convertTo(String.class, resource);

		String response = template.requestBody("direct:start2", request, String.class);

		log.info("Response = {}", response);
		assertEquals("<books><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>", response);
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

		return books;
	}

	/**
	 * 
	 * @return
	 */
	protected Bookstore getExpectedBookstore() {
		final Bookstore bookstore = new Bookstore();

		Book book = new Book();

		book.setCategory("COOKING");

		Book.Title title = new Book.Title();
		title.setValue("Everyday Italian");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Giada De Laurentiis");
		book.setYear(2005);
		book.setPrice(30.00);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("CHILDREN");

		title = new Book.Title();
		title.setValue("Harry Potter");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("J K. Rowling");
		book.setYear(2005);
		book.setPrice(29.99);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("WEB");

		title = new Book.Title();
		title.setValue("Learning XML");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Erik T. Ray");
		book.setYear(2003);
		book.setPrice(39.95);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("PROGRAMMING");

		title = new Book.Title();
		title.setValue("Apache Camel Developer's Cookbook");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Scott Cranton");
		book.getAuthor().add("Jakub Korab");
		book.setYear(2013);
		book.setPrice(49.99);

		bookstore.getBook().add(book);

		return bookstore;
	}

}

