package com.douglasdb.camel.feat.core.transform.normalizer;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.apache.camel.spi.DataFormat;

import java.util.Arrays;

import javax.xml.bind.JAXBContext;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import com.douglasdb.camel.feat.core.domain.jaxb.Bookstore;


/**
 * @author DouglasD.b
 */
public class NormalizerRouter extends RouteBuilder {

	
	/**
	 * 
	 */
	@Override
	@SuppressWarnings(value = "deprecation")
	public void configure() throws Exception {

		final DataFormat bindy = new BindyCsvDataFormat(BookModel.class);
		final DataFormat jaxb = new JaxbDataFormat(JAXBContext.newInstance(Bookstore.class));

		
		final XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setRootName("bookstore");
        xmlJsonFormat.setElementName("book");
		xmlJsonFormat.setExpandableProperties(Arrays.asList("author", "author"));

		
		

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
				.when(header(Exchange.FILE_NAME).endsWith(".json"))
					.unmarshal(xmlJsonFormat)
					.to("mock:json")
				.otherwise()
					.to("mock:unknown")
					.stop()
			.end()
			.to("mock:normalized");
	}

}