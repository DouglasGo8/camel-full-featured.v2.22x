package com.douglasdb.camel.feat.core.transform.xmljson;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;

import java.util.Arrays;

/**
 *
 */
public class XmlJsonRoute extends RouteBuilder {

    /**
     *
     * @throws Exception
     */
	@Override
	@SuppressWarnings("deprecation")
    public void configure() throws Exception {

        from("direct:marshal")
                .marshal().xmljson()
                .log("${body}")
                .to("mock:marshResult");

        from("direct:unmarshal")
                .unmarshal().xmljson()
                .to("mock:unmarshalResult");


        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setRootName("bookstore");
        xmlJsonFormat.setElementName("book");
        xmlJsonFormat.setExpandableProperties(Arrays.asList("author", "author"));

        from("direct:unmarshalBookstore")
                .unmarshal(xmlJsonFormat)
                .to("mock:unmarshalBookstoreResult");

    }
}
