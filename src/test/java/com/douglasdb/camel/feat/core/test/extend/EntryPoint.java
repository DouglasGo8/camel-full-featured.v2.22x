package com.douglasdb.camel.feat.core.test.extend;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.domain.bean.MyBean;
import com.douglasdb.camel.feat.core.extend.MyProcessorInlineRoute;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new MyProcessorInlineRoute();

		// MyPredicateBeanBindingRoute();

		// EbcdicDataFormatRoute();
		// MyBeanRoute();
	}

	@Test
	@Ignore
	public void testHello() {

		MyBean bean = new MyBean();

		final String result = bean.sayHello("Scott", false);

		assertEquals("Hello Scott", result);
	}

	@Test
	@Ignore
	public void testHipster() {

		MyBean bean = new MyBean();

		final String result = bean.sayHello("Scott", true);

		assertEquals("Yo Scott", result);
	}

	@Test
	@Ignore
	public void testSayCamelHello() {

		final String response = super.template.requestBody("direct:normal", "Scott", String.class);

		assertEquals("Hello Scott", response);
	}

	@Test
	@Ignore
	public void testSayHelloHipster() {

		final String response = super.template.requestBody("direct:hipster", "Scott", String.class);

		assertEquals("Yo Scott", response);
	}

	@Test
	@Ignore
	public void testSayHelloUndecided() throws Exception {
		String response = super.template.requestBodyAndHeader("direct:undecided", "Scott", "hipster", true,
				String.class);

		assertEquals("Yo Scott", response);

		response = template.requestBodyAndHeader("direct:undecided", "Scott", "hipster", false, String.class);

		assertEquals("Hello Scott", response);
	}

	@Test
	@Ignore
	public void testMarshalEbcdic() {

		final String ascii = "Mainframes rock!";

		final byte[] expected = { -44, -127, -119, -107, -122, -103, -127, -108, -123, -94, 64, -103, -106, -125, -110,
				90 };

		final byte[] result = template.requestBody("direct:marshal", ascii, byte[].class);

		assertArrayEquals(expected, result);

	}

	@Test
	@Ignore
	public void testUnmarshalEbcdic() throws Exception {
		final byte[] ebcdic = { -29, -119, -108, -123, 64, -93, -106, 64, -92, -105, -121, -103, -127, -124, -123, 75,
				75, 75 };

		final String result = template.requestBody("direct:unmarshal", ebcdic, String.class);

		assertEquals("Time to upgrade...", result);
	}

	@Test
	@Ignore
	public void testMyPredicateBeanBinding() throws InterruptedException {

		final String newYork = "<someXml><city>New York</city></someXml>";
		final String boston = "<someXml><city>Boston</city></someXml>";

		super.getMockEndpoint("mock:boston").expectedBodiesReceived(boston);

		super.template.sendBody("direct:start", newYork);
		super.template.sendBody("direct:start", boston);

		assertMockEndpointsSatisfied();

	}

	@Test
	@Ignore
	public void testMyInlineProcessor() throws InterruptedException {

		super.getMockEndpoint("mock:result").expectedBodiesReceived("Hello Scott");

		super.template.sendBodyAndHeader("direct:start", "Scott", "lang", "en");

		assertMockEndpointsSatisfied();
	}

	@Test
	public void testMyInlineProcessor2() throws Exception {
		getMockEndpoint("mock:result").expectedBodiesReceived("Bonjour Scott");

		template.sendBodyAndHeader("direct:start", "Scott", "lang", "fr");

		assertMockEndpointsSatisfied();
	}

}
