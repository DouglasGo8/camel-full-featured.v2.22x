package com.douglasdb.camel.feat.core.test.marshal;

import com.douglasdb.camel.feat.core.marshal.MarshalDataFormatsRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MarshalDataFormatTest extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MarshalDataFormatsRoute();
    }



    @Test
    public void testStringToByteArray() throws InterruptedException {


        final String payload = "Camel Rocks!!!";

        super.getMockEndpoint("mock:result").expectedMessageCount(1);


        super.template.sendBody("direct:in", payload);


        assertMockEndpointsSatisfied();

    }
}
