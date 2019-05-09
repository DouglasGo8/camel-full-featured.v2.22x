
package com.douglasdb.camel.feat.core.transform.normalizer;

import javax.xml.bind.JAXBContext;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import com.douglasdb.camel.feat.core.domain.jaxb.Bookstore;

/**
 * 
 */
public class NormalizerRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		final DataFormat bindy = new BindyCsvDataFormat(BookModel.class);
		final DataFormat jaxb = new JaxbDataFormat(JAXBContext.newInstance(Bookstore.class));

		// final XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();

		// xmlJsonFormat.setRootName("bookstore");
		// xmlJsonFormat.setElementName("book");
		// xmlJsonFormat.setExpandableProperties(Arrays.asList("author", "author"));

		from("direct:start")
			.log("${body}")
			.choice()
				.when(header(Exchange.FILE_NAME).endsWith(".xml"))
					.unmarshal(jaxb)
				.to("mock:xml")
				.when(header(Exchange.FILE_NAME).endsWith(".csv"))
					.unmarshal(bindy)
					.bean(MyNormalizer.class,  "bookModelToJaxb")
				.to("mock:csv")
			.end()
			.to("mock:normalized");
	}

}