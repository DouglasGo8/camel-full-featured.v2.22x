

package com.douglasdb.camel.feat.core.transform.jaxb;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * 
 */
public class JaxbRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        DataFormat myJaxb = new JaxbDataFormat();


        from("direct:marshal")
            .marshal(myJaxb)
            .to("mock:marshalResult");


    }

    
    
}